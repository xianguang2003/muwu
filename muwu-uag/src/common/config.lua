local M = {};

-- 获取相应配置的值
function M.get(key)
    return ngx.shared.config:get(key);
end

-- 把配置的值设置到共享内存中
function M.set(key, value)
    ngx.shared.config:set(key, value);
end

return M;