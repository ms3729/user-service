package com.rata.userService.config.redis;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories(basePackages = "com.rata.userService.repositories.redis.db1", keyValueTemplateRef = "redisKeyValueTemplateDb1")
public class RedisConfigDb1 {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password}")
    private String password;

    @Getter
    @Value("${spring.data.redis.ttl}")
    private Long ttl;

    @Bean
    public RedisConnectionFactory redisConnectionFactoryDb1() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        config.setPassword(password);
        config.setDatabase(1);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisKeyValueAdapter redisKeyValueAdapterDb1(
            @Qualifier("redisTemplateDb1") RedisTemplate<?, ?> redisTemplate) {
        return new RedisKeyValueAdapter(redisTemplate);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplateDb1(
            @Qualifier("redisConnectionFactoryDb1") RedisConnectionFactory factory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.afterPropertiesSet();
        return template;
    }

    @Bean("redisKeyValueTemplateDb1")
    public RedisKeyValueTemplate redisKeyValueTemplateDb1(
            @Qualifier("redisTemplateDb1") RedisTemplate<?, ?> template) {
        RedisMappingContext mappingContext = new RedisMappingContext();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new JdkSerializationRedisSerializer());

        return new RedisKeyValueTemplate(
                new RedisKeyValueAdapter(template),
                mappingContext);
    }
}