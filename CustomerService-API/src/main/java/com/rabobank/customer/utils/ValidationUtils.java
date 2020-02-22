package com.rabobank.customer.utils;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.rabobank.customer.exceptions.CustomerServiceApiException;

public class ValidationUtils {

	private ValidationUtils() {

	}

	public static void validateInput(String textToValidate) {
		String[] maliciousDataSamples = { "select", "insert into ", "update", "delete from", "upsert", "call",
				"rollback ", "create table", "drop table", "drop view", "alter table", "truncate table", "desc" };
		List<String> dataSamples = Arrays.asList(maliciousDataSamples);
		boolean isInValidInput = dataSamples.stream().anyMatch(x -> textToValidate.contains(x));
		if (isInValidInput) {
			throw new CustomerServiceApiException("Input Validation failed", "Input contains malicious characters",
					HttpStatus.BAD_REQUEST);
		}
	}
}
