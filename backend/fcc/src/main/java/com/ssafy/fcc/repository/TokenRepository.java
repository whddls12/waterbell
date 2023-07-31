package com.ssafy.fcc.repository;

import com.ssafy.fcc.dto.RefreshToken;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class TokenRepository {

    private RedisTemplate redisTemplate;

    public TokenRepository(final RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public void save( RefreshToken refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(String.valueOf(refreshToken.getId()),refreshToken.getRefreshToken());
        redisTemplate.expire(String.valueOf(refreshToken.getId()), 60L* 60 * 24 * 14, TimeUnit.SECONDS);
    }

    public Optional<String> findById(String id) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String token = valueOperations.get(id);

        if (token == null) {
            return Optional.empty();
        }

        return Optional.of(token);
    }

    public void deleteById(String id) {
        redisTemplate.delete(id);
    }
    public void saveBlackList( String accessToken, Long expiration) {
        redisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
    }


}