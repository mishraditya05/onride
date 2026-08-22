-- Record a driver's position and keep the cell index exact.
--
-- KEYS[1] driver position hash
-- KEYS[2] zset for the cell the driver is in now
--
-- ARGV[1] lat
-- ARGV[2] lng
-- ARGV[3] cell id
-- ARGV[4] driver id
-- ARGV[5] ping time, epoch millis (passed in: scripts must be deterministic)
-- ARGV[6] position ttl, seconds
-- ARGV[7] cell key prefix
--
-- Returns 1 if the driver changed cell, 0 if they stayed put.

local previousCell = redis.call('HGET', KEYS[1], 'cell')
local changedCell = 0

-- The reason this is a script: the eviction target is only known after the
-- read, and a pipeline cannot branch on a reply it has not received yet.
if previousCell and previousCell ~= ARGV[3] then
    redis.call('ZREM', ARGV[7] .. previousCell, ARGV[4])
    changedCell = 1
end

redis.call('HSET', KEYS[1],
    'lat', ARGV[1],
    'lng', ARGV[2],
    'cell', ARGV[3],
    'ts', ARGV[5])

redis.call('EXPIRE', KEYS[1], ARGV[6])

-- Score is the ping time, which turns "who is fresh" into a range query.
redis.call('ZADD', KEYS[2], ARGV[5], ARGV[4])

return changedCell