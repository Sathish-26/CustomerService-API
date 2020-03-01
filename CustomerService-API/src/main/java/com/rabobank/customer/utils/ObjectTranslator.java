package com.rabobank.customer.utils;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.rabobank.customer.exceptions.CustomerServiceApiException;
import com.rabobank.customer.model.Address;
import com.rabobank.customer.model.AddressEntity;
import com.rabobank.customer.model.Customer;
import com.rabobank.customer.model.CustomerEntity;

public class ObjectTranslator {

	private static final Logger logger = LoggerFactory.getLogger(ObjectTranslator.class);

	public static Function<CustomerEntity, Customer> translateCustomerEntityFn() {

		Function<CustomerEntity, Customer> translateCustEntityFn = new Function<CustomerEntity, Customer>() {
			@Override
			public Customer apply(CustomerEntity t) {
				Customer customer = null;
				if (t != null) {
					Integer age = null;
					if (t.getDateOfBirth() != null && CustomerServiceApiUtils.isValidDate(t.getDateOfBirth())) {
						age = CustomerServiceApiUtils.getAge(t.getDateOfBirth());
					}
					customer = new Customer(t.getId(), t.getCustomerId(), t.getFirstName(), t.getLastName(), age, null,
							translateAddressEntityFn().apply(t.getAddress()));
				}
				logger.info("Customer: {}", customer);
				return customer;
			}
		};
		return translateCustEntityFn;
	}

	public static Function<Customer, CustomerEntity> translateCustomerFn() {
		Function<Customer, CustomerEntity> translateCustomerFn = new Function<Customer, CustomerEntity>() {

			@Override
			public CustomerEntity apply(Customer t) {
				CustomerEntity customerEntity = null;
				if (CustomerServiceApiUtils.isValidDate(t.getDateOfBirth())) {
					customerEntity = new CustomerEntity(t.getId(), t.getCustomerId(), t.getFirstName(), t.getLastName(),
							t.getDateOfBirth(), t.getAge(), translateAddressFn().apply(t.getAddress()));
				} else {
					throw new CustomerServiceApiException("Invalid date of birth", "Invalid date of birth",
							HttpStatus.BAD_REQUEST);
				}
				logger.info("customerEntity: {}", customerEntity);
				return customerEntity;
			}
		};
		return translateCustomerFn;
	}

	public static Function<AddressEntity, Address> translateAddressEntityFn() {
		Function<AddressEntity, Address> translateAddressEntityFn = new Function<AddressEntity, Address>() {

			@Override
			public Address apply(AddressEntity t) {
				Address address = new Address(t.getAddressLine1(), t.getAddressline2(), t.getState(), t.getZipCode(),
						t.getCountry());
				logger.info("address: {}", address);
				return address;
			}
		};
		return translateAddressEntityFn;
	}

	public static Function<Address, AddressEntity> translateAddressFn() {
		Function<Address, AddressEntity> translateAddressFn = new Function<Address, AddressEntity>() {

			@Override
			public AddressEntity apply(Address t) {
				AddressEntity addressEntity = new AddressEntity(0, t.getAddressLine1(), t.getAddressline2(),
						t.getState(), t.getZipCode(), t.getCountry());
				logger.info("addressEntity: {}", addressEntity);
				return addressEntity;
			}
		};
		return translateAddressFn;
	}
}
