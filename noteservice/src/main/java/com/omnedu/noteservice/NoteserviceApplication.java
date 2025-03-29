package com.omnedu.noteservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NoteserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoteserviceApplication.class, args);
	}

}
