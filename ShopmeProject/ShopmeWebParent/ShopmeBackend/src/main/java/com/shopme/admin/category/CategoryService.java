package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryService {
	public static final int categoryS_PER_PAGE = 4;

	@Autowired
	private CategoryRepository catRepo;

	public List<Category> listAll() {
		List<Category> rootCategories = catRepo.findRootCategories();
		return listHierarchicalCategories(rootCategories);
	}

	private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
		List<Category> hierarchicalCategories = new ArrayList<>();

		rootCategories.forEach(rootCategory -> {
			hierarchicalCategories.add(Category.copyFull(rootCategory));

			Set<Category> children = rootCategory.getChildren();
			children.forEach(subCategory -> {
				String name = "--" + subCategory.getName();
				
				hierarchicalCategories.add(Category.copyFull(subCategory, name));
				
				listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1);
			});
		});

		return hierarchicalCategories;
	}

	private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();

		children.forEach(subCategory -> {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			hierarchicalCategories.add(Category.copyFull(subCategory, name));
			listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel);
		});
	}

	public Category save(Category category) {
		return catRepo.save(category);
	}

//	public Page<Category> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
//		Sort sort = Sort.by(sortField);
//
//		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
//
//		Pageable pageable = PageRequest.of(pageNum - 1, categoryS_PER_PAGE, sort);
//		if (keyword != null) {
//			return catRepo.findAll(keyword, pageable);
//		}
//
//		return catRepo.findAll(pageable);
//	}

	public List<Category> listCategoriesUsedInForm() {
		List<Category> categoriesUsedInForm = new ArrayList<>();
		Iterable<Category> categoriesInDB = catRepo.findAll();

		categoriesInDB.forEach(category -> {
			if (category.getParent() == null) {
				categoriesUsedInForm.add(Category.copyIdAndName(category));

				Set<Category> children = category.getChildren();

				children.forEach(subCategory -> {
					String name = "--" + subCategory.getName();
					categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
					listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
				});
			}
		});
		return categoriesUsedInForm;
	}

	private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();

		children.forEach(subCategory -> {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

			listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, newSubLevel);
		});
	}

//	public category save(Category category) {
//		boolean isUpdatingcategory = (category.getId() != null);
//
//		if (isUpdatingcategory) {
//			Category existingcategory = catRepo.findById(category.getId()).get();
//
//			if (category.getPassword().isEmpty()) {
//				category.setPassword(existingcategory.getPassword());
//			} else {
//				encodePassword(category);
//			}
//		}
//		return catRepo.save(category);
//	}

//	public Category updateAccount(Category categoryInForm) {
//		Category categoryInDB = catRepo.findById(categoryInForm.getId()).get();
//
//		if (!categoryInForm.getPassword().isEmpty()) {
//			categoryInDB.setPassword(categoryInForm.getPassword());
//			encodePassword(categoryInDB);
//		}
//
//		if (categoryInForm.getPhotos() != null) {
//			categoryInDB.setPhotos(categoryInForm.getPhotos());
//		}
//
//		categoryInDB.setFirstName(categoryInForm.getFirstName());
//		categoryInDB.setLastName(categoryInForm.getLastName());
//
//		return catRepo.save(categoryInDB);
//	}

	public Category get(Integer id) throws CategoryNotFoundException {
		try {
			return catRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CategoryNotFoundException("Could not find any category with ID " + id + "!");
		}
	}
	
	public String checkUnique(Integer id, String name, String alias) {
		boolean isCreatingNew = (id == null || id == 0);
		
		Category catByName = catRepo.findByName(name);
		
		if (isCreatingNew) {
			if (catByName != null) {
				return "DuplicateName";
			} else {
				Category catByAlias = catRepo.findByAlias(alias);
				if (catByAlias != null) {
					return "DuplicateAlias";
				}
			}
		}
		
		return "OK";
	}

//	public void delete(Integer id) throws categoryNotFoundException {
//		Long countById = catRepo.countById(id);
//		if (countById == null || countById == 0) {
//			throw new categoryNotFoundException("Could not find any category with ID " + id);
//		}
//
//		catRepo.deleteById(id);
//	}

	public void updatecategoryEnabledStatus(Integer id, boolean enabled) {
		catRepo.updateEnabledStatus(id, enabled);
	}
}
