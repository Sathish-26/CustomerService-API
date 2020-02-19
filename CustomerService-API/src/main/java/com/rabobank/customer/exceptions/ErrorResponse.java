package com.rabobank.customer.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

	private List<ErrorDetail> errorDetails;

	public ErrorResponse() {

	}

	public ErrorResponse(HttpStatus httpStatus, String applicationCode, String errorMessage) {
		ErrorDetail errorDetail = new ErrorDetail(httpStatus, applicationCode, errorMessage);
		this.addError(errorDetail);
	}

	public List<ErrorDetail> getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(List<ErrorDetail> errorDetails) {
		this.errorDetails = errorDetails;
	}

	public void addError(ErrorDetail errorDetail) {
		if (this.errorDetails == null) {
			this.errorDetails = new ArrayList<>();
		}
		this.errorDetails.add(errorDetail);
	}

}
