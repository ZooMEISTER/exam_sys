package com.zoom.exam_sys_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/1/22 19:56
 **/

@Configuration
public class RedisConfig {

    /**
    * @Author: ZooMEISTER
    * @Description: 该类主要用于配置key-value序列化和反序列化。
    * @DateTime: 2024/1/22 19:57
    * @Params: [lettuceConnectionFactory]
    * @Return org.springframework.data.redis.core.RedisTemplate<java.lang.String,java.lang.Object>
    */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        // 设置key序列化方式string，RedisSerializer.string() 等价于 new StringRedisSerializer()
        redisTemplate.setKeySerializer(RedisSerializer.string());
        // 设置value的序列化方式json，使用GenericJackson2JsonRedisSerializer替换默认序列化，RedisSerializer.json() 等价于 new GenericJackson2JsonRedisSerializer()
        redisTemplate.setValueSerializer(RedisSerializer.json());
        // 设置hash的key的序列化方式
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        // 设置hash的value的序列化方式
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        // 使配置生效
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}