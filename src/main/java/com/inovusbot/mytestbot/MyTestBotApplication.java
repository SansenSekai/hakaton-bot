package com.inovusbot.mytestbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MyTestBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyTestBotApplication.class, args);
	}

}
