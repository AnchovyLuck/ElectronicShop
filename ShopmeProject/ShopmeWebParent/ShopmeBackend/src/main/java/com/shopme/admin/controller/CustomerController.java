package com.shopme.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.exception.CustomerNotFoundException;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.service.CustomerService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

@Controller
public class CustomerController {
	@Autowired
	private CustomerService service;

	@GetMapping("/customers")
	public String listFirstPage(Model model) {
		return "redirect:/customers/page/1?sortField=id&sortDir=asc";
	}

	@GetMapping("/customers/page/{pageNum}")
	public String listByPage(@PagingAndSortingParam(listName = "listCustomers", moduleURL = "/customers") PagingAndSortingHelper helper, @PathVariable(name = "pageNum") int pageNum) {
		service.listByPage(pageNum, helper);
		return "customers/customers";
	}

	@GetMapping("/customers/{id}/enabled/{status}")
	public String updateCustomerEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes ra) {
		service.updateCustomerEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The customer ID " + id + " has been " + status;
		ra.addFlashAttribute("message", message);

		return "redirect:/customers/page/1?sortField=id&sortDir=asc";
	}

	@GetMapping("/customers/detail/{id}")
	public String viewCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Customer customer = service.get(id);
			model.addAttribute("customer", customer);
			return "customers/customer_detail_modal";
		} catch (CustomerNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/customers/page/1?sortField=id&sortDir=asc";
		}
	}

	@GetMapping("/customers/edit/{id}")
	public String editCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Customer customer = service.get(id);
			List<Country> countries = service.listAllCountries();

			model.addAttribute("listCountries", countries);
			model.addAttribute("customer", customer);
			model.addAttribute("pageTitle", String.format("Edit Customer (ID: %d", id));

			return "customers/customer_form";

		} catch (CustomerNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/customers/page/1?sortField=id&sortDir=asc";
		}
	}

	@PostMapping("/customers/save")
	public String saveCustomer(Customer customer, Model model, RedirectAttributes ra) {
		service.save(customer);

		String message = "The customer ID " + customer.getId() + " has been updated successfully!";

		ra.addFlashAttribute("message", message);

		return "redirect:/customers/page/1?sortField=id&sortDir=asc";
	}

	@GetMapping("/customers/delete/{id}")
		public String deleteCustomer(@PathVariable(name = "id") Integer id, RedirectAttributes ra) {
			try {
				service.delete(id);
				String message = "The customer ID " + id + " has been deleted successfully!";
				ra.addFlashAttribute("message", message);
			} catch (CustomerNotFoundException e) {
				ra.addFlashAttribute("message", e.getMessage());
			}
			
			return "redirect:/customers";
		}

}
