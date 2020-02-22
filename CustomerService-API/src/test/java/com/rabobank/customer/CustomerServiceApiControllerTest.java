package com.rabobank.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rabobank.customer.business.CustomerApiBusinessServiceImpl;
import com.rabobank.customer.model.Address;
import com.rabobank.customer.model.AddressEntity;
import com.rabobank.customer.model.Customer;
import com.rabobank.customer.model.CustomerEntity;
import com.rabobank.customer.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceApiControllerTest {

	@Mock
	private CustomerRepository customerRepository;

	@InjectMocks
	private CustomerApiBusinessServiceImpl businessServiceImpl;

	@Test
	public void whenFindAll_thenReturnCustomersList() {
		// given
		CustomerEntity customer = new CustomerEntity(1, 1, "Sathish", "Kumar", null, 28, new AddressEntity());
		List<CustomerEntity> expectedCustomers = Arrays.asList(customer);

		doReturn(expectedCustomers).when(customerRepository).findAll();

		// when
		List<Customer> actualCustomersList = businessServiceImpl.retrieveAllCustomers();

		// then
		assertThat(actualCustomersList).isEqualTo(expectedCustomers);
	}

	@Test
	public void whenFindById_thenReturnCustomer() {
		// given
		CustomerEntity customer = new CustomerEntity(1L, 1, "Sathish", "Kumar", null, 28, new AddressEntity());
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
		CustomerEntity customer = new CustomerEntity(1, 1, "Sathish", "Kumar", null, 28, new AddressEntity());
		List<CustomerEntity> expectedCustomers = Arrays.asList(customer);

		doReturn(expectedCustomers).when(customerRepository).getByFirstNameAndLastName("Sathish", "Kumar");

		// when
		List<Customer> actualCustomersList = businessServiceImpl.retrieveCustomerByFirstNameAndLastName("Sathish",
				"Kumar");

		// then
		assertThat(actualCustomersList).isEqualTo(expectedCustomers);
	}

	@Test
	public void whenAddNewCustomer_thenReturnCustomer() {
		// given
		CustomerEntity customerEntity = new CustomerEntity(1, 1, "Sathish", "Kumar", null, 28, new AddressEntity());

		doReturn(customerEntity).when(customerRepository).save(customerEntity);

		Customer customer = new Customer();
		// when
		Customer actualCustomer = businessServiceImpl.addANewCustomer(customer);

		// then
		assertThat(actualCustomer.getFirstName()).isEqualTo("Sathish");
	}

	@Test
	public void whenUpdateCustomerAddress_thenReturnUpdatedCustomer() {
		// given

		Customer customer = new Customer(1, "Sathish", "Kumar", 0, new Date(), new Address());
		Address address = new Address("No 35", "Second cross", "KA", "231145", "IN");
		customer.setAddress(address);
		CustomerEntity customerEntity = new CustomerEntity();
		doReturn(customerEntity).when(customerRepository).save(customerEntity);

		// when
		Customer actualCustomer = businessServiceImpl.updateCustomerAddress(0, address);
		// then
		assertThat(actualCustomer.getAddress().getCountry()).isEqualTo(customer.getAddress().getCountry());
	}
}