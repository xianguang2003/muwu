local _M = {};

_M.PLATFORM_AUTH_URL_PRE = "platform_auth_url_pre";

_M.VERIFY_TOKEN_MODE = "verify_token_mode";

_M.VERIFY_TOKEN_MODE_JWT = "jwt";
_M.VERIFY_TOKEN_MODE_INTERFACE = "interface";

_M.JWT_SECRET = "jwt_secret";


_M.ROLE_KEY = "muwu:role";
_M.PERMISSION_WHITELIST_URLS_KEY = "muwu:permission:whitelist:urls";

_M.CONFIG_PATH = "openResty/config.json";

-- begin，配置文件config.json中对应的配置项
_M.REDIS_IP = "redis_ip";
_M.REDIS_PORT = "redis_port";
_M.REDIS_PASSWORD = "redis_password";
_M.REDIS_SELECT = "redis_db_index";
_M.REDIS_TIMEOUT = "redis_timeout";
_M.REDIS_MAX_IDLE_TIME = "redis_pool_max_idle_time";
_M.REDIS_POOL_SIZE = "redis_pool_size";

_M.OAUTH_DOMAIN = "oauth_domain";
_M.CHECK_TOKEN = "check_token";
_M.GET_USER_INFO = "get_user_info";
-- end，配置文件config.json中对应的配置项

return _M;

