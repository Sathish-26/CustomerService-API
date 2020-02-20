package com.rabobank.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import com.rabobank.customer.model.Address;
import com.rabobank.customer.model.Customer;

@SpringBootTest(classes = CustomerServiceApiApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerServiceApiIntegrationTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Sql({ "schema.sql", "data.sql" })
	@Test
	public void testAllCustomers() {
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Customer[] customers = this.restTemplate.getForObject("http://localhost:" + port + "/v1/customers",
				Customer[].class);

		assertTrue(customers.length == 5);
	}

	@Test
	public void testAddCustomer() {
		Customer customer = new Customer(0, 7, "Sathish", "Kumar", null, 28, new Address());
		Customer c = this.restTemplate.postForObject("http://localhost:" + port + "/v1/customers", customer,
				Customer.class);
		assertEquals("Sathish", c.getFirstName());
	}

	@Test
	public void testFindCustomerById() {
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Customer customer = this.restTemplate.getForObject("http://localhost:" + port + "/v1/customers/1",
				Customer.class);

		assertTrue(customer.getId() == 1);
	}

	@Test
	public void testFindCustomerByFirstName() {
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Customer[] customers = this.restTemplate
				.getForObject("http://localhost:" + port + "/v1/customers/name?firstName=Sathish", Customer[].class);

		assertTrue(customers.length != 1);
	}

	@Test
	public void testFindCustomerByFirstAndLastName() {
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Customer[] customers = this.restTemplate.getForObject(
				"http://localhost:" + port + "/v1/customers/name?firstName=Sathish&lastName=Kumar", Customer[].class);

		assertTrue(customers.length != 1);
	}

	@Test
	public void testUpdateAddress() {
		Customer customer = new Customer(0, 7, "Sathish", "Kumar", null, 28, new Address());
		Address address = new Address(1, "No 35", "Second cross", "KA", "231145", "IN", customer);
		Customer c = this.restTemplate.patchForObject("http://localhost:" + port + "/v1/customers/1", address,
				Customer.class);
		assertEquals("First street", c.getAddress().getAddressLine1());
	}

}