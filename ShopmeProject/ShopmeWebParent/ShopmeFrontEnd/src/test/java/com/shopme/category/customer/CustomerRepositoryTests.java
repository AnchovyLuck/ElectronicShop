package com.shopme.category.customer;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.repository.CustomerRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {

	@Autowired
	private CustomerRepository repo;
	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateCustomer1() {
		Integer countryId = 2; // USA
		Country country = entityManager.find(Country.class, countryId);

		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("Anchovy");
		customer.setLastName("Luck");
		customer.setPassword("password123");
		customer.setEmail("anchovyluck@gmail.com");
		customer.setPhoneNumber("036 758 7383");
		customer.setAddressLine1("1478 Hung Vuong Street");
		customer.setCity("Nga Bay");
		customer.setState("Hau Giang");
		customer.setPostalCode("900000");
		customer.setCreatedTime(new Date());

		Customer savedCustomer = repo.save(customer);

		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateCustomer2() {
		Integer countryId = 1; // USA
		Country country = entityManager.find(Country.class, countryId);

		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("Sanya");
		customer.setLastName("Lad");
		customer.setPassword("password456");
		customer.setEmail("sanya.lad2020@gmail.com");
		customer.setPhoneNumber("02224928052");
		customer.setAddressLine1("173, A-, Shah & Nahar Ind1.estate, Sunmill Road");
		customer.setAddressLine2("Dhanraj Mill Compound, Lower Parel (west)");
		customer.setCity("Mumbai");
		customer.setState("Maharashtra");
		customer.setPostalCode("400013");
		customer.setCreatedTime(new Date());

		Customer savedCustomer = repo.save(customer);

		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}

	@Test
	public void testListCustomers() {
		Iterable<Customer> customers = repo.findAll();
		customers.forEach(System.out::println);

		assertThat(customers).hasSizeGreaterThanOrEqualTo(1);
	}

	@Test
	public void testUpdateCustomer() {
		Integer customerId = 1;
		String lastName = "Stanfield";

		Customer customer = repo.findById(customerId).get();
		customer.setLastName(lastName);
		customer.setEnabled(true);

		Customer updatedCustomer = repo.save(customer);

		assertThat(updatedCustomer.getLastName()).isEqualTo(lastName);
	}

	@Test
	public void testGetCustomer() {
		Integer customerId = 2;
		Optional<Customer> findById = repo.findById(customerId);

		assertThat(findById).isPresent();

		Customer customer = findById.get();
		System.out.println(customer);
	}
	
	@Test
	public void testDeleteCustomer() {
		Integer customerId = 2;
		repo.deleteById(customerId);
		
		Optional<Customer> findById = repo.findById(customerId);
		assertThat(findById).isNotPresent();
	}
	
	@Test
	public void testFindByEmail() {
		String email = "anchovyluck@gmail.com";
		Customer findByEmail = repo.findByEmail(email);
		
		assertThat(findByEmail).isNotNull();
		System.out.println(findByEmail);
	}
	
	@Test
	public void testFindByVerificationCode() {
		String code = "code_123";
		Customer customer = repo.findByVerificationCode(code);
		
		assertThat(customer).isNotNull();
		System.out.println(customer);
	}
	
	@Test
	public void testEnableCustomer() {
		Integer customerId = 1;
		repo.enable(customerId);
		
		Customer customer = repo.findById(customerId).get();
		assertThat(customer.isEnabled()).isTrue();
		
	}
}
