package com.rabobank.customer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.jdbc.Sql;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.rabobank.customer.model.Address;
import com.rabobank.customer.model.Customer;
import com.rabobank.customer.model.CustomerEntity;
import com.rabobank.customer.model.CustomerServiceResponse;

@EnableAutoConfiguration
@SpringBootTest(classes = CustomerServiceApiApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerServiceApiIntegrationTest {

	@LocalServerPort
	private int port;

	private TestRestTemplate restTemplate;

	@Autowired
	private RestTemplateBuilder restTemplateBuilder;

	private Date date;

	private Address address;

	@PostConstruct
	public void init() throws ParseException {
		MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
		jsonHttpMessageConverter.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		restTemplateBuilder.messageConverters(Collections.singletonList(jsonHttpMessageConverter)).build();
		this.restTemplate = new TestRestTemplate(restTemplateBuilder);

		String date = "1990-01-01";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		this.date = sdf.parse(date);
		this.address = new Address("New Apartments", "4th cross street", "KA", "570016", "IN");
	}

	@Sql({ "schema.sql", "data.sql" })
	@Test
	public void testAllCustomers() {

		CustomerServiceResponse customerServiceResponse = this.restTemplate
				.getForObject("http://localhost:" + port + "/customerApp/v1/customers", CustomerServiceResponse.class);

		assertTrue(customerServiceResponse.getCustomersList().size() == 5);
	}

	@Test
	public void testAddCustomer() {
		Customer customer = new Customer("Sathish", "Kumar", date, address);
		HttpEntity<Customer> requestEntity = new HttpEntity<Customer>(customer);
		ResponseEntity<Customer> c = this.restTemplate.exchange(
				"http://localhost:" + port + "/customerApp/v1/customers", HttpMethod.POST, requestEntity,
				Customer.class);
		assertEquals("Sathish", c.getBody().getFirstName());
	}

	@Test
	public void testFindCustomerById() {
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		CustomerEntity customer = this.restTemplate
				.getForObject("http://localhost:" + port + "/customerApp/v1/customers/1", CustomerEntity.class);

		assertTrue(customer.getFirstName().equalsIgnoreCase("Harry"));
	}

	@Test
	public void testFindCustomerByFirstAndLastName() {
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		CustomerServiceResponse customerServiceResponse = this.restTemplate.getForObject(
				"http://localhost:" + port + "/customerApp/v1/customers/search?firstName=Sathish&lastName=Kumar",
				CustomerServiceResponse.class);
		assertTrue(customerServiceResponse.getCustomersList().size() != 1);
	}

	@Test
	public void testUpdateAddress() {
		HttpEntity<Address> requestEntity = new HttpEntity<Address>(address);
		ResponseEntity<Customer> c = this.restTemplate.exchange(
				"http://localhost:" + port + "/customerApp/v1/customers/1", HttpMethod.PUT, requestEntity,
				Customer.class);
		assertNotNull(c.getBody().getAddress());
	}

}