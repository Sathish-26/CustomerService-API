package com.rabobank.customer.exceptions;

import org.springframework.http.HttpStatus;

public class CustomerServiceApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String applicationCode;
	private String errorMessage;
	private HttpStatus httpStatus;

	public CustomerServiceApiException(String applicationCode, String errorMessage, HttpStatus httpStatus) {
		super();
		this.applicationCode = applicationCode;
		this.errorMessage = errorMessage;
		this.httpStatus = httpStatus;
	}

	public String getApplicationCode() {
		return applicationCode;
	}

	public void setApplicationCode(String applicationCode) {
		this.applicationCode = applicationCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

}
