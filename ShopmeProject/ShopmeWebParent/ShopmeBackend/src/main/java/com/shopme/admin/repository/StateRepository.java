package com.shopme.admin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

public interface StateRepository extends CrudRepository<State, Integer> {
	public List<State> findByCountryOrderByNameAsc(Country country);
	
	@Query("SELECT s FROM State s JOIN FETCH s.country WHERE s.id = ?1")
	public Optional<State> findById(Integer stateId);
}
