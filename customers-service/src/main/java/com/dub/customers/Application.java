package com.dub.customers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.web.bind.annotation.RestController;


@RestController
@SpringBootApplication
public class Application implements CommandLineRunner {
	
	@Value("${spring.datasource.username}")
	String username;
	

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	

	private static final Logger log = LoggerFactory.getLogger(Application.class);


	@Override
	public void run(String... arg0) throws Exception {
		log.info("Application begin " + username);
	}
	
	
}
