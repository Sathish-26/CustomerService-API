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
		logger.info("Started the execution of addANewCustomer with customer details - {}", customer);
		CustomerEntity customerEntity = translateCustomerToCustomerEntity(customer);
		CustomerEntity custEntity = customerRepository.save(customerEntity);

		Customer c = translateCustomerEntityToCustomer(custEntity);
		logger.info("completed the execution of addANewCustomer");
		return c;
	}

	@Override
	public List<Customer> retrieveAllCustomers() {
		logger.info("Started the execution of retrieveAllCustomers");
		List<Customer> customersList = new ArrayList<>();
		List<CustomerEntity> customerEntityList = customerRepository.findAll();
		customerEntityList.forEach(customerEntity -> {
			customersList.add(translateCustomerEntityToCustomer(customerEntity));
		});
		logger.info("Completed the execution of retrieveAllCustomers");
		return customersList;
	}

	@Override
	public Customer retrieveCustomerById(long id) {
		logger.info("Started the execution of retrieveCustomerById");
		CustomerEntity customerEntity = customerRepository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException("retrieveCustomerById",
						"Customer Not found for the given id", HttpStatus.NOT_FOUND));
		logger.info("Completed the execution of retrieveCustomerById");
		return translateCustomerEntityToCustomer(customerEntity);
	}

	@Override
	public List<Customer> retrieveCustomerByFirstNameAndLastName(String firstName, String lastName) {
		logger.info("Started the execution of retrieveCustomerByFirstNameAndLastName");
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
		logger.info("Completed the execution of retrieveCustomerByFirstNameAndLastName");
		return customerList;
	}

	@Override
	public Customer updateCustomerAddress(long id, Address address) {
		logger.info("Started the execution of updateCustomerAddress with id- {}", id);
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
			logger.error("Exception during updateCustomerAddress call", e);
			throw e;
		}
		logger.info("Completed the execution of updateCustomerAddress");
		return customer;
	}

	private CustomerEntity translateCustomerToCustomerEntity(Customer customer) {
		logger.info("Started execution of translateCustomerToCustomerEntity");
		CustomerEntity customerEntity = null;
		if (isValidDate(customer.getDateOfBirth())) {
			logger.debug("Valid date of birth");
			customerEntity = new CustomerEntity();
			customerEntity.setAddress(translateAddressToAddressEntity(customer.getAddress()));
			customerEntity.setDateOfBirth(customer.getDateOfBirth());
			customerEntity.setFirstName(customer.getFirstName());
			customerEntity.setLastName(customer.getLastName());
		} else {
			throw new CustomerServiceApiException("translateCustomerToCustomerEntity", "Date is invalid",
					HttpStatus.BAD_REQUEST);
		}
		logger.info("Completed execution of translateCustomerToCustomerEntity");
		return customerEntity;
	}

	private AddressEntity translateAddressToAddressEntity(Address address) {
		logger.info("Started execution of translateAddressToAddressEntity");
		AddressEntity addressEntity = new AddressEntity();
		addressEntity.setAddressLine1(address.getAddressLine1());
		addressEntity.setAddressline2(address.getAddressline2());
		addressEntity.setCountry(address.getCountry());
		addressEntity.setState(address.getState());
		addressEntity.setZipCode(address.getZipCode());
		logger.info("Completed execution of translateAddressToAddressEntity");
		return addressEntity;
	}

	private boolean isValidDate(Date date) {
		System.out.println("Date in business service " + date);
		Pattern p = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(date);
		Matcher m = p.matcher(dateStr);
		return m.matches();
	}

	private Customer translateCustomerEntityToCustomer(CustomerEntity customerEntity) {
		logger.info("Started execution of translateCustomerEntityToCustomer with customerEntity - {}", customerEntity);
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
		logger.info("Completed execution of translateCustomerEntityToCustomer");
		return customer;
	}

	private Address translateAddressEntityToAddress(AddressEntity addressEntity) {
		logger.info("Started execution of translateAddressEntityToAddress");
		Address address = null;
		if (addressEntity != null) {
			address = new Address();
			address.setAddressLine1(addressEntity.getAddressLine1());
			address.setAddressline2(addressEntity.getAddressline2());
			address.setState(addressEntity.getState());
			address.setZipCode(addressEntity.getZipCode());
			address.setCountry(addressEntity.getCountry());
		}
		logger.info("Completed execution of translateAddressEntityToAddress");
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
