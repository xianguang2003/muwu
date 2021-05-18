local http = require "lib.http.http";

local _M = {};

-- 发起请求,lua_resty_http
function _M.request(uri, options, timeout)
    local is_success, code, response_body, response_err, response_header;
    local httpc = http.new();
    if (timeout == nil or timeout == ngx.null) then
        timeout = 10000;
    end
    httpc:set_timeout(timeout);
    local res, err = httpc:request_uri(uri, options);
    if res then
        if res.status == ngx.HTTP_OK then
            is_success = true;
            code = res.status;
            response_body = res.body;
            response_header = res.headers;
        else
            is_success = false;
            code = res.status;
            response_body = res.body;
            response_header = res.headers;
        end
    else
        is_success = false;
        code = -1;
        response_err = err;
    end
    return is_success, code, response_body, response_err, response_header;
end

--[[ capture方式模拟httpclient，只用于内部
	需要在nginx配置文件中添加
	location ~/_uagHttpclientProxy/(.*) {
        internal;
        proxy_pass http://wec-iap-auth/$1$is_args$args;
	}
--]]
function _M.capture(uri, options)
    local isSuccess, code, bodyResult, errResult;
    local err = "";
    local res = ngx.location.capture("/_uagHttpclientProxy/" .. uri, options);
    if res then
        if res.status == ngx.HTTP_OK then
            isSuccess = true;
            code = res.status;
            bodyResult = res.body;
        else
            isSuccess = false;
            code = res.status;
        end
    else
        isSuccess = false;
        code = -1;
        errResult = err;
    end

    return isSuccess, code, bodyResult, errResult;
end

return _M;