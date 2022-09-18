package com.pranshu.crypto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class CryptoPriceTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoPriceTrackerApplication.class, args);
	}

	@PostConstruct
	public void init(){
		// Setting default time zone for the application
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

}
