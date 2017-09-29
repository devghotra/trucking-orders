package com.jassboys.trucking.orders.model;

import java.io.Serializable;

public class OrdersServiceRequest implements Serializable {

	private static final long serialVersionUID = 5919871743941682945L;
	
	private String username;
	private String password;
	private String truckHaulerNumber;
	private String driverName;
	private boolean adminUserFlag;
	private String primeCarrier;
	private String customer;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTruckHaulerNumber() {
		return truckHaulerNumber;
	}
	public void setTruckHaulerNumber(String truckHaulerNumber) {
		this.truckHaulerNumber = truckHaulerNumber;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public boolean isAdminUserFlag() {
		return adminUserFlag;
	}
	public void setAdminUserFlag(boolean adminUserFlag) {
		this.adminUserFlag = adminUserFlag;
	}
	public String getPrimeCarrier() {
		return primeCarrier;
	}
	public void setPrimeCarrier(String primeCarrier) {
		this.primeCarrier = primeCarrier;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	

}
