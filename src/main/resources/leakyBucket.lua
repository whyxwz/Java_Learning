local key = KEYS[1]
local arg = ARGV[1]
if redis.call('exists', key) == false then
    redis.call('lpush', key, arg)
    redis.call('expire', key, 1)
else
    if redis.call('llen', 'key') > 50 then
        return false;
    else
        redis.call('lpush', key, arg);
        return true;
    end
end