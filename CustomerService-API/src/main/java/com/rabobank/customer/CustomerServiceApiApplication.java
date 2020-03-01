package com.rabobank.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.rabobank"})
public class CustomerServiceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApiApplication.class, args);
	}

}
