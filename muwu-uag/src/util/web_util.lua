--[[	
	web请求，request、response等公用方法
]]--

local string_util = require("util.string_util");

local _M = {};

-- 获取请求中所有头部信息
function _M.get_request_headers()
    return ngx.req.get_headers();
end

-- 获取请求头部信息
function _M.get_request_header(name)
    return string_util.to_string_trim(_M.get_request_headers()[name]);
end

-- 设置请求头部
function _M.set_request_header(key, value)
    ngx.req.set_header(key, value);
end

-- 请求请求头部key
function _M.clear_request_header(key)
    ngx.req.clear_header(key);
end

-- 设置响应中的头部信息
function _M.set_response_header(key, value)
    ngx.header[key] = value;
end

-- 获取响应中的头部信息
function _M.get_response_header(key)
    return ngx.header[key];
end

-- 获取域名比如http://www.muwu.com/aa/bb?a=b,则返回www.muwu.com
function _M.get_host()
    return string_util.to_string_trim(ngx.var.host);
end

-- 获取请求方式get、post、put、delete
-- 或者用ngx.req.get_method()，经测试应该相同的含义
function _M.get_request_method()
    return string_util.to_string_trim(ngx.var.request_method);
end

-- 获取uri,比如http://www.muwu.com/aa/bb?a=b,则返回/aa/bb
function _M.get_uri()
    return string_util.to_string_trim(ngx.var.uri);
end

-- 获取request_uri,比如http://www.muwu.com/aa/bb?a=b,则返回/aa/bb?a=b
function _M.get_request_uri()
    return string_util.to_string_trim(ngx.var.request_uri);
end

-- 获取请求的参数，注意和_M.getVarArgs()的区别
-- 如果没有参数，此方法会返回空table
-- 注意若参数为a=b&c=d&a=x，获取到的a对应的值还是一个table，里面包含b和x
function _M.get_uri_args()
    return ngx.req.get_uri_args();
end

-- 获取请求参数key的值
-- 示例a=b&c=d&a=x，则获取c时候返回字符串d，获取a时候获取是table
--经测试，这个方法获取到的参数是已经urlDecode过的值
function _M.get_uri_arg(key)
    return _M.get_uri_args()[key];
end

-- 获取请求的的参数，注意和_M.getUriArgs()的区别
-- 如果没有参数，此方法会返回nil
-- 此方法直接返回字符串，例如a=b&c=d&a=x则直接返回此字符串
function _M.get_var_args()
    return ngx.var.args;
end

-- 获取请求参数key的值
-- 示例a=b&c=d&a=x，则获取c时候返回字符串d，获取a时候获取是字符串b(即第一个参数值)
-- 经测试，这个方法获取到的参数没有进行urlDecode，但是getUriAgr方法获取到的参数就是已经urlDecode过的值
function _M.get_var_arg(key)
    return ngx.var["arg_" .. key];
end

-- 获取请求者的ip地址
-- 首先获取x-forwarded-for头信息，若存在则返回，若不存在则进行下一步
-- 获取ngx.var.remote_addr
function _M.get_remote_ip()
    local remote_ip = _M.get_request_header("x-forwarded-for");
    if (string_util.is_not_blank(remote_ip)) then
        return remote_ip;
    end
    return string_util.to_string_trim(ngx.var.remote_addr);
end

-- 获取请求的端口号
function _M.get_server_port()
    return ngx.var.server_port;
end

-- 获取请求的scheme
function _M.get_scheme()
    return ngx.var.scheme;
end

-- 返回请求体，一般是post请求时候可以获取到请求体
function _M.get_response_body()
    ngx.req.read_body();
    return string_util.to_string_trim(ngx.req.get_body_data());
end

-- 获取响应的状态
function _M.get_response_status()
    return string_util.to_string_trim(ngx.status);
end

return _M;