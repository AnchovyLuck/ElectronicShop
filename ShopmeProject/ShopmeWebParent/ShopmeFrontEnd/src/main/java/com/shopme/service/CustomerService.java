package com.shopme.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.repository.CountryRepository;
import com.shopme.repository.CustomerRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

import net.bytebuddy.utility.RandomString;

@Service
public class CustomerService {

	@Autowired
	private CountryRepository countryRepo;
	
	@Autowired
	private CustomerRepository customerRepo;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}
	
	public boolean isEmailUnique(String email) {
		Customer findByEmail = customerRepo.findByEmail(email);
		
		return (findByEmail == null) ? true : false;
	}
	
	public void registerCustomer(Customer customer) {
		encodePassword(customer);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());
		
		String randomCode = RandomString.make(64);
		customer.setVerificationCode(randomCode);
		
		customerRepo.save(customer);
	}
	
	private void encodePassword(Customer customer) {
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
	}
}
