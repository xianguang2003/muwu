local string_util = require("util.string_util");
local web_util = require("util.web_util");
local json_util = require("util.json_util");
local base64_util = require("util.base64_util");
local constants = require("common.constants");
local config = require("common.config");

local _M = {};

function _M.get_authorization_value()
    local clientId = string_util.to_string_trim(ngx.var.client_id);
    local clientSecret = string_util.to_string_trim(ngx.var.client_secret);
    if string_util.is_not_blank(clientId) and string_util.is_not_blank(clientSecret) then
        return "Basic " .. base64_util.enc(clientId .. ":" .. clientSecret);
    end
    return nil;
end

function _M.return_message(code, content)
    local result_content = {};
    result_content["code"] = code;
    result_content["message"] = content;
    result_content["result"] = {};
    return json_util.encode(result_content);
end

function _M.error_message(content)
    local result_content = {};
    result_content["code"] = 500;
    result_content["message"] = content;
    result_content["result"] = {};
    return json_util.encode(result_content);
end

function _M.success_message(content)
    if type(content) ~= "table" then
        content = jsonUtil.decode(content)
    end
    local result_content = {};
    result_content["code"] = 200;
    result_content["message"] = "success";
    result_content["result"] = content;
    return json_util.encode(result_content);
end

-- 程序错误，直接跳转到错误页面
function _M.return_page(content)
    if type(content) == "table" then
        content = json_util.encode(content)
    end
    web_util.set_response_header("Content-Type", "application/json");
    if (ngx.ctx.isAjax) then
        ngx.print(content);
        ngx.exit(ngx.HTTP_OK);
    else
        ngx.print(content);
        ngx.exit(ngx.HTTP_OK);
    end
end

function _M.url_encode(s)
    s = string.gsub(s, "([^%w%.%- ])", function(c) return string.format("%%%02X", string.byte(c)) end);
    return string.gsub(s, " ", "+");
end

function _M.url_decode(s)
    s = string.gsub(s, "%%(%x%x)", function(h) return string.char(tonumber(h, 16)) end);
    return s;
end

-- 获取redis配置并以table的形式返回
function _M.get_redis_config()
    local redis_config = {};
    redis_config["ip"] = config.get(constants.REDIS_IP);
    redis_config["port"] = config.get(constants.REDIS_PORT);
    redis_config["timeout"] = config.get(constants.REDIS_TIMEOUT);
    redis_config["password"] = config.get(constants.REDIS_PASSWORD);
    redis_config["pool_size"] = config.get(constants.REDIS_POOL_SIZE);
    redis_config["pool_max_idle_time"] = config.get(constants.REDIS_MAX_IDLE_TIME);
    redis_config["db_index"] = config.get(constants.REDIS_SELECT);
    return redis_config;
end

return _M;

