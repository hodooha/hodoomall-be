package com.hodoo.hodoomall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class HodoomallApplication {

	public static void main(String[] args) {
		SpringApplication.run(HodoomallApplication.class, args);
	}

}
