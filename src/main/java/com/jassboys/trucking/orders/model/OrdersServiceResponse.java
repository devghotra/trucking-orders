package com.jassboys.trucking.orders.model;

import java.io.Serializable;
import java.util.List;

public class OrdersServiceResponse implements Serializable {

	private static final long serialVersionUID = -7793768106494854750L;
	
	private int responseCode;
	private String responseDescription;
	private Order order;
	private String authToken;
	private boolean adminUserFlag;
	private List<Order> orderList;
	private List<String> truckHaulerNumbers;	
	private List<String> drivers;
	private List<String> primeCarriers;
	private List<String> customers;
	
	
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseDescription() {
		return responseDescription;
	}
	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public List<Order> getOrderList() {
		return orderList;
	}
	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}
	public List<String> getTruckHaulerNumbers() {
		return truckHaulerNumbers;
	}
	public void setTruckHaulerNumbers(List<String> truckHaulerNumbers) {
		this.truckHaulerNumbers = truckHaulerNumbers;
	}
	public List<String> getDrivers() {
		return drivers;
	}
	public void setDrivers(List<String> drivers) {
		this.drivers = drivers;
	}
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	public boolean isAdminUserFlag() {
		return adminUserFlag;
	}
	public void setAdminUserFlag(boolean adminUserFlag) {
		this.adminUserFlag = adminUserFlag;
	}
	public List<String> getPrimeCarriers() {
		return primeCarriers;
	}
	public void setPrimeCarriers(List<String> primeCarriers) {
		this.primeCarriers = primeCarriers;
	}
	public List<String> getCustomers() {
		return customers;
	}
	public void setCustomers(List<String> customers) {
		this.customers = customers;
	}
	
}
