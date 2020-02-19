package com.rabobank.customer.exceptions;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;

public class ErrorDetail {

	private LocalDateTime dateTime;
	private String id;
	private String code;
	private int status;
	private String errorMessage;

	public ErrorDetail(LocalDateTime dateTime, String id, String code, int status, String errorMessage) {
		this.dateTime = dateTime;
		this.id = id;
		this.code = code;
		this.status = status;
		this.errorMessage = errorMessage;
	}

	public ErrorDetail(HttpStatus httpStatus, String applicationCode, String errorMessage) {
		this(LocalDateTime.now(), UUID.randomUUID().toString(), applicationCode, httpStatus.value(), errorMessage);
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
