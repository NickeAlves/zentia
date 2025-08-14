package com.api.zentia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaRepositories("com.api.zentia.repository")
@EntityScan("com.api.zentia.entity")
@EnableAsync
public class ZentiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZentiaApplication.class, args);
	}

}
