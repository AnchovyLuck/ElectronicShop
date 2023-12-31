package com.shopme.admin.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.repository.BrandRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.BrandNotFoundException;

@Service
public class BrandService {
	public static final int BRANDS_PER_PAGE = 10;
	
	@Autowired
	private BrandRepository brandRepo;

	public List<Brand> listAll() {
		return (List<Brand>) brandRepo.findAll(Sort.by("name").ascending());
	}
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, BRANDS_PER_PAGE, brandRepo);
	}

	public Brand save(Brand brand) {
		return brandRepo.save(brand);
	}

	public Brand get(Integer id) throws BrandNotFoundException {
		try {
			return brandRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new BrandNotFoundException("Could not find any brand with ID " + id);
		}
	}

	public void delete(Integer id) throws BrandNotFoundException {
		Long countById = brandRepo.countById(id);

		if (countById == null || countById == 0) {
			throw new BrandNotFoundException("Could not find any brand with ID " + id);
		}
		Brand deletingBrand = brandRepo.findById(id).get();
		
		Set<Product> productsPerBrand = deletingBrand.getProducts();
		productsPerBrand.forEach(product -> deletingBrand.removeProduct(product));

		brandRepo.deleteById(id);
	}

	public String checkUnique(Integer id, String name) {
		boolean creatingNew = (id == null || id == 0);

		Brand brandByName = brandRepo.findByName(name);

		if (creatingNew) {
			if (brandByName != null) {
				return "Duplicate";
			}
		} else if (brandByName != null && brandByName.getId() != id) {
			return "Duplicate";
		}

		return "OK";
	}
}
