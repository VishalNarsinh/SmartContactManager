package com.smart.entities;

import com.razorpay.Order;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class MyOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long myOrderId;
	private String orderId;
	private String amount;
	private String receipt;
	private String status;
	@ManyToOne
	private User user;
	private String paymentId;
	public MyOrder(Order order,User user) {
		this.orderId=order.get("id");
		this.amount=order.get("amount").toString();
		this.status="created";
		this.user = user;
		this.receipt=order.get("receipt");
	}
	public MyOrder(long myOrderId, String orderId, String amount, String receipt, String status, User user,
			String paymentId) {
		super();
		this.myOrderId = myOrderId;
		this.orderId = orderId;
		this.amount = amount;
		this.receipt = receipt;
		this.status = status;
		this.user = user;
		this.paymentId = paymentId;
	}

	public MyOrder() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "MyOrder [myOrderId=" + myOrderId + ", orderId=" + orderId + ", amount=" + amount + ", receipt="
				+ receipt + ", status=" + status + ", user=" + user + ", paymentId=" + paymentId + "]";
	}

	public long getMyOrderId() {
		return myOrderId;
	}

	public void setMyOrderId(long myOrderId) {
		this.myOrderId = myOrderId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	
	
	
}
