package com.rabobank.customer.model;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Customer {

	private long customerId;

	@Size(min = 1, max = 50, message = "First Name should be between 1 and 50 chars long")
	@Pattern(regexp = "[A-Za-z\\W]+", message = "First Name should not contain any special characters")
	private String firstName;

	@Size(min = 1, max = 50, message = "Last Name should be between 1 and 50 chars long")
	@Pattern(regexp = "[A-Za-z\\W]+", message = "Last Name should not contain any special characters")
	private String lastName;

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	private Integer age;

	@NotNull(message = "Date of birth cannot be blank")
	@Past
	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	private Date dateOfBirth;

	@Valid
	private Address address;

	public Customer() {

	}

	public Customer(long customerId, String firstName, String lastName, Integer age, Date dateOfBirth,
			Address address) {
		super();
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
