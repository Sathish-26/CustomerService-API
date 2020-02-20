package com.rabobank.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import java.util.Arrays;
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
import com.rabobank.customer.model.Customer;
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
		Customer customer = new Customer(1, 1, "Sathish", "Kumar", null, 28, new Address());
		List<Customer> expectedCustomers = Arrays.asList(customer);

		doReturn(expectedCustomers).when(customerRepository).findAll();

		// when
		List<Customer> actualCustomersList = businessServiceImpl.retrieveAllCustomers();

		// then
		assertThat(actualCustomersList).isEqualTo(expectedCustomers);
	}

	@Test
	public void whenFindById_thenReturnCustomer() {
		// given
		Customer customer = new Customer(1L, 1, "Sathish", "Kumar", null, 28, new Address());
		Optional<Customer> customerOptional = Optional.of(customer);

		doReturn(customerOptional).when(customerRepository).findById(1L);

		// when
		Customer actualCustomer = businessServiceImpl.retrieveCustomerById(1L);

		// then
		assertThat(actualCustomer.getFirstName()).isEqualTo(customerOptional.get().getFirstName());
	}

	@Test
	public void whenFindByFirstName_thenReturnCustomersList() {
		// given
		Customer customer = new Customer(1, 1, "Sathish", "Kumar", null, 28, new Address());
		List<Customer> expectedCustomers = Arrays.asList(customer);

		doReturn(expectedCustomers).when(customerRepository).getByFirstName("Sathish");

		// when
		List<Customer> actualCustomersList = businessServiceImpl.retrieveCustomerByFirstName("Sathish");

		// then
		assertThat(actualCustomersList).isEqualTo(expectedCustomers);
	}

	@Test
	public void whenFindByFirstAndLastName_thenReturnCustomersList() {
		// given
		Customer customer = new Customer(1, 1, "Sathish", "Kumar", null, 28, new Address());
		List<Customer> expectedCustomers = Arrays.asList(customer);

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
		Customer customer = new Customer(1, 1, "Sathish", "Kumar", null, 28, new Address());

		doReturn(customer).when(customerRepository).save(customer);

		// when
		Customer actualCustomer = businessServiceImpl.addANewCustomer(customer);

		// then
		assertThat(actualCustomer.getFirstName()).isEqualTo("Sathish");
	}

	@Test
	public void whenUpdateCustomerAddress_thenReturnUpdatedCustomer() {
		// given

		Customer customer = new Customer(0, 1, "Sathish", "Kumar", null, 28, null);
		Address address = new Address(0, "No 35", "Second cross", "KA", "231145", "IN");
		customer.setAddress(address);
		doReturn(customer).when(customerRepository).save(customer);

		// when
		Customer actualCustomer = businessServiceImpl.updateCustomerAddress(0, address);
		// then
		assertThat(actualCustomer.getAddress().getCountry()).isEqualTo(customer.getAddress().getCountry());
	}
}