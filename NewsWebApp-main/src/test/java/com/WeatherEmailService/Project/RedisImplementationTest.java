package com.WeatherEmailService.Project;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
@Slf4j
public class RedisImplementationTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void restTemplate(){
        redisTemplate.opsForValue().set("email","service@gmail.com");
        log.info(".......");
        log.info((String)redisTemplate.opsForValue().get("mail"));
    }
}
