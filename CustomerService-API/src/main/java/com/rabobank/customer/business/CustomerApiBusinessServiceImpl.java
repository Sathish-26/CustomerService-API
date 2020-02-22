package com.rabobank.customer.business;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.rabobank.customer.exceptions.CustomerNotFoundException;
import com.rabobank.customer.exceptions.CustomerServiceApiException;
import com.rabobank.customer.model.Address;
import com.rabobank.customer.model.AddressEntity;
import com.rabobank.customer.model.Customer;
import com.rabobank.customer.model.CustomerEntity;
import com.rabobank.customer.repository.CustomerRepository;

@Service
public class CustomerApiBusinessServiceImpl implements CustomerApiBusinessService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerApiBusinessServiceImpl.class);

	@Autowired
	public CustomerRepository customerRepository;

	@Override
	public Customer addANewCustomer(Customer customer) {
		CustomerEntity customerEntity = translateCustomerToCustomerEntity(customer);
		CustomerEntity custEntity = null;
		try {
			custEntity = customerRepository.save(customerEntity);
		} catch (Exception e) {
			logger.error("Exception during addANewCustomer call", e);
			throw e;
		}
		Customer c = translateCustomerEntityToCustomer(custEntity);
		return c;
	}

	@Override
	public List<Customer> retrieveAllCustomers() {
		List<Customer> customersList = new ArrayList<>();
		List<CustomerEntity> customerEntityList = customerRepository.findAll();
		customerEntityList.forEach(customerEntity -> {
			customersList.add(translateCustomerEntityToCustomer(customerEntity));
		});
		return customersList;
	}

	@Override
	public Customer retrieveCustomerById(long id) {
		CustomerEntity customerEntity = customerRepository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException("retrieveCustomerById",
						"Customer Not found for the given id", HttpStatus.NOT_FOUND));
		return translateCustomerEntityToCustomer(customerEntity);
	}

	@Override
	public List<Customer> retrieveCustomerByFirstNameAndLastName(String firstName, String lastName) {
		List<CustomerEntity> customerEntityList = null;
		List<Customer> customerList = new ArrayList<>();
		try {
			customerEntityList = customerRepository.getByFirstNameAndLastName(firstName, lastName);
			if (!CollectionUtils.isEmpty(customerEntityList)) {
				customerEntityList.forEach(customerEntity -> {
					customerList.add(translateCustomerEntityToCustomer(customerEntity));
				});
			}
		} catch (Exception e) {
			logger.error("Exception during getByFirstNameAndLastName call", e);
			throw e;
		}

		return customerList;
	}

	@Override
	public Customer updateCustomerAddress(long id, Address address) {
		Optional<CustomerEntity> customerEntityOptional = null;
		CustomerEntity customerEntity = null;
		Customer customer = null;
		try {
			customerEntityOptional = customerRepository.findById(id);
			customerEntity = customerEntityOptional.get();
			if (customerEntity != null) {
				customerEntity.setAddress(translateAddressToAddressEntity(address));
				CustomerEntity custEntity = customerRepository.save(customerEntity);
				customer = translateCustomerEntityToCustomer(custEntity);
			}
		} catch (Exception e) {
			logger.error("Exception during findbyId call", e);
			throw e;
		}
		return customer;
	}

	private CustomerEntity translateCustomerToCustomerEntity(Customer customer) {
		CustomerEntity customerEntity = null;
		if (isValidDate(customer.getDateOfBirth())) {
			customerEntity = new CustomerEntity();
			customerEntity.setAddress(translateAddressToAddressEntity(customer.getAddress()));
			customerEntity.setDateOfBirth(customer.getDateOfBirth());
			customerEntity.setFirstName(customer.getFirstName());
			customerEntity.setLastName(customer.getLastName());
		} else {
			throw new CustomerServiceApiException("translateCustomerToCustomerEntity", "Date is invalid",
					HttpStatus.BAD_REQUEST);
		}
		return customerEntity;
	}

	private AddressEntity translateAddressToAddressEntity(Address address) {
		AddressEntity addressEntity = new AddressEntity();
		addressEntity.setAddressLine1(address.getAddressLine1());
		addressEntity.setAddressline2(address.getAddressline2());
		addressEntity.setCountry(address.getCountry());
		addressEntity.setState(address.getState());
		addressEntity.setZipCode(address.getZipCode());
		return addressEntity;
	}

	private boolean isValidDate(Date date) {
		Pattern p = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(date);
		Matcher m = p.matcher(dateStr);
		return m.matches();
	}

	private Customer translateCustomerEntityToCustomer(CustomerEntity customerEntity) {
		Customer customer = null;
		if (customerEntity != null) {
			customer = new Customer();
			customer.setCustomerId(customerEntity.getCustomerId());
			if (isValidDate(customerEntity.getDateOfBirth())) {
				customer.setAge(getAge(customerEntity.getDateOfBirth()));
			}
			customer.setFirstName(customerEntity.getFirstName());
			customer.setLastName(customerEntity.getLastName());
			customer.setAddress(translateAddressEntityToAddress(customerEntity.getAddress()));
		}
		return customer;
	}

	private Address translateAddressEntityToAddress(AddressEntity addressEntity) {
		Address address = null;
		if (addressEntity != null) {
			address = new Address();
			address.setAddressLine1(addressEntity.getAddressLine1());
			address.setAddressline2(addressEntity.getAddressline2());
			address.setState(addressEntity.getState());
			address.setZipCode(addressEntity.getZipCode());
			address.setCountry(addressEntity.getCountry());
		}
		return address;
	}

	private Integer getAge(Date dateOfBirth) {
		Integer age = 0;
		Calendar c = Calendar.getInstance();
		c.setTime(dateOfBirth);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int date = c.get(Calendar.DATE);
		LocalDate l1 = LocalDate.of(year, month, date);
		LocalDate now1 = LocalDate.now();
		Period period = Period.between(l1, now1);
		age = period.getYears();
		return age;
	}
}
