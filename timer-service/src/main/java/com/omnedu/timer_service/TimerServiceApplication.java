package com.omnedu.timer_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TimerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimerServiceApplication.class, args);
	}

}
