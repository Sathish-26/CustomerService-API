package com.rabobank.customer.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.rabobank.customer.exceptions.CustomerNotFoundException;
import com.rabobank.customer.model.Address;
import com.rabobank.customer.model.Customer;
import com.rabobank.customer.model.CustomerEntity;
import com.rabobank.customer.repository.CustomerRepository;
import com.rabobank.customer.utils.ObjectTranslator;

@Service
public class CustomerApiBusinessServiceImpl implements CustomerApiBusinessService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerApiBusinessServiceImpl.class);

	@Autowired
	public CustomerRepository customerRepository;

	/*
	 * Adds the new customer in the system
	 * 
	 * @param - Customer
	 * 
	 * @returns - New customer details
	 * 
	 */
	@Override
	public Customer addANewCustomer(Customer customer) {
		logger.info("Started the execution of addANewCustomer with customer details - {}", customer);
		CustomerEntity customerEntity = ObjectTranslator.translateCustomerFn().apply(customer);
		CustomerEntity custEntity = customerRepository.save(customerEntity);

		Customer c = ObjectTranslator.translateCustomerEntityFn().apply(custEntity);
		logger.info("completed the execution of addANewCustomer");
		return c;
	}

	/*
	 * Retrieves all customer details from the system
	 * 
	 * @returns - List<Customer>
	 * 
	 */
	@Override
	public List<Customer> retrieveAllCustomers() {
		logger.info("Started the execution of retrieveAllCustomers");
		List<Customer> customersList = new ArrayList<>();
		List<CustomerEntity> customerEntityList = customerRepository.findAll();
		customersList = customerEntityList.stream().map(ObjectTranslator.translateCustomerEntityFn())
				.collect(Collectors.toList());
		logger.info("Completed the execution of retrieveAllCustomers");
		return customersList;
	}

	/*
	 * Retrieves customer details based on Id
	 * 
	 * @Param - id of the customer
	 * 
	 * @returns - Customer
	 * 
	 */
	@Override
	public Customer retrieveCustomerById(long id) {
		logger.info("Started the execution of retrieveCustomerById");
		CustomerEntity customerEntity = customerRepository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException("retrieveCustomerById",
						"Customer Not found for the given id", HttpStatus.NOT_FOUND));
		logger.info("Completed the execution of retrieveCustomerById");
		return ObjectTranslator.translateCustomerEntityFn().apply(customerEntity);
	}

	/*
	 * Retrieves all customer details based on first/last name.
	 * 
	 * @returns - List<Customer>
	 * 
	 */
	@Override
	public List<Customer> retrieveCustomerByFirstNameAndLastName(String firstName, String lastName) {
		logger.info("Started the execution of retrieveCustomerByFirstNameAndLastName");
		List<CustomerEntity> customerEntityList = null;
		List<Customer> customerList = new ArrayList<>();
		try {
			customerEntityList = customerRepository.getByFirstNameAndLastName(firstName, lastName);
			if (!CollectionUtils.isEmpty(customerEntityList)) {
				customerList = customerEntityList.stream().map(ObjectTranslator.translateCustomerEntityFn())
						.collect(Collectors.toList());
			}
		} catch (Exception e) {
			logger.error("Exception during getByFirstNameAndLastName call", e);
			throw e;
		}
		logger.info("Completed the execution of retrieveCustomerByFirstNameAndLastName");
		return customerList;
	}

	/*
	 * Updates the address of the customer
	 * 
	 * @Params id - id of the customer, Address to be updated
	 * 
	 * @returns - Customer
	 * 
	 */
	@Override
	public Customer updateCustomerAddress(long id, Address address) {
		logger.info("Started the execution of updateCustomerAddress with id- {}", id);
		Optional<CustomerEntity> customerEntityOptional = null;
		CustomerEntity customerEntity = null;
		Customer customer = null;
		try {
			customerEntityOptional = customerRepository.findById(id);
			customerEntity = customerEntityOptional.get();
			customer = ObjectTranslator.translateCustomerEntityFn().apply(customerEntity);
		} catch (Exception e) {
			logger.error("Exception during updateCustomerAddress call", e);
			throw e;
		}
		logger.info("Completed the execution of updateCustomerAddress");
		return customer;
	}

}
