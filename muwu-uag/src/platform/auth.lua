local constants = require("common.constants");
local common = require("common.common");
local config = require("common.config");
local string_util = require("util.string_util");
local web_util = require("util.web_util");
local cookie_util = require("util.cookie_util");
local json_util = require("util.json_util");
local http_util = require("util.http_util");
local jwt = require("lib.jwt.resty.jwt");
local user = require("platform.user");
local redis = require("common.redis_pool"):new(common.get_redis_config());

local _M = {};

function _M:verify_jwt_token(token)
    if string_util.is_blank(token) then
        return false;
    end
    ngx.log(ngx.INFO, "Token: " .. token)
    local jwt_result = jwt:verify(config.get(constants.JWT_SECRET), token)
    if jwt_result.verified == false then
        ngx.log(ngx.WARN, "Invalid token: " .. jwt_result.reason)
        return false;
    end
    local expiration = jwt_result.payload.exp;
    local current = os.time();
    if current > expiration then
        ngx.log(ngx.WARN, "jwt expired")
        return false;
    end
    ngx.log(ngx.INFO, "expiration: " .. expiration .. ";current: " .. current)
    ngx.log(ngx.INFO, "JWT: " .. json_util.encode(jwt_result))
    return true, jwt_result.payload.authorities;
end

function _M:verify_interface_token(token)
    if string_util.is_blank(token) then
        return false;
    end
    local request_body = "token=" .. token;
    local is_success, code, response_body, response_err, response_header = http_util.request(config.get(constants.OAUTH_DOMAIN) .. config.get(constants.CHECK_TOKEN), {
        method = "POST",
        ssl_verify = false,
        body = request_body,
        headers = {
            ["Content-Type"] = "application/x-www-form-urlencoded"
        }
    }, 6000);
    ngx.log(ngx.INFO, "is_success:" .. (is_success and "true" or "false") .. ";code:" .. code .. ";response_body:" .. (response_body and response_body or "") .. ";response_err:" .. (response_err and response_err or ""));
    if is_success then
        response_body = json_util.decode(response_body);
        if response_body.active then
            return true, response_body.authorities;
        else
            return false;
        end
    else
        return false;
    end
end

function _M:verify_token(token)
    ngx.log(ngx.INFO, "VERIFY_TOKEN_MODE: " .. config.get(constants.VERIFY_TOKEN_MODE));
    if config.get(constants.VERIFY_TOKEN_MODE) == constants.VERIFY_TOKEN_MODE_JWT then
        return self:verify_jwt_token(token);
    else
        return self:verify_interface_token(token);
    end
end

function _M:verify_permission(authorities)
    local request_uri = web_util.get_uri();
    -- 验证权限
    for _, authoritie in ipairs(authorities) do
        -- 先写死superadmin角色拥有所有权限
        if authoritie == "superadmin" then
            return true;
        end
        -- 先写死visitor角色只能访问查询接口
        if authoritie == "visitor" then
            if string_util.contains(request_uri, "/get") or string_util.contains(request_uri, "/list") or string_util.contains(request_uri, "/query") or string_util.contains(request_uri, "/statistics") then
                return true;
            end
            -- return false;
        end
        local uris = redis:hget(constants.ROLE_KEY, authoritie);
        if nil ~= uris and 0 ~= #uris then
            uris = json_util.decode(uris)
            for _, uri in ipairs(uris) do
                ngx.log(ngx.INFO, "uris: " .. request_uri .. ";uri: " .. uri)
                --if string_util.starts_with(request_uri, uri) then
                if string_util.ends_with(uri, "*") then
                    uri = string_util.wipeoff_end(uri, "*")
                    if string_util.starts_with(request_uri, uri) then
                        ngx.log(ngx.INFO, "true uris: " .. request_uri .. ";uri: " .. uri)
                        return true;
                    end
                end
                if request_uri == uri then
                    ngx.log(ngx.INFO, "true uris: " .. request_uri .. ";uri: " .. uri)
                    return true;
                end
            end
        end
    end
    return false;
end

function _M:auth(token)
    ngx.log(ngx.INFO, "token: " .. token)
    if token == nil then
        common.return_page(common.return_message(5008, "token不能为空"));
        return false
    end
    -- 验证
    local active, authorities = self:verify_token(token);
    ngx.log(ngx.INFO, "active: " .. (active and 'true' or 'false') .. "; authorities: " .. (authorities and json_util.encode(authorities) or ""));
    if not active then
        common.return_page(common.return_message(5014, "token失效"));
        return false
    end
    -- 验证权限
    if not self:verify_permission(authorities) then
        common.return_page(common.return_message(401, "你没有权限访问"));
        return false
    end
    -- 获取用户信息
    local user_info = user:get_user_info(token);
    ngx.log(ngx.INFO, "user_info: " .. user_info)
    if string_util.is_not_blank(user_info) then
        web_util.set_request_header("Basic-User-Info", common.url_encode(user_info));
        return true
    else
        common.return_page(common.error_message("token失效"));
        return false
    end
end

--local WHITELIST_URIS = json_util.decode('["/muwu-auth/auth/getUserInfo", "/muwu-web-api/uc/user/getUserTree"]');

function _M:run()
    local match = false;
    -- 验证白名单
    local whitelist_uris = redis:smembers(constants.PERMISSION_WHITELIST_URLS_KEY);
    if whitelist_uris ~= nil then
        local request_uri = web_util.get_uri();
        for _, uri in ipairs(whitelist_uris) do
            --if (string_util.starts_with(request_uri, uri)) then
            if string_util.ends_with(uri, "*") then
                uri = string_util.wipeoff_end(uri, "*")
                if string_util.starts_with(request_uri, uri) then
                    match = true;
                    ngx.log(ngx.INFO, "request_uri:" .. (request_uri and request_uri or "") .. ";uri:" .. (uri and uri or ""));
                    break
                end
            end
            if request_uri == uri then
                match = true;
                ngx.log(ngx.INFO, "request_uri:" .. (request_uri and request_uri or "") .. ";uri:" .. (uri and uri or ""));
                break
            end
        end
    end
    -- 验证权限
    if not match then
        -- 获取token
        local authorization = web_util.get_request_header("Authorization");
        -- 验证token
        if string_util.is_not_blank(authorization) then
            -- 授权认证
            ngx.log(ngx.INFO, "Authorization: " .. authorization)
            -- 获取 bearer token
            local _, _, token = string.find(authorization, "bearer%s+(.+)")
            match = self:auth(token)
        end
    end
    -- 验证是否为监控或其它资源文件
    if not match then
        -- TODO 后期会先验证地址（监控会存入redis）在验证cookie
        local token = cookie_util.get_value("muwu_token");
        ngx.log(ngx.INFO, "cookie token: " .. (token and token or ''))
        if string_util.is_not_blank(token) then
            match = self:auth(token)
        else
            common.return_page(common.error_message("请先登录"));
        end
    end
    if not match then
        ngx.log(ngx.INFO, "未查询到访客地址:" .. web_util.get_uri());
        --ngx.redirect("http://127.0.0.1/login");
    end
    ngx.log(ngx.INFO, "platform auth success");
end

-- 入口main方法
function _M:main()
    ngx.log(ngx.INFO, "enter into platform auth");
    -- 验证是否登录
    self:run();
end

_M:main();

return _M;
