package com.jassboys.trucking.orders.model;

import java.io.Serializable;
import java.sql.Date;

public class Order implements Serializable {

	private static final long serialVersionUID = 2631035782331242790L;
	
	private long orderId;
	private String orderDate;
	private String tagNumber;
	private String primeCarrier;
	private String customer;
	private String jobNumber;
	private int numOfTolls;
	private double bridgeToll;
	private float loads;
	private double ratePerLoad;
	private float hours;
	private double ratePerHour;
	private float tons;
	private double ratePerTon;
	private double entryTotal;
	private String paymentStatus;
	private String paidToSub;
	private Date creationDateTime;
	private Date modDateTime;
	private String modifiedByUser;
	private String createdByUser;
	private String truckHaulerNumber;
	private String driverName;
	private String notes;
	private double brokagePercentage;
	private double netEntry;
	private float standbyHours;
	private double ratePerStandbyHr;
	
	private String toOrderDate;
	private String fromOrderDate;
	
	private String orderIdRef;
	private String loadsNRate;
	private String hoursNRate;
	private String tonsNRate;
	private String standbyHrsNRate;
	private String shift;
	
	// payment fields
	private float paymentHours;
	private double paymentPerHour;
	private float paymentTons;
	private double paymentPerTon;
	private float paymentLoads;
	private double paymentPerLoad;
	private double otherPayment;
	private double totalPayment;
	private double bridgeTollPayment;
	private String otherPaymentDesc;
	private double otherCharges;
	
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getTagNumber() {
		return tagNumber;
	}
	public void setTagNumber(String tagNumber) {
		this.tagNumber = tagNumber;
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
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public double getRatePerLoad() {
		return ratePerLoad;
	}
	public void setRatePerLoad(double ratePerLoad) {
		this.ratePerLoad = ratePerLoad;
	}
	public double getRatePerHour() {
		return ratePerHour;
	}
	public void setRatePerHour(double ratePerHour) {
		this.ratePerHour = ratePerHour;
	}
	public float getLoads() {
		return loads;
	}
	public void setLoads(float loads) {
		this.loads = loads;
	}
	public float getHours() {
		return hours;
	}
	public void setHours(float hours) {
		this.hours = hours;
	}
	public float getTons() {
		return tons;
	}
	public void setTons(float tons) {
		this.tons = tons;
	}
	public double getRatePerTon() {
		return ratePerTon;
	}
	public void setRatePerTon(double ratePerTon) {
		this.ratePerTon = ratePerTon;
	}
	public double getEntryTotal() {
		return entryTotal;
	}
	public void setEntryTotal(double entryTotal) {
		this.entryTotal = entryTotal;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getPaidToSub() {
		return paidToSub;
	}
	public void setPaidToSub(String paidToSub) {
		this.paidToSub = paidToSub;
	}
	public Date getCreationDateTime() {
		return creationDateTime;
	}
	public void setCreationDateTime(Date creationDateTime) {
		this.creationDateTime = creationDateTime;
	}
	public Date getModDateTime() {
		return modDateTime;
	}
	public void setModDateTime(Date modDateTime) {
		this.modDateTime = modDateTime;
	}
	public String getModifiedByUser() {
		return modifiedByUser;
	}
	public void setModifiedByUser(String modifiedByUser) {
		this.modifiedByUser = modifiedByUser;
	}
	public String getCreatedByUser() {
		return createdByUser;
	}
	public void setCreatedByUser(String createdByUser) {
		this.createdByUser = createdByUser;
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
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getToOrderDate() {
		return toOrderDate;
	}
	public void setToOrderDate(String toOrderDate) {
		this.toOrderDate = toOrderDate;
	}
	public String getFromOrderDate() {
		return fromOrderDate;
	}
	public void setFromOrderDate(String fromOrderDate) {
		this.fromOrderDate = fromOrderDate;
	}
	public String getOrderIdRef() {
		return orderIdRef;
	}
	public void setOrderIdRef(String orderIdRef) {
		this.orderIdRef = orderIdRef;
	}
	public String getLoadsNRate() {
		return loadsNRate;
	}
	public void setLoadsNRate(String loadsNRate) {
		this.loadsNRate = loadsNRate;
	}
	public String getHoursNRate() {
		return hoursNRate;
	}
	public void setHoursNRate(String hoursNRate) {
		this.hoursNRate = hoursNRate;
	}
	public String getTonsNRate() {
		return tonsNRate;
	}
	public void setTonsNRate(String tonsNRate) {
		this.tonsNRate = tonsNRate;
	}
	public double getBridgeToll() {
		return bridgeToll;
	}
	public void setBridgeToll(double bridgeToll) {
		this.bridgeToll = bridgeToll;
	}
	public int getNumOfTolls() { return numOfTolls; }
	public void setNumOfTolls(int numOfTolls) { this.numOfTolls = numOfTolls; }
	public double getBrokagePercentage() {
		return brokagePercentage;
	}
	public void setBrokagePercentage(double brokagePercentage) {
		this.brokagePercentage = brokagePercentage;
	}
	public double getNetEntry() {
		return netEntry;
	}
	public void setNetEntry(double netEntry) {
		this.netEntry = netEntry;
	}
	public float getStandbyHours() {
		return standbyHours;
	}
	public void setStandbyHours(float standbyHours) {
		this.standbyHours = standbyHours;
	}
	public double getRatePerStandbyHr() {
		return ratePerStandbyHr;
	}
	public void setRatePerStandbyHr(double ratePerStandbyHr) {
		this.ratePerStandbyHr = ratePerStandbyHr;
	}
	public String getStandbyHrsNRate() {
		return standbyHrsNRate;
	}
	public void setStandbyHrsNRate(String standbyHrsNRate) {
		this.standbyHrsNRate = standbyHrsNRate;
	}
	public float getPaymentHours() {
		return paymentHours;
	}
	public void setPaymentHours(float paymentHours) {
		this.paymentHours = paymentHours;
	}
	public double getPaymentPerHour() {
		return paymentPerHour;
	}
	public void setPaymentPerHour(double paymentPerHour) {
		this.paymentPerHour = paymentPerHour;
	}
	public float getPaymentTons() {
		return paymentTons;
	}
	public void setPaymentTons(float paymentTons) {
		this.paymentTons = paymentTons;
	}
	public double getPaymentPerTon() {
		return paymentPerTon;
	}
	public void setPaymentPerTon(double paymentPerTon) {
		this.paymentPerTon = paymentPerTon;
	}
	public float getPaymentLoads() {
		return paymentLoads;
	}
	public void setPaymentLoads(float paymentLoads) {
		this.paymentLoads = paymentLoads;
	}
	public double getPaymentPerLoad() {
		return paymentPerLoad;
	}
	public void setPaymentPerLoad(double paymentPerLoad) {
		this.paymentPerLoad = paymentPerLoad;
	}
	public double getOtherPayment() {
		return otherPayment;
	}
	public void setOtherPayment(double otherPayment) {
		this.otherPayment = otherPayment;
	}
	public double getTotalPayment() {
		return totalPayment;
	}
	public void setTotalPayment(double totalPayment) {
		this.totalPayment = totalPayment;
	}
	public double getBridgeTollPayment() {
		return bridgeTollPayment;
	}
	public void setBridgeTollPayment(double bridgeTollPayment) {
		this.bridgeTollPayment = bridgeTollPayment;
	}
	public String getOtherPaymentDesc() {
		return otherPaymentDesc;
	}
	public void setOtherPaymentDesc(String otherPaymentDesc) {
		this.otherPaymentDesc = otherPaymentDesc;
	}
	public double getOtherCharges() {
		return otherCharges;
	}
	public void setOtherCharges(double otherCharges) {
		this.otherCharges = otherCharges;
	}
	public String getShift() {
		return shift;
	}
	public void setShift(String shift) {
		this.shift = shift;
	}
	
}
