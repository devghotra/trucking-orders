package com.jassboys.trucking.orders.dao;

import com.jassboys.trucking.orders.model.Order;
import com.jassboys.trucking.orders.model.OrdersServiceResponse;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

@Component
public class OrdersServiceDao {

	static int TOKEN_LIFE = 60;
	static Properties dbProps = null;

	@Autowired
	DataSource dataSource;
	
	private void loadDbProperties() throws IOException{
		dbProps = new Properties();
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("db.properties");
		dbProps.load(input);
	}

	private Connection getConnection() throws Exception{
		return dataSource.getConnection();
	}


	private Connection getConnection1() throws Exception{
		//String connectionUrl = System.getenv("OPENSHIFT_MYSQL_DB_URL") == null ? "mysql://root@127.0.0.1:3306/" : System.getenv("OPENSHIFT_MYSQL_DB_URL");
		
		Connection connection = null;
		Class.forName("com.mysql.jdbc.Driver");
		
		if(dbProps == null)
			loadDbProperties();
		
		if(System.getenv("OPENSHIFT_MYSQL_DB_URL") == null)
			connection = DriverManager.getConnection(dbProps.getProperty("dbhost"), dbProps.getProperty("dbuser"), dbProps.getProperty("dbpwd"));
		else
			connection = DriverManager.getConnection(
							"jdbc:mysql://"+System.getenv("OPENSHIFT_MYSQL_DB_HOST")+":"+System.getenv("OPENSHIFT_MYSQL_DB_PORT"), 
							System.getenv("OPENSHIFT_MYSQL_DB_USERNAME"), 
							System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD"));
		
		return connection;
	}
	
	public OrdersServiceResponse authorize(String username, String password) throws Exception{
		Connection connection = null;
		Statement statement = null;
		
		OrdersServiceResponse orderSvcResponse =  new OrdersServiceResponse();
		boolean adminUserFlag;
		
		try{
			connection = getConnection();
			statement = connection.createStatement();
			String sql = "select * from jborders.user_details "
							+ "where username='"+username.toUpperCase()+"' and password = '"+password.toUpperCase()+"' and active_flag = 'Y'";
			ResultSet rs  = statement.executeQuery(sql);
			
			if(!rs.next())
				throw new Exception("Invalid credentials");
			else
				adminUserFlag = rs.getString("admin_user_flag").trim().equalsIgnoreCase("Y");
			
			String authtoken = UUID.randomUUID().toString().replace("-", "");
			
			String insertSql = "INSERT INTO jborders.authtoken_details "
					+ "(authtoken, username, mod_date_time, token_life) "
					+ "VALUES( "
					+ "'"+authtoken+"', "
					+ "'"+username.toUpperCase()+"', "
					+ "now(), "
					+ TOKEN_LIFE+""
					+ ")";
			
			statement.executeUpdate(insertSql);	
			
			orderSvcResponse.setAuthToken(authtoken);
			orderSvcResponse.setAdminUserFlag(adminUserFlag);
			
			return orderSvcResponse;
			
		} catch (Exception e) {
			System.out.println("Exception in authorize: "+e);
			throw e;
		} finally{
			try {
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Exception while closing connection authorize: "+e);
				throw e;
			}
		}
	}
	
	public List<Order> getOrderList(String authtoken) throws Exception{
		List<Order> orderList = new ArrayList<Order>();
		Connection connection = null;
		Statement statement = null;
		
		try{
			connection = getConnection();
			statement = connection.createStatement();
			
			checkAuthToken(statement, authtoken);
			String sql = "select * from jborders.orders order by order_id desc limit 30";
			ResultSet rs  = statement.executeQuery(sql);
			while (rs.next()) {
				
				Order order = new Order();
				long orderId = rs.getLong("order_id");
				order.setOrderId(orderId);
				order.setOrderIdRef("<a href=\'javascript: loadOrderFromLink("+orderId+")\'>"+orderId+"</a>");
				order.setOrderDate(rs.getDate("order_date").toString());
				order.setTagNumber(rs.getString("tag_number"));
				order.setPrimeCarrier(rs.getString("prime_carrier"));
				order.setCustomer(rs.getString("customer"));
				order.setJobNumber(rs.getString("job_number"));
				order.setLoadsNRate(""+rs.getFloat("loads")+"/<br/>"+rs.getDouble("rate_per_load"));
				order.setHoursNRate(""+rs.getFloat("hours")+"/<br/>"+rs.getDouble("rate_per_hour"));
				order.setStandbyHrsNRate(""+rs.getFloat("standby_hours")+"/<br/>"+rs.getDouble("rate_per_standby_hour"));
				order.setTonsNRate(""+rs.getFloat("tons")+"/<br/>"+rs.getDouble("rate_per_ton"));
				order.setEntryTotal(rs.getDouble("entry_total"));
				order.setPaymentStatus(rs.getString("payment_status"));
				order.setPaidToSub(rs.getString("paid_to_sub"));
				//order.setCreationDateTime(rs.getDate("creation_date_time"));
				//order.setModDateTime(rs.getDate("mod_date_time"));
				//order.setModifiedByUser(rs.getString("modified_by_user"));
				//order.setCreatedByUser(rs.getString("created_by_user")); 
				order.setTruckHaulerNumber(rs.getString("truck_hauler_number"));
				order.setDriverName(rs.getString("driver_name"));
				order.setBridgeToll(rs.getDouble("bridge_toll"));
				order.setBrokagePercentage(rs.getDouble("brokage_percentage"));
				order.setNetEntry(rs.getDouble("net_entry"));
				order.setNotes(rs.getString("notes"));
				
				order.setPaymentHours(rs.getFloat("payment_hours"));
				order.setPaymentPerHour(rs.getDouble("payment_per_hour"));
				order.setPaymentLoads(rs.getFloat("payment_loads"));
				order.setPaymentPerLoad(rs.getDouble("payment_per_load"));
				order.setPaymentTons(rs.getFloat("payment_tons"));
				order.setPaymentPerTon(rs.getDouble("payment_per_ton"));
				
				order.setOtherPayment(rs.getDouble("payment_other"));
				order.setTotalPayment(rs.getDouble("total_payment"));
				
				order.setOtherPaymentDesc(rs.getString("payment_other_desc"));
				order.setBridgeTollPayment(rs.getDouble("payment_bridge_toll"));
				
				order.setOtherCharges(rs.getDouble("other_charges"));
				order.setShift(rs.getString("shift"));
				
				orderList.add(order);			
			}
		} catch (Exception e) {
			System.out.println("Exception in getOrderList: "+e);
			throw e;
		} finally{
			try {
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Exception while closing connection getOrderList: "+e);
				throw e;
			}
		}
		
		return orderList;
	}
	
	public Order getOrderDetails(long orderId, String authtoken) throws Exception{
		Order order = null;
		Connection connection = null;
		Statement statement = null;
		
		try{
			connection = getConnection();
			statement = connection.createStatement();
			
			checkAuthToken(statement, authtoken);
			String sql = "select * from jborders.orders where order_id="+orderId;
			ResultSet rs  = statement.executeQuery(sql);
			while (rs.next()) {
			    order = new Order();
				order.setOrderId(orderId);
				order.setOrderDate(rs.getDate("order_date").toString());
				order.setTagNumber(rs.getString("tag_number"));
				order.setPrimeCarrier(rs.getString("prime_carrier"));
				order.setCustomer(rs.getString("customer"));
				order.setJobNumber(rs.getString("job_number"));
				order.setLoads(rs.getFloat("loads"));
				order.setRatePerLoad(rs.getDouble("rate_per_load"));
				order.setHours(rs.getFloat("hours"));
				order.setRatePerHour(rs.getDouble("rate_per_hour"));
				order.setStandbyHours(rs.getFloat("standby_hours"));
				order.setRatePerStandbyHr(rs.getDouble("rate_per_standby_hour"));
				order.setTons(rs.getFloat("tons"));
				order.setRatePerTon(rs.getDouble("rate_per_ton"));
				order.setEntryTotal(rs.getDouble("entry_total"));
				order.setPaymentStatus(rs.getString("payment_status"));
				order.setPaidToSub(rs.getString("paid_to_sub"));
				order.setCreationDateTime(rs.getDate("creation_date_time"));
				order.setModDateTime(rs.getDate("mod_date_time"));
				order.setModifiedByUser(rs.getString("modified_by_user"));
				order.setCreatedByUser(rs.getString("created_by_user")); 
				order.setTruckHaulerNumber(rs.getString("truck_hauler_number"));
				order.setDriverName(rs.getString("driver_name"));
				order.setNotes(rs.getString("notes"));
				order.setBridgeToll(rs.getDouble("bridge_toll"));
				order.setBrokagePercentage(rs.getDouble("brokage_percentage"));
				order.setNetEntry(rs.getDouble("net_entry"));
				
				order.setPaymentHours(rs.getFloat("payment_hours"));
				order.setPaymentPerHour(rs.getDouble("payment_per_hour"));
				order.setPaymentLoads(rs.getFloat("payment_loads"));
				order.setPaymentPerLoad(rs.getDouble("payment_per_load"));
				order.setPaymentTons(rs.getFloat("payment_tons"));
				order.setPaymentPerTon(rs.getDouble("payment_per_ton"));
				
				order.setOtherPayment(rs.getDouble("payment_other"));
				order.setTotalPayment(rs.getDouble("total_payment"));
				
				order.setOtherPaymentDesc(rs.getString("payment_other_desc"));
				order.setBridgeTollPayment(rs.getDouble("payment_bridge_toll"));
				order.setOtherCharges(rs.getDouble("other_charges"));
				order.setShift(rs.getString("shift"));
			}
			
		} catch (Exception e) {
			System.out.println("Exception in getOrderDetails: "+e);
			throw e;
		} finally{
			try {
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Exception while closing connection getOrderDetails: "+e);
				throw e;
			}
		}
		
		if(order == null)
			throw new Exception("Order number "+orderId+" not found");
		
		return order;
	}
	
	public List<String> getTruckHaulerDetails(String authtoken) throws Exception{
		List<String> truckHaulerList = new ArrayList<String>();
		Connection connection = null;
		Statement statement = null;
		
		try{
			connection = getConnection();
			statement = connection.createStatement();
			
			checkAuthToken(statement, authtoken);
			String sql = "select * from jborders.truck_hauler_details where active_flag = 'Y'";
			ResultSet rs  = statement.executeQuery(sql);
			while (rs.next()) {
				truckHaulerList.add(rs.getString("number"));
			}
		} catch (Exception e) {
			System.out.println("Exception in getTruckHaulerDetails: "+e);
			throw e;
		} finally{
			try {
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Exception while closing connection getTruckHaulerDetails: "+e);
				throw e;
			}
		}
	
		return truckHaulerList;
	}
	
	public List<String> getDriverDetails(String authtoken) throws Exception{
		List<String> driverList = new ArrayList<String>();
		Connection connection = null;
		Statement statement = null;
		
		try{
			connection = getConnection();
			statement = connection.createStatement();
			
			checkAuthToken(statement, authtoken);
			String sql = "select * from jborders.driver_details";
			ResultSet rs  = statement.executeQuery(sql);
			while (rs.next()) {
				driverList.add(rs.getString("name"));
			}
		} catch (Exception e) {
			System.out.println("Exception in getDriverDetails: "+e);
			throw e;
		} finally{
			try {
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Exception while closing connection getDriverDetails: "+e);
				throw e;
			}
		}
	
		return driverList;
	}
	
	public List<String> getPrimeCarriers(String authtoken) throws Exception{
		List<String> pcList = new ArrayList<String>();
		Connection connection = null;
		Statement statement = null;
		
		try{
			connection = getConnection();
			statement = connection.createStatement();
			
			checkAuthToken(statement, authtoken);
			String sql = "select * from jborders.prime_carrier_details";
			ResultSet rs  = statement.executeQuery(sql);
			while (rs.next()) {
				pcList.add(rs.getString("name"));
			}
		} catch (Exception e) {
			System.out.println("Exception in getPrimeCarriers: "+e);
			throw e;
		} finally{
			try {
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Exception while closing connection getPrimeCarriers: "+e);
				throw e;
			}
		}
	
		return pcList;
	}
	
	
	public Order addOrder(Order order, String authtoken) throws Exception{
		Connection connection = null;
		Statement statement = null;
		
		try{
			connection = getConnection();
			statement = connection.createStatement();
			
			String username = checkAuthToken(statement, authtoken);
			
			String insertSql = ""
					+ "INSERT INTO jborders.orders(order_date, shift, tag_number,prime_carrier, "
					+ "customer,job_number,loads,rate_per_load,hours,rate_per_hour, "
					+ "tons, rate_per_ton,entry_total,payment_status,paid_to_sub, truck_hauler_number, "
					+ "driver_name, notes, bridge_toll, brokage_percentage, net_entry, standby_hours, rate_per_standby_hour, other_charges, "
					+ "mod_date_time,modified_by_user,created_by_user) "
					+ "VALUES ("
					+ "STR_TO_DATE('"+order.getOrderDate()+"', '%Y-%m-%d'),"
					+ "'"+order.getShift()+"',"
					+ "'"+order.getTagNumber()+"',"
					+ "'"+order.getPrimeCarrier()+"'," 
					+ "'"+order.getCustomer()+"',"
					+ "'"+order.getJobNumber()+"',"
					+ ""+order.getLoads()+","
					+ ""+order.getRatePerLoad()+","
					+ ""+order.getHours()+","
					+ ""+order.getRatePerHour()+","
					+ ""+order.getTons()+","
					+ ""+order.getRatePerTon()+","
					+ ""+order.getEntryTotal()+","
					+ "'"+order.getPaymentStatus()+"',"
					+ "'"+order.getPaidToSub()+"',"
					+ "'"+order.getTruckHaulerNumber()+"',"
					+ "'"+order.getDriverName()+"',"
					+ "'"+order.getNotes()+"',"
					+ ""+order.getBridgeToll()+","
					+ ""+order.getBrokagePercentage()+","
					+ ""+order.getNetEntry()+","
					+ ""+order.getStandbyHours()+","
					+ ""+order.getRatePerStandbyHr()+","
					+ ""+order.getOtherCharges()+","
					+ "now(),"
					+ "'"+username+"',"
					+ "'"+username+"')"
					;

			
			statement.executeUpdate(insertSql);			
		} catch (Exception e) {
			System.out.println("Exception in addOrder: "+e);
			throw e;
		} finally{
			try {
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Exception while closing connection addOrder: "+e);
				throw e;
			}
		}
		
		return order;
	}
	
	public void addTruckHauler(String truckHaulerNumber, String authtoken) throws Exception{
		Connection connection = null;
		Statement statement = null;
		
		try{
			connection = getConnection();
			statement = connection.createStatement();
			
			checkAuthToken(statement, authtoken);
			
			String insertSql = "INSERT INTO jborders.truck_hauler_details (number, active_flag) "
					+ "VALUES ('"+truckHaulerNumber.trim().toUpperCase()+"', 'Y')";

			statement.executeUpdate(insertSql);			
		} catch (Exception e) {
			System.out.println("Exception in addTruckHauler: "+e);
			throw e;
		} finally{
			try {
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Exception while closing connection addTruckHauler: "+e);
				throw e;
			}
		}
	}
	
	public void removeTruckHauler(String truckHaulerNumber, String authtoken) throws Exception{
		Connection connection = null;
		Statement statement = null;
		
		try{
			connection = getConnection();
			statement = connection.createStatement();
			
			checkAuthToken(statement, authtoken);
			
			String sql = "delete from jborders.truck_hauler_details where number='"+truckHaulerNumber.trim().toUpperCase()+"'";

			statement.executeUpdate(sql);			
		} catch (Exception e) {
			System.out.println("Exception in removeTruckHauler: "+e);
			throw e;
		} finally{
			try {
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Exception while closing connection removeTruckHauler: "+e);
				throw e;
			}
		}
	}
	
	public void addDriver(String driverName, String authtoken) throws Exception{
		Connection connection = null;
		Statement statement = null;
		
		try{
			connection = getConnection();
			statement = connection.createStatement();
			
			checkAuthToken(statement, authtoken);
			
			String insertSql = "INSERT INTO jborders.driver_details (name) "
					+ "VALUES ('"+WordUtils.capitalizeFully(driverName)+"')";

			statement.executeUpdate(insertSql);			
		} catch (Exception e) {
			System.out.println("Exception in addDriver: "+e);
			throw e;
		} finally{
			try {
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Exception while closing connection addDriver: "+e);
				throw e;
			}
		}
	}
	
	public void removeDriver(String driverName, String authtoken) throws Exception{
		Connection connection = null;
		Statement statement = null;
		
		try{
			connection = getConnection();
			statement = connection.createStatement();
			
			checkAuthToken(statement, authtoken);
			
			String sql = "delete from jborders.driver_details where name='"+WordUtils.capitalizeFully(driverName)+"'";

			statement.executeUpdate(sql);			
		} catch (Exception e) {
			System.out.println("Exception in removeDriver: "+e);
			throw e;
		} finally{
			try {
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Exception while closing connection removeDriver: "+e);
				throw e;
			}
		}
	}
	
	public List<String> getCustomerDetails(String authtoken) throws Exception{
		List<String> customerList = new ArrayList<String>();
		Connection connection = null;
		Statement statement = null;
		
		try{
			connection = getConnection();
			statement = connection.createStatement();
			
			checkAuthToken(statement, authtoken);
			String sql = "select * from jborders.customer_details";
			ResultSet rs  = statement.executeQuery(sql);
			while (rs.next()) {
				customerList.add(rs.getString("name"));
			}
		} catch (Exception e) {
			System.out.println("Exception in getCustomerDetails: "+e);
			throw e;
		} finally{
			try {
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Exception while closing connection getCustomerDetails: "+e);
				throw e;
			}
		}
	
		return customerList;
	}
	
	public void addCustomer(String custName, String authtoken) throws Exception{
		Connection connection = null;
		Statement statement = null;
		
		try{
			connection = getConnection();
			statement = connection.createStatement();
			
			checkAuthToken(statement, authtoken);
			
			String insertSql = "INSERT INTO jborders.customer_details (name) "
					+ "VALUES ('"+WordUtils.capitalizeFully(custName)+"')";

			statement.executeUpdate(insertSql);			
		} catch (Exception e) {
			System.out.println("Exception in addCustomer: "+e);
			throw e;
		} finally{
			try {
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Exception while closing connection addCustomer: "+e);
				throw e;
			}
		}
	}
	
	public void removeCustomer(String custName, String authtoken) throws Exception{
		Connection connection = null;
		Statement statement = null;
		
		try{
			connection = getConnection();
			statement = connection.createStatement();
			
			checkAuthToken(statement, authtoken);
			
			String sql = "delete from jborders.customer_details where name='"+WordUtils.capitalizeFully(custName)+"'";

			statement.executeUpdate(sql);			
		} catch (Exception e) {
			System.out.println("Exception in removeCustomer: "+e);
			throw e;
		} finally{
			try {
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Exception while closing connection removeCustomer: "+e);
				throw e;
			}
		}
	}
	
	public void addPrimeCarrier(String pcName, String authtoken) throws Exception{
		Connection connection = null;
		Statement statement = null;
		
		try{
			connection = getConnection();
			statement = connection.createStatement();
			
			checkAuthToken(statement, authtoken);
			
			String insertSql = "INSERT INTO jborders.prime_carrier_details (name) "
					+ "VALUES ('"+pcName.toUpperCase()+"')";

			statement.executeUpdate(insertSql);			
		} catch (Exception e) {
			System.out.println("Exception in addPrimeCarrier: "+e);
			throw e;
		} finally{
			try {
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Exception while closing connection addPrimeCarrier: "+e);
				throw e;
			}
		}
	}
	
	public void removePrimeCarrier(String pcName, String authtoken) throws Exception{
		Connection connection = null;
		Statement statement = null;
		
		try{
			connection = getConnection();
			statement = connection.createStatement();
			
			checkAuthToken(statement, authtoken);
			
			String sql = "delete from jborders.prime_carrier_details where name='"+pcName.toUpperCase()+"'";

			statement.executeUpdate(sql);			
		} catch (Exception e) {
			System.out.println("Exception in removePrimeCarrier: "+e);
			throw e;
		} finally{
			try {
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Exception while closing connection removePrimeCarrier: "+e);
				throw e;
			}
		}
	}
	
	public void deleteOrder(String orderIdStr, String authtoken) throws Exception{
		Connection connection = null;
		Statement statement = null;
		
		try{
			connection = getConnection();
			statement = connection.createStatement();
			
			checkAuthToken(statement, authtoken);
			
			long orderId;
			try {
				orderId = Long.parseLong(orderIdStr);
			} catch (Exception e) {
				throw new Exception("Invalid Order ID");
			}
			
			String sql = "delete from jborders.orders where order_id="+orderId;

			statement.executeUpdate(sql);			
		} catch (Exception e) {
			System.out.println("Exception in deleteOrder: "+e);
			throw e;
		} finally{
			try {
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Exception while closing connection deleteOrder: "+e);
				throw e;
			}
		}
	}
	
	public void addUser(String username, String password, boolean adminUserFlag, String authtoken) throws Exception{
		Connection connection = null;
		Statement statement = null;
		
		try{
			connection = getConnection();
			statement = connection.createStatement();
			
			String createdByUser = checkAuthToken(statement, authtoken);			
			String adminUserStr = adminUserFlag ? "Y" : "N";
			
			String insertSql = "INSERT INTO jborders.user_details"
					+ "(username, password, active_flag, mod_date_time, created_by_user, mod_by_user, admin_user_flag) "
					+ "VALUES( "
					+ "'"+username.trim().toUpperCase()+"', "
					+ "'"+password.trim().toUpperCase()+"', "
					+ "'Y', "
					+ "now(), "
					+ "'"+createdByUser+"', "
					+ "'"+createdByUser+"', "
					+ "'"+adminUserStr+"')";

			statement.executeUpdate(insertSql);			
		} catch (Exception e) {
			System.out.println("Exception in addUser: "+e);
			throw new Exception("Username "+username+" already exist");
		} finally{
			try {
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Exception while closing connection addUser: "+e);
				throw e;
			}
		}
	}
	
	public void deactivateUser(String username, String authtoken) throws Exception{
		Connection connection = null;
		Statement statement = null;
		
		try{
			connection = getConnection();
			statement = connection.createStatement();
			
			checkAuthToken(statement, authtoken);			
			
			String sql = "delete from jborders.user_details "
					+ "where username = '"+username.trim().toUpperCase()+"'";

			int rowsUpdated = statement.executeUpdate(sql);
			
			if(rowsUpdated == 0)
				throw new Exception("Username "+username.trim()+" not found");
			
		} catch (Exception e) {
			System.out.println("Exception in deactivateUser: "+e);
			throw e;
		} finally{
			try {
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Exception while closing connection deactivateUser: "+e);
				throw e;
			}
		}
	}
	
	public Order updateOrder(Order order, String authtoken) throws Exception{
		Connection connection = null;
		Statement statement = null;
		
		try{
			connection = getConnection();
			statement = connection.createStatement();
						
			String username = checkAuthToken(statement, authtoken);
			
			String updSql = "UPDATE jborders.orders SET "
					+ "order_date = STR_TO_DATE('"+order.getOrderDate()+"', '%Y-%m-%d'), "
					+ "shift = '"+order.getShift()+"', "
					+ "tag_number = '"+order.getTagNumber()+"', "
					+ "prime_carrier = '"+order.getPrimeCarrier()+"', "
					+ "customer = '"+order.getCustomer()+"', "
					+ "job_number = '"+order.getJobNumber()+"', "
					+ "loads = "+order.getLoads()+", "
					+ "rate_per_load = "+order.getRatePerLoad()+", "
					+ "hours = "+order.getHours()+", "
					+ "rate_per_hour = "+order.getRatePerHour()+", "
					+ "standby_hours = "+order.getStandbyHours()+", "
					+ "rate_per_standby_hour = "+order.getRatePerStandbyHr()+", "
					+ "tons = "+order.getTons()+", "
					+ "rate_per_ton = "+order.getRatePerTon()+", "
					+ "entry_total = "+order.getEntryTotal()+", "
					+ "brokage_percentage = "+order.getBrokagePercentage()+", "
					+ "net_entry = "+order.getNetEntry()+", "
					+ "bridge_toll = "+order.getBridgeToll()+", "
					+ "payment_status = '"+order.getPaymentStatus()+"', "
					+ "paid_to_sub = '"+order.getPaidToSub()+"', "
					+ "mod_date_time = now(), "
					+ "modified_by_user = '"+username+"', "
					+ "driver_name = '"+order.getDriverName()+"', "
					+ "truck_hauler_number = '"+order.getTruckHaulerNumber()+"', "
					+ "notes = '"+order.getNotes()+"', "
					+ "payment_hours = "+order.getPaymentHours()+", "
					+ "payment_per_hour = "+order.getPaymentPerHour()+", "
					+ "payment_tons = "+order.getPaymentTons()+", "
					+ "payment_per_ton = "+order.getPaymentPerTon()+", "
					+ "payment_loads = "+order.getPaymentLoads()+", "
					+ "payment_per_load = "+order.getPaymentPerLoad()+", "
					+ "payment_other = "+order.getOtherPayment()+", "
					+ "payment_bridge_toll = "+order.getBridgeTollPayment()+", "
					+ "payment_other_desc = '"+order.getOtherPaymentDesc()+"', "
					+ "other_charges = "+order.getOtherCharges()+", "
					+ "total_payment = "+order.getTotalPayment()+" "
					+ "WHERE order_id = "+order.getOrderId()+";"
					+ "";
		
			statement.executeUpdate(updSql);			
		} catch (Exception e) {
			System.out.println("Exception in updateOrder: "+e);
			throw e;
		} finally{
			try {
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Exception while closing connection updateOrder: "+e);
				throw e;
			}
		}
		
		return order;
	}
	
	public List<Order> searchOrders(Order order, String authtoken) throws Exception{
		List<Order> orderList = new ArrayList<Order>();
		Connection connection = null;
		Statement statement = null;
		
		try{
			connection = getConnection();
			statement = connection.createStatement();
			
			checkAuthToken(statement, authtoken);
			
			StringBuilder sql = new StringBuilder("select * from jborders.orders where order_id > 0");
			
			if(order.getFromOrderDate() != null && !order.getFromOrderDate().isEmpty() && 
					order.getToOrderDate() != null && !order.getToOrderDate().isEmpty()){
				sql.append(" and order_date BETWEEN '"+order.getFromOrderDate()+"' and '"+ order.getToOrderDate()+"'");
			} 
			else if(order.getFromOrderDate() != null && !order.getFromOrderDate().isEmpty())
				sql.append(" and order_date >= '").append(order.getFromOrderDate()).append("'");
			
			else if(order.getToOrderDate() != null && !order.getToOrderDate().isEmpty())
				sql.append(" and order_date <= '").append(order.getToOrderDate()).append("'");
			
			if(order.getTagNumber() != null && !order.getTagNumber().isEmpty())
				sql.append(" and tag_number = '").append(order.getTagNumber().trim()).append("'");
			
			if(order.getPrimeCarrier() != null && !order.getPrimeCarrier().isEmpty())
				sql.append(" and prime_carrier = '").append(order.getPrimeCarrier().trim()).append("'");
			
			if(order.getCustomer() != null && !order.getCustomer().isEmpty())
				sql.append(" and customer = '").append(order.getCustomer().trim()).append("'");
			
			if(order.getJobNumber() != null && !order.getJobNumber().isEmpty())
				sql.append(" and job_number = '").append(order.getJobNumber().trim()).append("'");

			if(order.getPaymentStatus() != null && !order.getPaymentStatus().isEmpty())
				sql.append(" and payment_status = '").append(order.getPaymentStatus().trim()).append("'");
			
			if(order.getDriverName() != null && !order.getDriverName().isEmpty())
				sql.append(" and driver_name = '").append(order.getDriverName().trim()).append("'");
			
			if(order.getTruckHaulerNumber() != null && !order.getTruckHaulerNumber().isEmpty())
				sql.append(" and truck_hauler_number = '").append(order.getTruckHaulerNumber().trim()).append("'");
			
			sql.append(" order by order_id desc");
			
			ResultSet rs  = statement.executeQuery(sql.toString());
			
			while (rs.next()) {
				Order ord = new Order();
				long orderId = rs.getLong("order_id");
				ord.setOrderId(orderId);
				ord.setOrderIdRef("<a href=\'javascript: loadOrderFromLink("+orderId+")\'>"+orderId+"</a>");
				ord.setOrderDate(rs.getDate("order_date").toString());
				ord.setTagNumber(rs.getString("tag_number"));
				ord.setPrimeCarrier(rs.getString("prime_carrier"));
				ord.setCustomer(rs.getString("customer"));
				ord.setJobNumber(rs.getString("job_number"));
				ord.setLoadsNRate(""+rs.getFloat("loads")+"/<br/>"+rs.getDouble("rate_per_load"));
				ord.setHoursNRate(""+rs.getFloat("hours")+"/<br/>"+rs.getDouble("rate_per_hour"));
				ord.setStandbyHrsNRate(""+rs.getFloat("standby_hours")+"/<br/>"+rs.getDouble("rate_per_standby_hour"));
				ord.setTonsNRate(""+rs.getFloat("tons")+"/<br/>"+rs.getDouble("rate_per_ton"));
				ord.setEntryTotal(rs.getDouble("entry_total"));
				ord.setBridgeToll(rs.getDouble("bridge_toll"));
				ord.setPaymentStatus(rs.getString("payment_status"));
				ord.setPaidToSub(rs.getString("paid_to_sub"));
				//ord.setCreationDateTime(rs.getDate("creation_date_time"));
				//ord.setModDateTime(rs.getDate("mod_date_time"));
				//ord.setModifiedByUser(rs.getString("modified_by_user"));
				//ord.setCreatedByUser(rs.getString("created_by_user")); 
				ord.setTruckHaulerNumber(rs.getString("truck_hauler_number"));
				ord.setDriverName(rs.getString("driver_name"));
				ord.setNotes(rs.getString("notes"));
				ord.setBrokagePercentage(rs.getDouble("brokage_percentage"));
				ord.setNetEntry(rs.getDouble("net_entry"));
				
				ord.setLoads(rs.getFloat("loads"));
				ord.setRatePerLoad(rs.getDouble("rate_per_load"));
				ord.setHours(rs.getFloat("hours"));
				ord.setRatePerHour(rs.getDouble("rate_per_hour"));
				ord.setStandbyHours(rs.getFloat("standby_hours"));
				ord.setRatePerStandbyHr(rs.getDouble("rate_per_standby_hour"));
				ord.setTons(rs.getFloat("tons"));
				ord.setRatePerTon(rs.getDouble("rate_per_ton"));
				
				ord.setPaymentHours(rs.getFloat("payment_hours"));
				ord.setPaymentPerHour(rs.getDouble("payment_per_hour"));
				ord.setPaymentLoads(rs.getFloat("payment_loads"));
				ord.setPaymentPerLoad(rs.getDouble("payment_per_load"));
				ord.setPaymentTons(rs.getFloat("payment_tons"));
				ord.setPaymentPerTon(rs.getDouble("payment_per_ton"));
				
				ord.setOtherPayment(rs.getDouble("payment_other"));
				ord.setTotalPayment(rs.getDouble("total_payment"));
				
				ord.setOtherPaymentDesc(rs.getString("payment_other_desc"));
				ord.setBridgeTollPayment(rs.getDouble("payment_bridge_toll"));
				
				ord.setOtherCharges(rs.getDouble("other_charges"));
				ord.setShift(rs.getString("shift"));
			    
			    orderList.add(ord);
		
			}
		} catch (Exception e) {
			System.out.println("Exception in searchOrders: "+e);
			throw e;
		} finally{
			try {
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Exception while closing connection searchOrders: "+e);
				throw e;
			}
		}
		
		return orderList;
	}
	
	private String checkAuthToken(Statement statement, String authtoken) throws Exception{
		String username = null;
		try{
			String sql = "select * from jborders.authtoken_details where authtoken='"+authtoken+"'";
			ResultSet rs  = statement.executeQuery(sql);
			while (rs.next()) {
				username = rs.getString("username");
				Timestamp modDateTime = rs.getTimestamp("mod_date_time");
				int tokenLife = rs.getInt("token_life");
				
				long currentTimestamp = Calendar.getInstance().getTimeInMillis();
				
				long expirationTime = modDateTime.getTime() + tokenLife*60*1000;
				
				if(expirationTime > currentTimestamp){
					String updSql = "update jborders.authtoken_details set mod_date_time = now() where authtoken='"+authtoken+"'";
					statement.executeUpdate(updSql);
					return username;
				}
				else 
					throw new Exception("Session Expired");
			}
		} catch (Exception e) {
			System.out.println("Exception in checkAuthToken: "+e);
			throw e;
		} 
		
		throw new Exception("Unauthorized User");
	}

}


/*SimpleDateFormat reqSdf = new SimpleDateFormat("MM/dd/yyyy");
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
String strDate = sdf.format(reqSdf.parse(order.getFromOrderDate()));*/

/*SimpleDateFormat feSdf = new SimpleDateFormat("MM/dd/yyyy");
SimpleDateFormat dbSdf = new SimpleDateFormat("yyyy-MM-dd");*/

//String orderDate = feSdf.format(dbSdf.parse(rs.getDate("order_date").toString()));