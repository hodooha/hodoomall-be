package com.hodoo.hodoomall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableMongoAuditing
@EnableScheduling
@EnableRetry
@SpringBootApplication
public class HodoomallApplication {

	public static void main(String[] args) {
		SpringApplication.run(HodoomallApplication.class, args);
	}

}
