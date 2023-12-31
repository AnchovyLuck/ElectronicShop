package com.shopme.admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.service.BrandService;
import com.shopme.common.dto.CategoryDTO;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.exception.BrandNotFoundException;
import com.shopme.common.exception.BrandNotFoundRestException;

@RestController
public class BrandRestController {

	@Autowired
	private BrandService service;

	@PostMapping("/brands/check_unique")
	public String checkUnique(@Param("id") Integer id, @Param("name") String name) {
		return service.checkUnique(id, name);
	}

	@GetMapping("brands/{id}/categories")
	public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name = "id") Integer brandId) throws BrandNotFoundRestException {
		List<CategoryDTO> listCategories = new ArrayList<>();

		try {
			Brand brand = service.get(brandId);

			Set<Category> categories = brand.getCategories();

			categories.forEach(cat -> {
				CategoryDTO dto = new CategoryDTO(cat.getId(), cat.getName());
				listCategories.add(dto);
			});
			return listCategories;

		} catch (BrandNotFoundException e) {
			throw new BrandNotFoundRestException();
		}
	}
}
