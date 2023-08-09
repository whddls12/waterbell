package com.ssafy.fcc.repository;

import com.ssafy.fcc.dto.RefreshToken;
import com.ssafy.fcc.dto.SocialTempDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import com.google.gson.Gson;

@Repository
public class SocialRedisRepository {
    private RedisTemplate redisTemplate;
    public SocialRedisRepository(final RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    private Gson gson = new Gson();


    public void save( SocialTempDto m) {
//        ValueOperations<String, Object > valueOperations = redisTemplate.opsForValue();
//        valueOperations.set(String.valueOf(m.getEmail()), m);
//        redisTemplate.expire(String.valueOf(m.getEmail()), 60L* 60, TimeUnit.SECONDS);//1시간 유지

        try {
            // Redis에 객체를 저장하기 위해 ObjectMapper를 사용하여 객체를 JSON 문자열로 직렬화합니다.
            String jsonValue = gson.toJson(m);
            System.out.println(jsonValue);
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(String.valueOf(m.getEmail()), jsonValue);
            redisTemplate.expire(String.valueOf(m.getEmail()), 60L * 60, TimeUnit.SECONDS); // 1시간 유지 // 1시간 유지
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Optional<SocialTempDto> findByEmail(String email) {
        ValueOperations<String, SocialTempDto> valueOperations = redisTemplate.opsForValue();
        SocialTempDto m = valueOperations.get(email);

        System.out.println(m);
        if (m == null) {
            return Optional.empty();
        }

        return Optional.of(m);
    }

    public SocialTempDto get(String email) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String jsonValue = valueOperations.get(email);

        try {
            // JSON 문자열을 객체로 역직렬화합니다.
            return gson.fromJson(jsonValue, SocialTempDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public SocialTempDto getDtoByEmail(String key){
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(SocialTempDto.class));
        ValueOperations<String, SocialTempDto> redis = redisTemplate.opsForValue();
        SocialTempDto data = redis.get(key);
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        return data;
    }
    public void deleteById(String email) {
        redisTemplate.delete(email);
    }

}
