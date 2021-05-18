local constants = require("common.constants");
local common = require("common.common");
local config = require("common.config");
local string_util = require("util.string_util");
local web_util = require("util.web_util");
local json_util = require("util.json_util");
local http_util = require("util.http_util");
local jwt = require("lib.jwt.resty.jwt");

local _M = {};

function _M:jwt_user_info(token)
    if string_util.is_blank(token) then
        return null;
    end
    ngx.log(ngx.INFO, "Token: " .. token)
    local jwt_result = jwt:verify(config.get(constants.JWT_SECRET), token)
    if jwt_result.verified == false then
        ngx.log(ngx.WARN, "Invalid token: ".. jwt_result.reason)
        return null;
    end
    ngx.log(ngx.INFO, "JWT: " .. json_util.encode(jwt_result))
    return jwt_result.payload.principal;
end

function _M:interface_user_info(token)
    if string_util.is_blank(token) then
        return null;
    end
    local request_body = "token=" .. token;
    local is_success, code, response_body, response_err, response_header = http_util.request(config.get(constants.OAUTH_DOMAIN) .. config.get(constants.GET_USER_INFO), {
        method = "POST",
        ssl_verify = false,
        body = request_body,
        headers = {
            ["Content-Type"] = "application/x-www-form-urlencoded",
            ["Authorization"] = "bearer " .. token
        }
    }, 6000);
    ngx.log(ngx.INFO, "code:" .. code .. ";response_body:" .. (response_body and response_body or "") .. ";response_err:" .. (response_err and response_err or ""));
    if is_success then
        response_body = json_util.decode(response_body);
        if response_body.code == 200 then
            return json_util.encode(response_body.result)
        else
            return null;
        end
    else
        return null;
    end
end

function _M:get_user_info(token)
    if config.get(constants.VERIFY_TOKEN_MODE) == constants.VERIFY_TOKEN_MODE_JWT then
        return self:jwt_user_info(token);
    else
        return self:interface_user_info(token);
    end
end

return _M;