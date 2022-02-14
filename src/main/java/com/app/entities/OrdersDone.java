package com.app.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



@Entity
@Table(name = "orders")
public class OrdersDone {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long myOrderId;
	private String orderId;
	private String amount;
	private String recieptNum;
	private String status;
	private String time;
	@ManyToOne
	private User user;
	private String paymentId;
	public Long getMyOrderId() {
		return myOrderId;
	}
	public void setMyOrderId(Long myOrderId) {
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
	public String getRecieptNum() {
		return recieptNum;
	}
	public void setRecieptNum(String recieptNum) {
		this.recieptNum = recieptNum;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
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
