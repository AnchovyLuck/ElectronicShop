package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.data.domain.Pageable;

import com.shopme.admin.repository.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import nl.jqno.equalsverifier.EqualsVerifier;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private UserRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User anchovy = new User("anchovyluck20@gmail.com", "Anchovy.shopme2023@", "Anchovy", "Luck");
		anchovy.addRole(roleAdmin);

		User savedUser = repo.save(anchovy);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateNewUserWithTwoRole() {
		User NgocTram = new User("tramb9531@gmail.com", "tram.shopme2023@", "Ngoc Tram", "Nguyen");
		Role roleEditor = entityManager.find(Role.class, 3);
		Role roleAssistant = entityManager.find(Role.class, 5);

		NgocTram.addRole(roleEditor);
		NgocTram.addRole(roleAssistant);
		NgocTram.setEnabled(true);

		User savedUser = repo.save(NgocTram);

		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(System.out :: println);
		
		assertThat(listUsers).isNotEmpty();
	}

	@Test
	public void testGetUserById() {
		User userAnchovy = repo.findById(1).get();
		System.out.println(userAnchovy);
		assertThat(userAnchovy).isNotNull();
	}

	@Test
	public void testUpdateUserDetails() {
		User userAnchovy = repo.findById(1).get();
		userAnchovy.setEnabled(true);
		userAnchovy.setEmail("anchovyluck@gmail.com");

		repo.save(userAnchovy);
	}

	@Test
	public void testUpdateUserRoles() {
		User userAnchovy = repo.findById(4).get();
		Role roleEditor = new Role(3);
		Role roleSalesPerson = new Role(2);

		userAnchovy.getRoles().remove(roleEditor);
		userAnchovy.addRole(roleSalesPerson);

		repo.save(userAnchovy);
	}

	@Test
	public void testDeleteUser() {
		Integer userId = 4;
		repo.deleteById(userId);

		User deletedUser = repo.findById(userId).get();

		assertNull(deletedUser);
	}

	@Test
	public void testGetUserByEmail() {
		String email = "tramb9531@gmail.com";
		User userByEmail = repo.getUserByEmail(email);

		assertThat(userByEmail).isNotNull();

	}

	@Test
	public void testCountById() {
		Integer id = 1;
		Long countById = repo.countById(id);

		assertThat(countById).isNotNull();
	}

	@Test
	public void testDisableUser() {
		Integer id = 1;
		repo.updateEnabledStatus(id, false);
	}

	@Test
	public void testEnableUser() {
		Integer id = 3;
		repo.updateEnabledStatus(id, true);
	}

	@Test
	public void testListFirstPage() {
		int pageNumber = 1;
		int pageSize = 4;

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);

		List<User> listUsers = page.getContent();

		listUsers.forEach(user -> System.out.println(user));

		assertThat(listUsers.size()).isEqualTo(pageSize);
	}

	@Test
	public void testSearchUsers() {
		String keyword = "bruce";
		int pageNumber = 0;
		int pageSize = 4;

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(keyword, pageable);

		List<User> listUsers = page.getContent();

		listUsers.forEach(user -> System.out.println(user));

		assertThat(listUsers.size()).isGreaterThan(0);
	}

	@Test
	public void equalsContract() {
		EqualsVerifier.forClass(User.class).verify();
	}
}
