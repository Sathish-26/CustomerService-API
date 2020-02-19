package com.rabobank.customer.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rabobank.customer.business.CustomerApiBusinessService;
import com.rabobank.customer.exceptions.CustomerNotFoundException;
import com.rabobank.customer.exceptions.CustomerServiceApiException;
import com.rabobank.customer.exceptions.ExceptionConstants;
import com.rabobank.customer.model.Address;
import com.rabobank.customer.model.Customer;
import com.rabobank.customer.utils.ValidationUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "CustomerService")
@RequestMapping("/${api.version}")
public class CustomerServiceApiController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceApiController.class);

	@Autowired
	private CustomerApiBusinessService customerApibusinessService;

	@ApiOperation(value = "Add a new customer", response = Customer.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully added the customer"),
			@ApiResponse(code = 401, message = "You are not authorized to add a customer"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/customers", method = RequestMethod.POST, consumes = "application/json")
	public Customer addANewCustomer(@Valid @RequestBody Customer customer) {
		logger.info("Entered addANewCustomer with request - {}", customer);
		Customer newCustomerDetails = null;
		try {
			newCustomerDetails = customerApibusinessService.addANewCustomer(customer);
		} catch (Exception e) {
			logger.error(ExceptionConstants.ADD_NEW_CUSTOMER_EXCEPTION_MSG, e);
			throw new CustomerServiceApiException("addANewCustomer", ExceptionConstants.ADD_NEW_CUSTOMER_EXCEPTION_MSG,
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
		logger.info("COmpleted adding the customer");
		return newCustomerDetails;
	}

	@ApiOperation(value = "Retrieves list of all available customers", response = Customer.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved the customers list"),
			@ApiResponse(code = 401, message = "You are not authorized to retrieve all customer"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/customers", method = RequestMethod.GET, produces = "application/json")
	public List<Customer> retrieveAllCustomers() {
		logger.info("Entered retrieveAllCustomers");
		List<Customer> customersList = null;
		try {
			customersList = customerApibusinessService.retrieveAllCustomers();
		} catch (Exception e) {
			logger.error(ExceptionConstants.RETRIEVE_ALL_CUSTOMERS_EXCEPTION_MSG, e);
			throw new CustomerServiceApiException("retrieveAllCustomers",
					ExceptionConstants.RETRIEVE_ALL_CUSTOMERS_EXCEPTION_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		logger.info("Completed retrieveAllCustomers");
		return customersList;
	}

	@ApiOperation(value = "Retrieves list of all available customers based on first name or last name", response = Customer.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved the customers list"),
			@ApiResponse(code = 401, message = "You are not authorized to retrieve all customer"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/customers/name", method = RequestMethod.GET, produces = "application/json")
	public List<Customer> retrieveCustomersByName(@RequestParam(value = "firstName", required = true) String firstName,
			@RequestParam(value = "lastName", required = false) String lastName) {
		logger.info("Entered retrieveCustomersByName with firstname {} and lastName: {}", firstName, lastName);
		List<Customer> customersList = null;
		if (StringUtils.isNotBlank(firstName) && StringUtils.isBlank(lastName)) {
			ValidationUtils.validateInput(firstName);
			try {
				customersList = customerApibusinessService.retrieveCustomerByFirstName(firstName);
			} catch (Exception e) {
				logger.error(ExceptionConstants.RETRIEVE_CUSTOMER_BY_FIRST_NAME_EXCEPTION_MSG, e);
				throw new CustomerServiceApiException("retrieveCustomersByName",
						ExceptionConstants.RETRIEVE_CUSTOMER_BY_FIRST_NAME_EXCEPTION_MSG,
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)) {
			ValidationUtils.validateInput(firstName);
			ValidationUtils.validateInput(lastName);
			try {
				customersList = customerApibusinessService.retrieveCustomerByFirstNameAndLastName(firstName, lastName);
			} catch (Exception e) {
				logger.error(ExceptionConstants.RETRIEVE_CUSTOMER_BY_NAME_EXCEPTION_MSG, e);
				throw new CustomerServiceApiException("retrieveCustomersByName",
						ExceptionConstants.RETRIEVE_CUSTOMER_BY_NAME_EXCEPTION_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		logger.info("Completed retrieveCustomersByName");
		return customersList;
	}

	@ApiOperation(value = "Retrieves a customer by Id", response = Customer.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved the customer"),
			@ApiResponse(code = 404, message = "Customer Not found"),
			@ApiResponse(code = 401, message = "You are not authorized to retrieve a customer"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/customers/{id}", method = RequestMethod.GET, produces = "application/json")
	public Customer retrieveCustomerById(@PathVariable long id) {
		logger.info("Entered retrieveCustomerById with id {}", id);
		Customer customer = null;
		try {
			customer = customerApibusinessService.retrieveCustomerById(id);
		} catch (Exception e) {
			logger.error(ExceptionConstants.RETRIEVE_CUSTOMER_BY_ID_EXCEPTION_MSG, e);
			if (e instanceof CustomerNotFoundException) {
				throw e;
			}
			throw new CustomerServiceApiException("retrieveCustomerById",
					ExceptionConstants.RETRIEVE_CUSTOMER_BY_ID_EXCEPTION_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		logger.info("Completed retrieveCustomerById");
		return customer;
	}

	@ApiOperation(value = "Updates the adress of the customer", response = Customer.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated the address the customer"),
			@ApiResponse(code = 401, message = "You are not authorized to update the address of the customer"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/customers/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	public Customer updateCustomerAddress(@PathVariable long id, @Valid @RequestBody Address address) {
		logger.info("Entered updateCustomerAddress with id {} and address {}", id, address);
		Customer customer = null;
		try {
			customer = customerApibusinessService.updateCustomerAddress(id, address);
		} catch (Exception e) {
			logger.error(ExceptionConstants.UPDATE_CUSTOMER_ADDRESS_EXCEPTION_MSG, e);
			throw new CustomerServiceApiException("updateCustomerAddress",
					ExceptionConstants.UPDATE_CUSTOMER_ADDRESS_EXCEPTION_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		logger.info("completed updateCustomerAddress");
		return customer;
	}
}
