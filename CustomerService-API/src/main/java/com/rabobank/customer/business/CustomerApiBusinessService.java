package com.rabobank.customer.business;

import java.util.List;

import com.rabobank.customer.model.Address;
import com.rabobank.customer.model.Customer;

public interface CustomerApiBusinessService {

	Customer addANewCustomer(Customer customer);

	List<Customer> retrieveAllCustomers();

	Customer retrieveCustomerById(long id);

	List<Customer> retrieveCustomerByFirstNameAndLastName(String firstName, String lastName);

	Customer updateCustomerAddress(long id, Address address);
}
