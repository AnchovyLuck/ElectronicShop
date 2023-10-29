package com.shopme.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.repository.StateRepository;
import com.shopme.common.dto.StateDTO;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@RestController
public class StateRestController {

	@Autowired
	private StateRepository repo;

	@GetMapping("/settings/list_states_by_country/{id}")
	public List<StateDTO> listAll(@PathVariable("id") Integer countryId) {
		List<State> states = repo.findByCountryOrderByNameAsc(new Country(countryId));
		List<StateDTO> result = new ArrayList<>();
		
		states.forEach(state -> result.add(new StateDTO(state.getId(), state.getName())));
		
		return result;
	}
}
