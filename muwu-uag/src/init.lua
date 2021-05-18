--[[
   	初始化脚本,在nginx启动的时候调用的脚本，对应init_by_lua_file。 	
   	
   	功能：
   		读取配置文件，把配置存储到共享内存中。
--]]

local config = require("common.config");
local json_util = require("util.json_util");
local constants = require("common.constants");
--配置文件路径
local configPath = constants.CONFIG_PATH;
ngx.log(ngx.INFO, "Init, reading config.json,path[" .. configPath .."]");

-- 读取配置文件
local file = io.open(configPath, "r");
if (file == nil) then 
	ngx.log(ngx.ERR, "读取配置文件[" .. configPath .. "]失败");
	return;
end

local configContent = json_util.decode(file:read("*all"));
file:close();
if not configContent then 
	ngx.log(ngx.ERR, "解析配置文件[" .. configPath .. "]中内容失败");
	return;
end

-- 循环将配置文件中内容保存到共享内存中
for name, value in pairs(configContent) do
    config.set(name, value);
    ngx.log(ngx.INFO, "Config key:value is [" .. name .. ":" .. tostring(value) .. "]");
end

ngx.log(ngx.INFO, "Init success....");
