package com.edu.kt.gw.simple;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
//@EnableR2dbcRepositories
@EnableCaching
@SpringBootApplication
@ComponentScan("com.edu.kt")
public class EduGwSimpleApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduGwSimpleApplication.class, args);
	}
}
