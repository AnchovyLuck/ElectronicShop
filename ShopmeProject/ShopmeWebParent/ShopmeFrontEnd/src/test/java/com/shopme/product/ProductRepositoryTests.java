package com.shopme.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shopme.common.entity.Product;
import com.shopme.repository.ProductRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ProductRepositoryTests {
	
	@Autowired 
	private ProductRepository repo;
	
	@Test 
	public void testFindByAlias() {
		String alias = "pelican-1200-case";
		
		Product product = repo.findByAlias(alias);
		
		assertThat(product).isNotNull();
		assertThat(product.getId()).isGreaterThan(0);
	}
}
