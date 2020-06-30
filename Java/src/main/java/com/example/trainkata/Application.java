package com.example.trainkata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(com.example.trainkata.Application.class, args);
	}

	@RequestMapping("/hello")
	public String hello(){
		return "hello train !";
	}
}
