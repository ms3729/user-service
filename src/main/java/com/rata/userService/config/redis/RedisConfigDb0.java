package com.rata.userService.config.redis;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
@EnableRedisRepositories(basePackages = "com.rata.userService.repositories.redis.db0", keyValueTemplateRef = "redisKeyValueTemplateDb0")
public class RedisConfigDb0 {

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
    public RedisConnectionFactory redisConnectionFactoryDb0() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        config.setPassword(password);
        config.setDatabase(0);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisKeyValueAdapter redisKeyValueAdapterDb0(
            @Qualifier("redisTemplate") RedisTemplate<?, ?> redisTemplate) {
        return new RedisKeyValueAdapter(redisTemplate);
    }

    @Bean("redisTemplate")
    @Primary
    public RedisTemplate<String, Object> redisTemplateDb0(
            @Qualifier("redisConnectionFactoryDb0") RedisConnectionFactory factory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.afterPropertiesSet();
        return template;
    }

    @Bean("redisKeyValueTemplateDb0")
    public RedisKeyValueTemplate redisKeyValueTemplateDb0(
            @Qualifier("redisTemplate") RedisTemplate<?, ?> template) {
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

