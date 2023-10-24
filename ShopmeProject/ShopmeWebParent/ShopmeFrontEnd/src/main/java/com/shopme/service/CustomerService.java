package com.shopme.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.repository.CountryRepository;
import com.shopme.repository.CustomerRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

@Service
public class CustomerService {

	@Autowired
	private CountryRepository countryRepo;
	
	@Autowired
	private CustomerRepository customerRepo;
	
	public List<Country> listAllCountries() {
		return (List<Country>) countryRepo.findAll();
	}
	
	public boolean isEmailUnique(String email) {
		Customer findByEmail = customerRepo.findByEmail(email);
		
		if (findByEmail != null) {
			return false;
		}
		return true;
	}
}
