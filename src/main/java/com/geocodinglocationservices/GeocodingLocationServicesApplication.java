package com.geocodinglocationservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GeocodingLocationServicesApplication {
	public static void main(String[] args) {
		SpringApplication.run(GeocodingLocationServicesApplication.class, args);
	}

}
