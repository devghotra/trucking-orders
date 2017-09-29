package com.jassboys.trucking.orders.controller;

import com.jassboys.trucking.orders.model.Order;
import com.jassboys.trucking.orders.model.OrdersServiceRequest;
import com.jassboys.trucking.orders.model.OrdersServiceResponse;
import com.jassboys.trucking.orders.service.OrdersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
@RequestMapping("/services/orders")
public class OrdersController {
	
	@Autowired
	OrdersServiceImpl ordersServiceImpl;
	
	@RequestMapping(value="/login",
			method = RequestMethod.POST,
    		consumes = {"application/json"}, 
    		produces = {"application/json"})
    @ResponseBody
	public OrdersServiceResponse authorize(@RequestBody OrdersServiceRequest orderSvcReq){
		OrdersServiceResponse serviceResponse = new OrdersServiceResponse();
		try {
			if(orderSvcReq.getUsername() == null || orderSvcReq.getUsername().trim().isEmpty()
					|| orderSvcReq.getPassword() == null || orderSvcReq.getPassword().trim().isEmpty()){
				throw new Exception("Invalid credentials");
			}
			serviceResponse = ordersServiceImpl.authorize(orderSvcReq.getUsername().trim(), orderSvcReq.getPassword().trim());
			serviceResponse.setResponseCode(200);
		} catch (Exception e) {
			serviceResponse.setResponseCode(500);
			serviceResponse.setResponseDescription(e.getMessage());
		}
		return serviceResponse;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
	 public OrdersServiceResponse getOrder(HttpServletRequest request,
			 @RequestParam(value = "at", required = true) String authtoken,
			 @RequestParam(value="order-id", required=false) String orderIdStr,
			 @RequestParam(value="tag", required=false) String tagNumber){
		OrdersServiceResponse serviceResponse = new OrdersServiceResponse();
		try {
			if(tagNumber != null){
				Order order = new Order();
				order.setTagNumber(tagNumber);
				List<Order> orderList = ordersServiceImpl.searchOrders(order, authtoken);
				if(orderList != null && orderList.size() > 0){
					serviceResponse.setOrder(orderList.get(0));
				} else
					throw new Exception("No Order Found");
			} else{
				if(orderIdStr == null)
					serviceResponse.setOrderList(ordersServiceImpl.getOrderList(authtoken));
				else{
					long orderId = 0;
					try {
						orderId = Long.parseLong(orderIdStr);
					} catch (NumberFormatException nfe) {
						throw new Exception("Invalid Order Number");
					}
					serviceResponse.setOrder(ordersServiceImpl.getOrderDetails(orderId, authtoken));
				}
			}
			serviceResponse.setResponseCode(200);
		} catch (Exception e) {
			serviceResponse.setResponseCode(500);
			serviceResponse.setResponseDescription(e.getMessage());
		}
		
		return serviceResponse;
	 }
	
	@RequestMapping(method = RequestMethod.POST,
    		consumes = {"application/json"}, 
    		produces = {"application/json"})
    @ResponseBody
	public OrdersServiceResponse addOrder(@RequestBody Order order, @RequestParam(value = "at", required = true) String authtoken){
		OrdersServiceResponse serviceResponse = new OrdersServiceResponse();
		try {
			if(order.getOrderId() > 0)
				ordersServiceImpl.updateOrder(order, authtoken);
			else
				ordersServiceImpl.addOrder(order, authtoken);
			serviceResponse.setResponseCode(200);
		} catch (Exception e) {
			serviceResponse.setResponseCode(500);
			serviceResponse.setResponseDescription(e.getMessage());
		}
		return serviceResponse;
	}
	
	@RequestMapping(value="/search",
			method = RequestMethod.POST,
    		consumes = {"application/json"}, 
    		produces = {"application/json"})
    @ResponseBody
	public OrdersServiceResponse searchOrder(@RequestBody Order order, @RequestParam(value = "at", required = true) String authtoken){
		OrdersServiceResponse serviceResponse = new OrdersServiceResponse();
		try {
			serviceResponse.setOrderList(ordersServiceImpl.searchOrders(order, authtoken));
			serviceResponse.setResponseCode(200);
		} catch (Exception e) {
			serviceResponse.setResponseCode(500);
			serviceResponse.setResponseDescription(e.getMessage());
		}
		return serviceResponse;
	}
	
	@RequestMapping(value = "/truck-hauler-numbers", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
	 public OrdersServiceResponse getTruckHaulerDetails(HttpServletRequest request, @RequestParam(value = "at", required = true) String authtoken){
		OrdersServiceResponse serviceResponse = new OrdersServiceResponse();
		try {
			serviceResponse.setTruckHaulerNumbers(ordersServiceImpl.getTruckHaulerDetails(authtoken));
			serviceResponse.setResponseCode(200);
		} catch (Exception e) {
			serviceResponse.setResponseCode(500);
			serviceResponse.setResponseDescription(e.getMessage());
		}
		
		return serviceResponse;
	 }
	
	@RequestMapping(value = "/truck-hauler-numbers", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
	 public OrdersServiceResponse addTruckHauler(HttpServletRequest request,
			 @RequestParam(value = "at", required = true) String authtoken,
			 @RequestBody OrdersServiceRequest ordersSvcReq){
		OrdersServiceResponse serviceResponse = new OrdersServiceResponse();
		try {
			if(ordersSvcReq.getTruckHaulerNumber() == null)
				throw new Exception("Invalid Truck/Sub-Hauler Number");
			ordersServiceImpl.addTruckHauler(ordersSvcReq.getTruckHaulerNumber(), authtoken);
			serviceResponse.setResponseCode(200);
		} catch (Exception e) {
			serviceResponse.setResponseCode(500);
			serviceResponse.setResponseDescription(e.getMessage());
		}
		
		return serviceResponse;
	 }
	
	@RequestMapping(value = "/remove-truck", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
	 public OrdersServiceResponse removeTruck(HttpServletRequest request,
			 @RequestParam(value = "at", required = true) String authtoken,
			 @RequestBody OrdersServiceRequest ordersSvcReq){
		OrdersServiceResponse serviceResponse = new OrdersServiceResponse();
		try {
			ordersServiceImpl.removeTruckHauler(ordersSvcReq.getTruckHaulerNumber(), authtoken);
			serviceResponse.setResponseCode(200);
		} catch (Exception e) {
			serviceResponse.setResponseCode(500);
			serviceResponse.setResponseDescription(e.getMessage());
		}
		
		return serviceResponse;
	 }
	
	@RequestMapping(value = "/drivers", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
	 public OrdersServiceResponse getDriverDetails(HttpServletRequest request, @RequestParam(value = "at", required = true) String authtoken){
		OrdersServiceResponse serviceResponse = new OrdersServiceResponse();
		try {
			serviceResponse.setDrivers(ordersServiceImpl.getDriverDetails(authtoken));
			serviceResponse.setResponseCode(200);
		} catch (Exception e) {
			serviceResponse.setResponseCode(500);
			serviceResponse.setResponseDescription(e.getMessage());
		}
		
		return serviceResponse;
	 }
	
	@RequestMapping(value = "/drivers", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
	 public OrdersServiceResponse addDriver(HttpServletRequest request,
			 @RequestParam(value = "at", required = true) String authtoken,
			 @RequestBody OrdersServiceRequest ordersSvcReq){
		OrdersServiceResponse serviceResponse = new OrdersServiceResponse();
		try {
			if(ordersSvcReq.getDriverName() == null)
				throw new Exception("Invalid Driver Name");
			ordersServiceImpl.addDriver(ordersSvcReq.getDriverName(), authtoken);
			serviceResponse.setResponseCode(200);
		} catch (Exception e) {
			serviceResponse.setResponseCode(500);
			serviceResponse.setResponseDescription(e.getMessage());
		}
		
		return serviceResponse;
	 }
	
	@RequestMapping(value = "/remove-driver", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
	 public OrdersServiceResponse removeDriver(HttpServletRequest request,
			 @RequestParam(value = "at", required = true) String authtoken,
			 @RequestBody OrdersServiceRequest ordersSvcReq){
		OrdersServiceResponse serviceResponse = new OrdersServiceResponse();
		try {
			ordersServiceImpl.removeDriver(ordersSvcReq.getDriverName(), authtoken);
			serviceResponse.setResponseCode(200);
		} catch (Exception e) {
			serviceResponse.setResponseCode(500);
			serviceResponse.setResponseDescription(e.getMessage());
		}
		
		return serviceResponse;
	 }
	
	@RequestMapping(value = "/add-user", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
	 public OrdersServiceResponse addUser(HttpServletRequest request,
			 @RequestParam(value = "at", required = true) String authtoken,
			 @RequestBody OrdersServiceRequest ordersSvcReq){
		OrdersServiceResponse serviceResponse = new OrdersServiceResponse();
		try {
			ordersServiceImpl.addUser(ordersSvcReq.getUsername(), ordersSvcReq.getPassword(), ordersSvcReq.isAdminUserFlag(), authtoken);
			serviceResponse.setResponseCode(200);
		} catch (Exception e) {
			serviceResponse.setResponseCode(500);
			serviceResponse.setResponseDescription(e.getMessage());
		}
		
		return serviceResponse;
	 }
	@RequestMapping(value = "/deactivate-user", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
	 public OrdersServiceResponse deactivateUser(HttpServletRequest request,
			 @RequestParam(value = "at", required = true) String authtoken,
			 @RequestBody OrdersServiceRequest ordersSvcReq){
		OrdersServiceResponse serviceResponse = new OrdersServiceResponse();
		try {
			ordersServiceImpl.deactivateUser(ordersSvcReq.getUsername(), authtoken);
			serviceResponse.setResponseCode(200);
		} catch (Exception e) {
			serviceResponse.setResponseCode(500);
			serviceResponse.setResponseDescription(e.getMessage());
		}
		
		return serviceResponse;
	 }
	
	@RequestMapping(value = "/prime-carrier", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
	 public OrdersServiceResponse getPrimeCarriers(HttpServletRequest request, @RequestParam(value = "at", required = true) String authtoken){
		OrdersServiceResponse serviceResponse = new OrdersServiceResponse();
		try {
			serviceResponse.setPrimeCarriers(ordersServiceImpl.getPrimeCarriers(authtoken));
			serviceResponse.setResponseCode(200);
		} catch (Exception e) {
			serviceResponse.setResponseCode(500);
			serviceResponse.setResponseDescription(e.getMessage());
		}
		
		return serviceResponse;
	 }
	
	@RequestMapping(value = "/prime-carrier", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
	 public OrdersServiceResponse addPrimeCarrier(HttpServletRequest request,
			 @RequestParam(value = "at", required = true) String authtoken,
			 @RequestBody OrdersServiceRequest ordersSvcReq){
		OrdersServiceResponse serviceResponse = new OrdersServiceResponse();
		try {
			if(ordersSvcReq.getPrimeCarrier() == null)
				throw new Exception("Invalid Prime Carrier");
			ordersServiceImpl.addPrimeCarrier(ordersSvcReq.getPrimeCarrier(), authtoken);
			serviceResponse.setResponseCode(200);
		} catch (Exception e) {
			serviceResponse.setResponseCode(500);
			serviceResponse.setResponseDescription(e.getMessage());
		}
		
		return serviceResponse;
	 }
	
	@RequestMapping(value = "/remove-prime-carrier", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
	 public OrdersServiceResponse removePrimeCarrier(HttpServletRequest request,
			 @RequestParam(value = "at", required = true) String authtoken,
			 @RequestBody OrdersServiceRequest ordersSvcReq){
		OrdersServiceResponse serviceResponse = new OrdersServiceResponse();
		try {
			ordersServiceImpl.removePrimeCarrier(ordersSvcReq.getPrimeCarrier(), authtoken);
			serviceResponse.setResponseCode(200);
		} catch (Exception e) {
			serviceResponse.setResponseCode(500);
			serviceResponse.setResponseDescription(e.getMessage());
		}
		
		return serviceResponse;
	 }
	
	@RequestMapping(value = "/delete-order", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
	 public OrdersServiceResponse deleteOrder(HttpServletRequest request,
			 @RequestParam(value = "at", required = true) String authtoken,
			 @RequestParam(value = "order-id", required = true) String orderId){
		OrdersServiceResponse serviceResponse = new OrdersServiceResponse();
		try {
			ordersServiceImpl.deleteOrder(orderId, authtoken);
			serviceResponse.setResponseCode(200);
		} catch (Exception e) {
			serviceResponse.setResponseCode(500);
			serviceResponse.setResponseDescription(e.getMessage());
		}
		
		return serviceResponse;
	 }
	
	@RequestMapping(value = "/customers", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
	 public OrdersServiceResponse getCustomerDetails(HttpServletRequest request, @RequestParam(value = "at", required = true) String authtoken){
		OrdersServiceResponse serviceResponse = new OrdersServiceResponse();
		try {
			serviceResponse.setCustomers(ordersServiceImpl.getCustomerDetails(authtoken));
			serviceResponse.setResponseCode(200);
		} catch (Exception e) {
			serviceResponse.setResponseCode(500);
			serviceResponse.setResponseDescription(e.getMessage());
		}
		
		return serviceResponse;
	 }
	
	@RequestMapping(value = "/customers", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
	 public OrdersServiceResponse addCustomer(HttpServletRequest request,
			 @RequestParam(value = "at", required = true) String authtoken,
			 @RequestBody OrdersServiceRequest ordersSvcReq){
		OrdersServiceResponse serviceResponse = new OrdersServiceResponse();
		try {
			if(ordersSvcReq.getCustomer() == null)
				throw new Exception("Invalid Customer Name");
			ordersServiceImpl.addCustomer(ordersSvcReq.getCustomer(), authtoken);
			serviceResponse.setResponseCode(200);
		} catch (Exception e) {
			serviceResponse.setResponseCode(500);
			serviceResponse.setResponseDescription(e.getMessage());
		}
		
		return serviceResponse;
	 }
	
	@RequestMapping(value = "/remove-customer", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
	 public OrdersServiceResponse removeCustomer(HttpServletRequest request,
			 @RequestParam(value = "at", required = true) String authtoken,
			 @RequestBody OrdersServiceRequest ordersSvcReq){
		OrdersServiceResponse serviceResponse = new OrdersServiceResponse();
		try {
			ordersServiceImpl.removeCustomer(ordersSvcReq.getCustomer(), authtoken);
			serviceResponse.setResponseCode(200);
		} catch (Exception e) {
			serviceResponse.setResponseCode(500);
			serviceResponse.setResponseDescription(e.getMessage());
		}
		
		return serviceResponse;
	 }

}


