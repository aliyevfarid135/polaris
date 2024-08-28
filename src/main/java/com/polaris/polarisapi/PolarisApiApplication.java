package com.polaris.polarisapi;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.polaris")
@EnableJpaRepositories(basePackages = "com.polaris.repository")
@EntityScan(basePackages = "com.polaris.entity")
//@ComponentScan(basePackages = {"com.blogapp", "com.blogapp.security", "com.blogapp.service.impl", "com.blogapp.service.inter", "com.blogapp.payload"})
@RequiredArgsConstructor
public class PolarisApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PolarisApiApplication.class, args);
	}
}
