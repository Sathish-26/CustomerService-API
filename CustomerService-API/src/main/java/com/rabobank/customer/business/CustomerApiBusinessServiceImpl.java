package com.rabobank.customer.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.rabobank.customer.exceptions.CustomerNotFoundException;
import com.rabobank.customer.model.Address;
import com.rabobank.customer.model.Customer;
import com.rabobank.customer.repository.CustomerRepository;

@Service
public class CustomerApiBusinessServiceImpl implements CustomerApiBusinessService {

	@Autowired
	public CustomerRepository customerRepository;

	@Override
	public Customer addANewCustomer(Customer customer) {
		Customer c = customerRepository.save(customer);
		return c;
	}

	@Override
	public List<Customer> retrieveAllCustomers() {
		return customerRepository.findAll();
	}

	@Override
	public Customer retrieveCustomerById(long id) {
		return customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("retrieveCustomerById",
				"Customer Not found for the given id", HttpStatus.NOT_FOUND));
	}

	@Override
	public List<Customer> retrieveCustomerByFirstName(String firstName) {
		List<Customer> customersList = customerRepository.getByFirstName(firstName);
		if (CollectionUtils.isEmpty(customersList)) {
			throw new CustomerNotFoundException("retrieveCustomerByFirstName", "Customer Not found for the given Name",
					HttpStatus.NOT_FOUND);
		}
		return customersList;
	}

	@Override
	public List<Customer> retrieveCustomerByFirstNameAndLastName(String firstName, String lastName) {
		List<Customer> customersList = customerRepository.getByFirstNameAndLastName(firstName, lastName);
		if (CollectionUtils.isEmpty(customersList)) {
			throw new CustomerNotFoundException("retrieveCustomerByFirstNameAndLastName",
					"Customer Not found for the given Name", HttpStatus.NOT_FOUND);
		}
		return customersList;
	}

	@Override
	public Customer updateCustomerAddress(long id, Address address) {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException("updateCustomerAddress",
						"Customer Not found for the given id", HttpStatus.NOT_FOUND));
		customer.setAddress(address);
		return customerRepository.save(customer);
	}

}
