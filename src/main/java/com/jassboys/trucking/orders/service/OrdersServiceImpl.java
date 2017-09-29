package com.jassboys.trucking.orders.service;

import com.jassboys.trucking.orders.dao.OrdersServiceDao;
import com.jassboys.trucking.orders.model.Order;
import com.jassboys.trucking.orders.model.OrdersServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrdersServiceImpl {
	
	@Autowired
	OrdersServiceDao ordersServiceDao;
	
	public OrdersServiceResponse authorize(String username, String password) throws Exception{
		return ordersServiceDao.authorize(username, password);
	}
	
	public List<Order> getOrderList(String authtoken) throws Exception{
		return ordersServiceDao.getOrderList(authtoken);
	}
	
	public Order getOrderDetails(long orderId, String authtoken) throws Exception{
		return ordersServiceDao.getOrderDetails(orderId, authtoken);
	}
	
	public Order addOrder(Order order, String authtoken) throws Exception{
		return ordersServiceDao.addOrder(order, authtoken);
	}
	
	public Order updateOrder(Order order, String authtoken) throws Exception{
		return ordersServiceDao.updateOrder(order, authtoken);
	}
	
	public List<Order> searchOrders(Order order, String authtoken) throws Exception{
		return ordersServiceDao.searchOrders(order, authtoken);
	}
	
	public List<String> getTruckHaulerDetails(String authtoken) throws Exception{
		return ordersServiceDao.getTruckHaulerDetails(authtoken);
	}

	public List<String> getDriverDetails(String authtoken) throws Exception{
		return ordersServiceDao.getDriverDetails(authtoken);
	}
	
	public List<String> getCustomerDetails(String authtoken) throws Exception{
		return ordersServiceDao.getCustomerDetails(authtoken);
	}
	
	public void addTruckHauler(String truckHaulerNumber, String authtoken) throws Exception{
		ordersServiceDao.addTruckHauler(truckHaulerNumber, authtoken);
	}
	
	public void removeTruckHauler(String truckHaulerNumber, String authtoken) throws Exception{
		ordersServiceDao.removeTruckHauler(truckHaulerNumber, authtoken);
	}
	
	public void addDriver(String driverName, String authtoken) throws Exception{
		ordersServiceDao.addDriver(driverName, authtoken);
	}
	
	public void removeDriver(String driverName, String authtoken) throws Exception{
		ordersServiceDao.removeDriver(driverName, authtoken);
	}
	
	public void addCustomer(String driverName, String authtoken) throws Exception{
		ordersServiceDao.addCustomer(driverName, authtoken);
	}
	
	public void removeCustomer(String driverName, String authtoken) throws Exception{
		ordersServiceDao.removeCustomer(driverName, authtoken);
	}
	
	public void addUser(String username, String password, boolean adminUserFlag, String authtoken) throws Exception{
		ordersServiceDao.addUser(username, password, adminUserFlag, authtoken);
	}
	
	public void deactivateUser(String username, String authtoken) throws Exception{
		ordersServiceDao.deactivateUser(username, authtoken);
	}
	public List<String> getPrimeCarriers(String authtoken) throws Exception{
		return ordersServiceDao.getPrimeCarriers(authtoken);
	}
	public void addPrimeCarrier(String pcName, String authtoken) throws Exception{
		ordersServiceDao.addPrimeCarrier(pcName, authtoken);
	}
	public void removePrimeCarrier(String pcName, String authtoken) throws Exception{
		ordersServiceDao.removePrimeCarrier(pcName, authtoken);
	}
	public void deleteOrder(String orderId, String authtoken) throws Exception{
		ordersServiceDao.deleteOrder(orderId, authtoken);
	}
}
