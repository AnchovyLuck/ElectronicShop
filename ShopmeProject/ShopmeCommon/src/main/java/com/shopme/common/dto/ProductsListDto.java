package com.shopme.common.dto;

import java.util.ArrayList;
import java.util.List;

import com.shopme.common.entity.Product;

public class ProductsListDto {
	private List<Product> products;
	
	public ProductsListDto() {
		this.products = new ArrayList<>();
	}
	
	public ProductsListDto(List<Product> products) {
		this.products = products;
	}
	
	public void addProduct(Product product) {
		this.products.add(product);
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
}
