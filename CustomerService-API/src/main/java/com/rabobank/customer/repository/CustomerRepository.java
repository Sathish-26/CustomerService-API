package com.rabobank.customer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rabobank.customer.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	@Query("Select c from Customer c where c.firstName like CONCAT('%', ?1, '%')")
	List<Customer> getByFirstName(String firstName);

	@Query("Select c from Customer c where c.firstName like CONCAT('%', ?1, '%') AND c.lastName like concat('%', ?2, '%')")
	List<Customer> getByFirstNameAndLastName(String firstName, String lastName);

}
