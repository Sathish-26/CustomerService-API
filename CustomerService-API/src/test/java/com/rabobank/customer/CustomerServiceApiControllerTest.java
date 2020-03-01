package com.rabobank.customer;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabobank.customer.business.CustomerApiBusinessServiceImpl;
import com.rabobank.customer.model.Address;
import com.rabobank.customer.model.AddressEntity;
import com.rabobank.customer.model.Customer;
import com.rabobank.customer.model.CustomerEntity;
import com.rabobank.customer.repository.CustomerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CustomerServiceApiApplication.class)
@AutoConfigureMockMvc
@TestMethodOrder(value = OrderAnnotation.class)
class CustomerServiceApiControllerTest {

	@Autowired
	private MockMvc mvc;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private CustomerApiBusinessServiceImpl businessService;

	private Date date;

	private Address address;

	private AddressEntity addressEntity;

	private CustomerEntity customerEntity;

	private Customer customer;

	private String baseUrl = "/v1/customers";

	@PostConstruct
	public void init() throws ParseException {
		String dateStr = "1990-01-01";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		this.date = sdf.parse(dateStr);
		this.address = new Address("New Apartments", "4th cross street", "KA", "570016", "IN");
		this.addressEntity = new AddressEntity(1L, "New Apartments", "4th cross street", "KA", "570016", "IN");
		this.customerEntity = new CustomerEntity(1L, 1L, "Sathish", "Kumar", this.date, 28, this.addressEntity);
		this.customer = new Customer("Sathish", "Kumar", this.date, address);
	}

	@Test
	@Order(1)
	public void whenaddNewCustomer_thenReturnNewCustomer() throws Exception {

		when(customerRepository.save(Mockito.any(CustomerEntity.class))).thenReturn(customerEntity);

		when(businessService.addANewCustomer(customer)).thenReturn(customer);

		MockHttpServletRequestBuilder mockHttpReqBuilder = MockMvcRequestBuilders.post(baseUrl)
				.content(asJsonString(customer)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);
		mvc.perform(mockHttpReqBuilder).andExpect(status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$.customerId").exists());
	}

	@Test
	@Order(2)
	public void whenFindAll_thenReturnCustomersList() throws Exception {

		// given
		List<CustomerEntity> customerEntityList = new ArrayList<>();
		customerEntityList.add(customerEntity);

		when(customerRepository.findAll()).thenReturn(customerEntityList);

		List<Customer> customerList = new ArrayList<>();
		customerList.add(customer);

		when(businessService.retrieveAllCustomers()).thenReturn(customerList);

		MockHttpServletRequestBuilder mockHttpReqBuilder = MockMvcRequestBuilders.get(baseUrl)
				.accept(MediaType.APPLICATION_JSON);

		mvc.perform(mockHttpReqBuilder).andExpect(status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$.customersList").isNotEmpty());

	}

	@Test
	@Order(3)
	public void whenFindById_thenReturnCustomer() throws Exception {

		Optional<CustomerEntity> customerEntityOptional = Optional.of(customerEntity);

		// given
		when(customerRepository.findById(1L)).thenReturn(customerEntityOptional);

		when(businessService.retrieveCustomerById(1L)).thenReturn(customer);

		MockHttpServletRequestBuilder mockHttpReqBuilder = MockMvcRequestBuilders.get(baseUrl + "/1")
				.accept(MediaType.APPLICATION_JSON);

		mvc.perform(mockHttpReqBuilder).andExpect(status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$.customerId").exists());

	}

	@Test
	@Order(4)
	public void whenFindByFirstAndLastName_thenReturnCustomersList() throws Exception {
		List<CustomerEntity> customerEntityList = new ArrayList<>();
		customerEntityList.add(customerEntity);

		// given
		when(customerRepository.getByFirstNameAndLastName("Sathish", "Kumar")).thenReturn(customerEntityList);

		List<Customer> customerList = new ArrayList<>();
		customerList.add(customer);

		when(businessService.retrieveCustomerByFirstNameAndLastName("Sathish", "Kumar")).thenReturn(customerList);

		MockHttpServletRequestBuilder mockHttpReqBuilder = MockMvcRequestBuilders
				.get(baseUrl + "/search?firstName=Sathish&lastName=Kumar").accept(MediaType.APPLICATION_JSON);

		mvc.perform(mockHttpReqBuilder).andExpect(status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$.customersList").exists());

	}

	@Test
	@Order(5)
	public void whenUpdateCustomerAddress_thenReturnUpdatedCustomer() throws Exception {

		// given
		when(customerRepository.save(customerEntity)).thenReturn(customerEntity);

		Optional<CustomerEntity> customerEntityOptional = Optional.of(customerEntity);

		when(customerRepository.findById(customerEntity.getId())).thenReturn(customerEntityOptional);

		when(businessService.updateCustomerAddress(customerEntity.getId(), address)).thenReturn(customer);

		MockHttpServletRequestBuilder mockHttpReqBuilder = MockMvcRequestBuilders.put(baseUrl + "/1")
				.content(asJsonString(address)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		mvc.perform(mockHttpReqBuilder).andExpect(status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$.customerId").exists());

	}

	public String asJsonString(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {

		}
		return jsonString;
	}

}
