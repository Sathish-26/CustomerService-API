package com.rabobank.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.rabobank.customer.business.CustomerApiBusinessServiceImpl;
import com.rabobank.customer.model.Address;
import com.rabobank.customer.model.AddressEntity;
import com.rabobank.customer.model.Customer;
import com.rabobank.customer.model.CustomerEntity;
import com.rabobank.customer.repository.CustomerRepository;

@RunWith(MockitoJUnitRunner.class)
public class CustomerApiBusinessServiceTest {

	@Mock
	private CustomerRepository customerRepository;

	@InjectMocks
	private CustomerApiBusinessServiceImpl businessServiceImpl;

	private Date date;

	private AddressEntity addressEntity;

	private Address address;

	@Before()
	public void init() throws ParseException {
		MockitoAnnotations.initMocks(this);
		String date = "1990-01-01";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		this.date = sdf.parse(date);
		this.addressEntity = new AddressEntity(1, "New Apartments", "4th cross street", "KA", "570016", "IN");
		this.address = new Address("New Apartments", "4th cross street", "KA", "570016", "IN");
	}

	@Test
	public void whenFindAll_thenReturnCustomersList() {
		// given
		CustomerEntity customer = new CustomerEntity(1, 1, "Sathish", "Kumar", date, 28, addressEntity);
		List<CustomerEntity> expectedCustomers = Arrays.asList(customer);

		doReturn(expectedCustomers).when(customerRepository).findAll();

		// when
		List<Customer> actualCustomersList = businessServiceImpl.retrieveAllCustomers();

		// then
		assertThat(actualCustomersList.size()).isEqualTo(expectedCustomers.size());
	}

	@Test
	public void whenFindById_thenReturnCustomer() {
		// given
		CustomerEntity customer = new CustomerEntity(1L, 1, "Sathish", "Kumar", date, 28, addressEntity);
		Optional<CustomerEntity> customerOptional = Optional.of(customer);

		doReturn(customerOptional).when(customerRepository).findById(1L);

		// when
		Customer actualCustomer = businessServiceImpl.retrieveCustomerById(1L);

		// then
		assertThat(actualCustomer.getFirstName()).isEqualTo(customerOptional.get().getFirstName());
	}

	@Test
	public void whenFindByFirstAndLastName_thenReturnCustomersList() {
		// given
		CustomerEntity customer = new CustomerEntity(1, 1, "Sathish", "Kumar", date, 28, addressEntity);
		List<CustomerEntity> expectedCustomers = Arrays.asList(customer);

		doReturn(expectedCustomers).when(customerRepository).getByFirstNameAndLastName("Sathish", "Kumar");

		// when
		List<Customer> actualCustomersList = businessServiceImpl.retrieveCustomerByFirstNameAndLastName("Sathish",
				"Kumar");

		// then
		assertThat(actualCustomersList.size()).isEqualTo(expectedCustomers.size());
	}

	@Test
	public void whenAddNewCustomer_thenReturnCustomer() {
		// given
		CustomerEntity customerEntity = new CustomerEntity(1, 1, "Sathish", "Kumar", date, 28, addressEntity);

		doReturn(customerEntity).when(customerRepository).save(Mockito.any(CustomerEntity.class));

		Customer customer = new Customer(1, 1, "Sathish", "Kumar", 0, date, address);

		// when
		Customer actualCustomer = businessServiceImpl.addANewCustomer(customer);

		// then
		assertThat(actualCustomer.getFirstName()).isEqualTo("Sathish");
	}

	@Test
	public void whenUpdateCustomerAddress_thenReturnUpdatedCustomer() {

		// given
		CustomerEntity customerEntity = new CustomerEntity(1, 1, "Sathish", "Kumar", date, 28, addressEntity);
		Optional<CustomerEntity> customerEntityOptional = Optional.of(customerEntity);

		doReturn(customerEntityOptional).when(customerRepository).findById(1L);

		doReturn(customerEntity).when(customerRepository).save(customerEntity);

		// when
		Customer actualCustomer = businessServiceImpl.updateCustomerAddress(customerEntity.getCustomerId(), address);

		// then
		assertThat(actualCustomer.getAddress().getCountry())
				.isEqualTo(customerEntityOptional.get().getAddress().getCountry());
	}
}