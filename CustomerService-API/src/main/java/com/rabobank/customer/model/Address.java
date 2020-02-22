package com.rabobank.customer.model;

import javax.validation.constraints.Size;

public class Address {

	@Size(min = 5, max = 75, message = "Address line1 should be between 5 and 75 chars long")
	private String addressLine1;

	private String addressline2;

	@Size(min = 2, max = 15, message = "State should be between 5 and 15 chars long")
	private String state;

	@Size(min = 6, max = 6, message = "ZipCode should be 6 chars long")
	private String zipCode;

	@Size(min = 2, max = 15, message = "Country should be between 5 and 15 chars long")
	private String country;

	public Address() {

	}

	public Address(String addressLine1, String addressline2, String state, String zipCode, String country) {
		super();
		this.addressLine1 = addressLine1;
		this.addressline2 = addressline2;
		this.state = state;
		this.zipCode = zipCode;
		this.country = country;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressline2() {
		return addressline2;
	}

	public void setAddressline2(String addressline2) {
		this.addressline2 = addressline2;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
