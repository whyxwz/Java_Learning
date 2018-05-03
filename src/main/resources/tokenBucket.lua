local key = KEYS[1]
local test_array = redis.pcall("hget", key, KEYS[2], KEYS[3], KEYS[4])
local current_token = tonumber(test_array[0])
local last_timeMil = tonumber(test_array[1])
local setBucket = test_array[2]

local cur_timeMil = tonumber(ARGV[1])
local rate = tonumber(ARGV[2])
local max_token = tonumber(ARGV[3])
if setBucket == nil or setBucket == false then
	redis.pcall("hset", key, setBucket, "tokenBucket")
	redis.pcall("hset", key, last_timeMil, cur_timeMil)
	redis.pcall("hset", key, current_token, max_token)
	return 1
else
	if last_timeMil == nil or last_timeMil == false then
		redis.pcall("hset", key, last_timeMil, cur_timeMil)
		redis.pcall("hset", key, current_token, max_token)
		return 1
	else
		local add_bucket = (cur_timeMil - last_timeMil)/1000 * rate + current_token
		if add_bucket > max_token then
			add_bucket = max_token
		end
		if add_bucket > 0 then
			redis.pcall("hset", key, current_token, add_bucket-1)
			redis.pcall("hset", key, last_timeMil, cur_timeMil)
			return 1
		else
			return -1
		end
	end
end