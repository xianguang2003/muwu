local _M = {};

-- 去除前后空格,参数可以是任意值，但是我只会处理string类型
function _M.trim(str)
    if type(str) ~= "string" or str == ngx.null then
        return str;
    end
    return str:gsub("^%s+", ""):gsub("%s+$", "");
end

-- nil变成""，其他类型变成string、同时trim
function _M.to_string_trim(str)
    if str == nil or str == ngx.null then
        return "";
    else
        return _M.trim(tostring(str));
    end
end

-- nil变成""，其他类型变成string、同时trim,若为空字符串则返回默认值
function _M.to_string_trim_default(str, default_str)
    str = _M.to_string_trim(str);
    if str == "" then
        return default_str;
    else
        return str;
    end
end

-- 判断是否为空字符串，参数只能是字符串
function _M.is_blank(str)
    if str == nil or str == ngx.null then
        return true;
    else
        str = _M.trim(str);
        if str == "" then
            return true;
        else
            return false;
        end
    end
end

-- 判断是否为非空字符串，参数只能是字符串
function _M.is_not_blank(str)
    return not _M.is_blank(str);
end

-- 判断str是否以substr开头
function _M.starts_with(str, substr)
    if (_M.is_blank(str) or _M.is_blank(substr)) then
        return false;
    end

    local start_index = string.find(str, substr, 1, true);
    if start_index ~= nil and start_index == 1 then
        return true;
    else
        return false;
    end
end

-- 判断str是否以substr结尾
function _M.ends_with(str, substr)
    if (_M.is_blank(str) or _M.is_blank(substr)) then
        return false;
    end

    local index = string.len(str) - string.len(substr);
    local start_index, end_index = string.find(str, substr, index, true);
    if end_index ~= nil and end_index == string.len(str) then
        return true;
    else
        return false;
    end
end

-- 若end_str是str结尾则去除end_str结尾
function _M.wipeoff_end(str, end_str)
    if (_M.is_blank(str) or _M.is_blank(end_str)) then
        return str;
    end
    if (not _M.ends_with(str, end_str)) then
        return str;
    end

    local index = string.len(str) - string.len(end_str);
    return string.sub(str, 0, index);
end

-- 参数:待分割的字符串,分割字符
-- 返回:子串表.(含有空串)
function _M.split(str, split_char)
    local sub_str_tab = {};
    while (true) do
        local pos = string.find(str, split_char);
        if (not pos) then
            sub_str_tab[#sub_str_tab + 1] = str;
            break ;
        end
        local sub_str = string.sub(str, 1, pos - 1);
        sub_str_tab[#sub_str_tab + 1] = sub_str;
        str = string.sub(str, pos + 1, #str);
    end
    return sub_str_tab;
end

-- 判断str是否包含substr
function _M.contains(str, substr)
    local start_index, end_index = string.find(str, substr, 1);
    if (start_index ~= nil) then
        return true;
    else
        return false;
    end
end

-- 产生随机字符串
function _M.get_random_str()
    -- uuid
    local uuid = require("3rd.uuid");
    uuid.seed();
    local random_str = uuid();
    return random_str
end

-- 编码
function _M.encode(str)
    return ngx.escape_uri(str);
end

-- 解码
function _M.decode(str)
    return ngx.unescape_uri(str);
end

-- 转大写
function _M.upper(str)
    str = _M.to_string_trim(str);
    if (_M.is_blank(str)) then
        return "";
    end

    return string.upper(str);
end

-- 转小写
function _M.lower(str)
    str = _M.to_string_trim(str);
    if (_M.is_blank(str)) then
        return "";
    end

    return string.lower(str);
end

-- 在字符串中替换,mainString为要替换的字符串， findString 为被替换的字符，replaceString 要替换的字符
function _M.replace_all(main_string, find_string, replace_string)
    local start_index, end_index = string.find(main_string, find_string, 0, true);
    if (start_index == nil or end_index == nil) then
        return main_string;
    end

    main_string = string.sub(main_string, 0, startIndex - 1) .. replace_string .. string.sub(main_string, endIndex + 1, -1);

    return _M.replaceAll(main_string, find_string, replace_string);
end

return _M;

