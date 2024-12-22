package com.it.naturlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.it.naturlink.repository")
@EntityScan(basePackages = "com.it.naturlink.db")

public class NaturnlinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(NaturnlinkApplication.class, args);
	}

}
