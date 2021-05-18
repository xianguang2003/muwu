--[[
  	cookie工具lua
--]]
local web_util = require("util.web_util");

local _M = {};

-- 设置cookie的header信息，先获取已存在的header，然后add新的cookie操作，防止两次操作同时存在的情况
local function set_cookie_header(cookie)
    local headers = {};
    local exist_cookie = web_util.get_response_header("Set-Cookie");
    if exist_cookie ~= nil then
        table.insert(headers, exist_cookie);
    end
    table.insert(headers, cookie);
    web_util.set_response_header("Set-Cookie", headers);
end

-- 清除key对应的cookie
function _M.invalidate(key)
    local cookie = key .. "=; Max-Age=0; path=/; Httponly";
    set_cookie_header(cookie);
end

-- 获取key对应的cookie值
function _M.get_value(key)
    return ngx.var["cookie_" .. key];
end

-- 设置cookie
function _M.set_cookie(key, value)
    local cookie = key .. "=" .. value .. "; path=/; Httponly";
    set_cookie_header(cookie);
end

return _M;
