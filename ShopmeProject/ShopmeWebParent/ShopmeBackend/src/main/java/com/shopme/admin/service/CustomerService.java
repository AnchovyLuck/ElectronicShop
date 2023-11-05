package com.shopme.admin.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.exception.CustomerNotFoundException;
import com.shopme.admin.repository.CountryRepository;
import com.shopme.admin.repository.CustomerRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerService {
	public static final int CUSTOMERS_PER_PAGE = 10;
	
	@Autowired
	private CustomerRepository customerRepo;
	
	@Autowired
	private CountryRepository countryRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, CUSTOMERS_PER_PAGE, sort);
		
		if (keyword != null) {
			return customerRepo.findAll(keyword, pageable);
		}
		
		return customerRepo.findAll(pageable);
	}
	
	public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
		customerRepo.updateEnabledStatus(id, enabled);
	}
	
	public Customer get(Integer id) throws CustomerNotFoundException {
		try {
			return customerRepo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new CustomerNotFoundException("Could not find any customers with ID " + id);
		}
	}
	
	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}
	
	public boolean isEmailUnique(Integer id, String email) {
		Customer existCustomer = customerRepo.findByEmail(email);		
		return (existCustomer != null && existCustomer.getId() != id) ? false : true;
	}
	
	public void save(Customer customerInForm) {
		if (!customerInForm.getPassword().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
			customerInForm.setPassword(encodedPassword);
		} else {
			Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();
			customerInForm.setPassword(customerInDB.getPassword());
		}
		customerRepo.save(customerInForm);
	}
	
	public void delete(Integer id) throws CustomerNotFoundException {
		Long count = customerRepo.countById(id);
		if (count == null || count == 0) {
			throw new CustomerNotFoundException("Could not find any customers with ID " + id);
		}
		customerRepo.deleteById(id);
	}
}