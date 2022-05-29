package org.quitian.mutantchecker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Redis configurations.
 */
@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.user}")
    private String redisUser;

    @Value("${spring.redis.pass}")
    private String redisPass;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        System.out.println("---------------------------------------");
        System.out.println("-------------REDIS----------------------");
        System.out.println(redisPort+"-"+redisHost);
        System.out.println("---------------------------------------");
        System.out.println("---------------------------------------");

        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder()
                .connectTimeout(Duration.ofSeconds(10))
                .usePooling()
                .build();

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        config.setPassword(redisPass);
        config.setUsername(redisUser);

        return new JedisConnectionFactory(config,
                jedisClientConfiguration);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    private RedisCacheConfiguration redisCacheConfiguration() {
        RedisSerializationContext.SerializationPair<Object> serializationPair = RedisSerializationContext.SerializationPair.fromSerializer(
                new JdkSerializationRedisSerializer()
        );

        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(serializationPair)
                .disableCachingNullValues();
    }

    private RedisCacheManager getCacheManagerByTtl(Long ttl) {
        RedisCacheConfiguration configuration = this.redisCacheConfiguration().entryTtl(Duration.ofSeconds(ttl));

        return RedisCacheManager.builder(this.jedisConnectionFactory())
                .cacheDefaults(configuration)
                .build();
    }

    @Bean("expireOneDay")
    public CacheManager cacheManagerOneDay() {
        return getCacheManagerByTtl(60l * 60l * 24l);
    }

    @Bean("expire30Secs")
    @Primary
    public CacheManager cacheManager30Secs() {
        return getCacheManagerByTtl(60l);
    }

}