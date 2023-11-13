package com.shopme.admin.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.service.BrandService;
import com.shopme.admin.service.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.exception.BrandNotFoundException;

@Controller
public class BrandController {

	@Autowired
	private BrandService brandService;

	@Autowired
	private CategoryService catService;

	@GetMapping("/brands")
	public String listFirstPage() {
		return "redirect:/brands/page/1?sortField=id&sortDir=asc";
	}

	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listBrands", moduleURL = "/brands") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum) {
		brandService.listByPage(pageNum, helper);
		return "brands/brands";
	}

	@GetMapping("brands/new")
	public String newBrand(Model model) {
		List<Category> listCategories = catService.listCategoriesUsedInForm();

		model.addAttribute("brand", new Brand());
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Create New Brand");

		return "brands/brand_form";
	}

	@PostMapping("/brands/save")
	public String saveBrand(Brand brand, @RequestParam("fileImage") MultipartFile multipartFile, RedirectAttributes ra)
			throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);

			Brand savedBrand = brandService.save(brand);
			String uploadDir = "../brand-logos/" + savedBrand.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			brandService.save(brand);
		}

		ra.addFlashAttribute("message", "The brand has been saved successfully!");
		return "redirect:/brands/page/1?sortField=id&sortDir=asc";
	}

	@GetMapping("/brands/edit/{id}")
	public String editBrand(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Brand brand = brandService.get(id);
			List<Category> listCategories = catService.listCategoriesUsedInForm();

			model.addAttribute("brand", brand);
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("pageTitle", "Edit brand (ID: " + id + ")");

			return "brands/brand_form";
		} catch (BrandNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return "redirect:/brands/page/1?sortField=id&sortDir=asc";
		}
	}

	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes ra) {
		try {
			brandService.delete(id);
			String brandDir = "../brand-logo/" + id;
			FileUploadUtil.removeDir(brandDir);

			ra.addFlashAttribute("message", "The brand ID " + id + " has been deleted successfully!");
		} catch (BrandNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}

		return "redirect:/brands/page/1?sortField=id&sortDir=asc";
	}
}
