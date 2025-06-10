package net.engineeringdigest.journalApp.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    public <T> T get(String key, Class<T> entityClass){
        try {
            Object o = redisTemplate.opsForValue().get(key);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(o.toString(), entityClass);
        } catch (Exception e) {
            log.error("Exception in getting cache from redis", e);
            return null;
        }
    }
    public <T> T get(String key, TypeReference<T> typeReference) {
        try {
            Object o = redisTemplate.opsForValue().get(key);
            ObjectMapper mapper = new ObjectMapper();
            return o != null ? mapper.readValue(o.toString(), typeReference) : null;
        } catch (Exception e) {
            log.error("Failed to get data from Redis", e);
            return null;
        }
    }

    public void set(String key, Object o, Long ttl){
        try{
            ObjectMapper mapper = new ObjectMapper();
            String jsonData = mapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key, jsonData, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Exception in setting cache to redis", e);
        }
    }
}
