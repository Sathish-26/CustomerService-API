package com.rabobank.customer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rabobank.customer.model.CustomerEntity;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

	// @Query("Select c from CustomerEntity c where (c.firstName like concat('%',
	// ?1, '%') AND c.lastName like concat('%', ?2, '%') OR (c.firstName like
	// concat('%', ?1, '%') OR c.lastName like concat('%', ?2, '%')))")
	// @Query("Select c from CustomerEntity c where (c.firstName like concat('%',
	// ?1, '%') OR c.lastName like concat('%', ?2, '%'))")
	@Query("Select c from CustomerEntity c where (c.firstName = ?1 OR c.lastName = ?2)")
	List<CustomerEntity> getByFirstNameAndLastName(String firstName, String lastName);

}
