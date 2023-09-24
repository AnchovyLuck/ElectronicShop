package com.shopme.admin.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.order.Order;

public interface OrderRepository extends CrudRepository<Order, Integer>, PagingAndSortingRepository<Order, Integer> {
	
}
