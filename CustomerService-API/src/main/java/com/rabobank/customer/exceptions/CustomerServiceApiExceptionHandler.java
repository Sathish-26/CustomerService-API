package com.rabobank.customer.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.rabobank.customer.model.ErrorResponse;

@RestControllerAdvice
public class CustomerServiceApiExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceApiExceptionHandler.class);

	@ExceptionHandler(CustomerNotFoundException.class)
	protected ResponseEntity<Object> handleCustomerNotFoundException(
			CustomerNotFoundException customerNotFoundException) {
		logger.info("Handling handleCustomerNotFoundException");
		return handleException(customerNotFoundException);
	}

	@ExceptionHandler(CustomerServiceApiException.class)
	protected ResponseEntity<Object> handleGenericExceptions(CustomerServiceApiException exception) {
		logger.info("Handling handleGenericExceptions");
		ErrorResponse errorResponse = new ErrorResponse(exception.getHttpStatus(), exception.getApplicationCode(),
				exception.getMessage());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<Object>(errorResponse, headers, exception.getHttpStatus());

	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.info("Entered handleMethodArgumentNotValid");
		List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();
		ErrorResponse errorResponse = new ErrorResponse(status, "addNewCustomer", ex.getLocalizedMessage(),
				objectErrors);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		logger.info("After setting up the error response");
		return new ResponseEntity<Object>(errorResponse, responseHeaders, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintValidationException(ConstraintViolationException ex) {
		logger.info("Entered handleMethodArgumentNotValid");
		Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
		List<ObjectError> objectErrors = new ArrayList<ObjectError>();

		constraintViolations.forEach(constraint -> {
			ObjectError objectError = new ObjectError(constraint.getInvalidValue().toString(), constraint.getMessage());
			objectErrors.add(objectError);
		});

		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "retrievebyName",
				ex.getLocalizedMessage(), objectErrors);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		logger.info("After setting up the error response");
		return new ResponseEntity<Object>(errorResponse, responseHeaders, HttpStatus.BAD_REQUEST);
	}

	public static ResponseEntity<Object> handleException(CustomerNotFoundException customerNotFoundException) {
		logger.info("Entered handleException with applicationCode: {} and message: {}",
				customerNotFoundException.getApplicationCode(), customerNotFoundException.getMessage());
		HttpStatus httpStatus = customerNotFoundException.getHttpStatus();
		String applicationCode = customerNotFoundException.getApplicationCode();
		String message = customerNotFoundException.getMessage();

		ErrorResponse errorResponse = new ErrorResponse(httpStatus, applicationCode, message);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		logger.info("After setting up the error response");
		return new ResponseEntity<Object>(errorResponse, headers, customerNotFoundException.getHttpStatus());

	}
}
