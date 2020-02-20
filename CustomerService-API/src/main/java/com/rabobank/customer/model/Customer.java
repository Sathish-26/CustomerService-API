package com.rabobank.customer.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "T_CUSTOMER")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2842116160173783996L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull
	@Column(name = "CUSTOMER_ID")
	private Integer customerId;

	@NotNull
	@Size(max = 50)
	@Column(name = "FIRST_NAME")
	private String firstName;

	@Size(max = 50)
	@Column(name = "LAST_NAME")
	private String lastName;

	@Temporal(TemporalType.DATE)
	@Column(name = "DOB")
	private Date dateOfBirth;

	@Transient
	private Integer age;

	@OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
	private Address address;

	@Version
	private int version;

	public Customer() {

	}

	public Customer(long id, Integer customerId, String firstName, String lastName, Date dateOfBirth, Integer age,
			Address address) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.age = age;
		this.address = address;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
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

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Integer getAge() {
		if (this.dateOfBirth != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(dateOfBirth);
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int date = c.get(Calendar.DATE);
			LocalDate l1 = LocalDate.of(year, month, date);
			LocalDate now1 = LocalDate.now();
			Period period = Period.between(l1, now1);
			age = period.getYears();
		}
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

	@Override
	public String toString() {
		return "Customer [id=" + id + ", customerId=" + customerId + ", firstName=" + firstName + ", lastName="
				+ lastName + ", dateOfBirth=" + dateOfBirth + ", age=" + age + ", address=" + address + ", version="
				+ version + "]";
	}

}
