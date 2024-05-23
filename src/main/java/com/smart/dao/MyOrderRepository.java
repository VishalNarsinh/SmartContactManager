package com.smart.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.entities.MyOrder;

public interface MyOrderRepository extends JpaRepository<MyOrder, Integer>{
	public MyOrder findByOrderId(String orderId);
}
