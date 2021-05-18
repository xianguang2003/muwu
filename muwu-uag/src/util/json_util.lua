local cjson = require("cjson.safe");
cjson.encode_empty_table_as_object(false); 

local _M = {};

-- 编码
-- 比如table转换成字符串
function _M.encode(data)
    return cjson.encode(data);
end

-- 解码
-- 比如字符串解析成table
function _M.decode(data)
    return cjson.decode(data);
end

return _M;
