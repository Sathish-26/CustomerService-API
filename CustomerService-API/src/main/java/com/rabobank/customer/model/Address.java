package com.rabobank.customer.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "T_ADDRESS")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id"/* , scope = Address.class */)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7345418062479675657L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull
	@Size(max = 50)
	@Column(name = "ADDR_LINE1")
	private String addressLine1;

	@Size(max = 50)
	@Column(name = "ADDR_LINE2")
	private String addressline2;

	@NotNull
	@Size(max = 20)
	@Column(name = "STATE")
	private String state;

	@NotNull
	@Size(max = 10)
	@Column(name = "ZIP_CD")
	private String zipCode;

	@NotNull
	@Size(max = 15)
	@Column(name = "COUNTRY")
	private String country;

//	@OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "customer_id", nullable = false)
//	@Cascade(org.hibernate.annotations.CascadeType.ALL)
//	private Customer customer;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

//	public Customer getCustomer() {
//		return customer;
//	}
//
//	public void setCustomer(Customer customer) {
//		this.customer = customer;
//	}

	public Address() {

	}

	public Address(long id, String addressLine1, String addressline2, String state, String zipCode, String country,
			Customer customer) {
		super();
		this.id = id;
		this.addressLine1 = addressLine1;
		this.addressline2 = addressline2;
		this.state = state;
		this.zipCode = zipCode;
		this.country = country;
//		this.customer = customer;
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
