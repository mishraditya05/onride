-- Confirm and consume a driver's matched ride, only if the matchId still matches.
--
-- KEYS[1] matched-ride key for the driver
--
-- ARGV[1] matchId the driver is confirming
--
-- Returns the stored JSON value if it matched (and deletes the key),
-- or false if there was nothing there or the matchId was stale.
--
-- The reason this is a script: a plain GET-then-DELETE from the app would
-- leave a gap for the key to change in between, deleting the wrong match.

local value = redis.call('GET', KEYS[1])
if not value then
    return false
end

local decoded = cjson.decode(value)
if decoded.matchId ~= ARGV[1] then
    return false
end

redis.call('DEL', KEYS[1])
return value