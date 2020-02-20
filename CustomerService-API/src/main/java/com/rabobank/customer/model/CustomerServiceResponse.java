package com.rabobank.customer.model;

import java.util.List;

public class CustomerServiceResponse {

	private List<Customer> customersList;

	public CustomerServiceResponse() {

	}

	public CustomerServiceResponse(List<Customer> customersList) {
		super();
		this.customersList = customersList;
	}

	public List<Customer> getCustomersList() {
		return customersList;
	}

	public void setCustomersList(List<Customer> customersList) {
		this.customersList = customersList;
	}
}
