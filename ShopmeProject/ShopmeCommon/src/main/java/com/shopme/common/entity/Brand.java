package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "brands")
public class Brand {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 45, unique = true)
	private String name;

	@Column(nullable = false, length = 128)
	private String logo;

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "brands_categories", joinColumns = @JoinColumn(name = "brand_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private Set<Category> categories = new HashSet<>();
	
	@OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Product> products = new HashSet<>();

	public Brand() {
	}

	public Brand(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Brand(String name) {
		this.name = name;
		this.logo = "brand-logo.png";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}
	
	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	
	public void addProduct(Product product) {
		this.products.add(product);
		product.setBrand(this);
	}
	
	public void removeProduct(Product product) {
		this.products.remove(product);
		product.setBrand(null);
	}

	@Override
	public String toString() {
		return "Brand [id=" + id + ", name=" + name + ", categories=" + categories + "]";
	}

	@Transient
	public String getLogoPath() {
		if (this.id == null || this.logo == null) {
			return "/images/image-thumbnail.png";
		}

		return "/brand-logos/" + this.id + "/" + this.logo;
	}
	
	
}
