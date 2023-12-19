package org.prgrms.nabimarketbe.global.redisson;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RedisDAO {
    private final RedisTemplate<String, String> redisTemplate;

    public String getValue(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    public List<String> getValuesList(String key) {
        Long len = redisTemplate.opsForList().size(key);
        return len == 0 ? new ArrayList<>() : redisTemplate.opsForList().range(key, 0, len-1);
    }

    public Set<String> getKeySet(String keyPattern) {
        return redisTemplate.keys(keyPattern);
    }

    public void increment(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.increment(key, 1);
    }

    public void setValues(String key, String data) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data);
    }

    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    public void setValuesList(String key, String data) {
        redisTemplate.opsForList().rightPushAll(key, data);
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
}

