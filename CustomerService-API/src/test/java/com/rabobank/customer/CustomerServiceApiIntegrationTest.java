package com.rabobank.customer;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.jdbc.Sql;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.rabobank.customer.model.AddressEntity;
import com.rabobank.customer.model.CustomerEntity;
import com.rabobank.customer.model.CustomerServiceResponse;

@EnableAutoConfiguration
@SpringBootTest(classes = CustomerServiceApiApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerServiceApiIntegrationTest {

	@LocalServerPort
	private int port;

	// @Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private RestTemplateBuilder restTemplateBuilder;

	@PostConstruct
	public void init() {
		MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
		jsonHttpMessageConverter.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		restTemplateBuilder.messageConverters(Collections.singletonList(jsonHttpMessageConverter)).build();
		this.restTemplate = new TestRestTemplate(restTemplateBuilder);
	}

	@Sql({ "schema.sql", "data.sql" })
	@Test
	public void testAllCustomers() {

		CustomerServiceResponse customerServiceResponse = this.restTemplate
				.getForObject("http://localhost:" + port + "/v1/customers", CustomerServiceResponse.class);

		assertTrue(customerServiceResponse.getCustomersList().size() == 5);
	}

	@Test
	public void testAddCustomer() {
		CustomerEntity customer = new CustomerEntity(0, 7, "Sathish", "Kumar", null, 28, new AddressEntity());
		CustomerEntity c = this.restTemplate.postForObject("http://localhost:" + port + "/v1/customers", customer,
				CustomerEntity.class);
		assertEquals("Sathish", c.getFirstName());
	}

	@Test
	public void testFindCustomerById() {
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		CustomerEntity customer = this.restTemplate.getForObject("http://localhost:" + port + "/v1/customers/1",
				CustomerEntity.class);

		assertTrue(customer.getFirstName().equalsIgnoreCase("Harry"));
	}

	@Test
	public void testFindCustomerByFirstName() {
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		CustomerServiceResponse customerServiceResponse = this.restTemplate.getForObject(
				"http://localhost:" + port + "/v1/customers/name?firstName=Sathish", CustomerServiceResponse.class);

		assertTrue(customerServiceResponse.getCustomersList().size() == 5);
	}

	@Test
	public void testFindCustomerByFirstAndLastName() {
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		CustomerServiceResponse customerServiceResponse = this.restTemplate.getForObject(
				"http://localhost:" + port + "/v1/customers/name?firstName=Sathish&lastName=Kumar",
				CustomerServiceResponse.class);

		assertTrue(customerServiceResponse.getCustomersList().size() != 1);
	}

	@Test
	public void testUpdateAddress() {
		AddressEntity address = new AddressEntity(1, "No 35", "Second cross", "KA", "231145", "IN");
		CustomerEntity c = this.restTemplate.patchForObject("http://localhost:" + port + "/v1/customers/1", address,
				CustomerEntity.class);
		assertEquals("First street", c.getAddress().getAddressLine1());
	}

}