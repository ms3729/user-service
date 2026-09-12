package com.rata.userService;

import com.rata.userService.config.AuditorAwareImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.TimeZone;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableJpaRepositories(basePackages = "com.rata.userService.repositories.mysql")
@EnableMongoRepositories(basePackages = "com.rata.userService.repositories.mongodb")
@PropertySource("classpath:message_**.properties")
public class UserServiceApplication {


	@Bean
	AuditorAware<String> auditorProvider() {
		return new AuditorAwareImpl();
	}

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tehran"));
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
