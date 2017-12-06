import java.util.HashMap;
import java.util.Date;
import java.util.ArrayList;
import java.io.*;  
import java.io.FileOutputStream;  
import java.io.ObjectOutputStream;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.*;
import java.sql.*;
import java.io.File;
import java.io.IOException;

public class DBUtilities
{
	HashMap<String, Order> hmOrders = null;
	HashMap<String, ProductCatalog> hmProductCatalog = new HashMap<String, ProductCatalog>();
	
	
	ProductCatalog pc = null;
	
	public DBUtilities() { 
	}

	public boolean insertOrderDetails(HashMap<String, Order> orderDetails)
	{
		try
		{
			hmOrders = getOrderDetails();

			if(hmOrders == null) {
				
				FileOutputStream fos = new FileOutputStream("hashmapfile.ser");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(orderDetails);
				oos.close();
				fos.close();
				System.out.printf("Serialized HashMap data is saved in hashmap.ser");
			}
			else {

				for (String key : orderDetails.keySet()) {

					Order o = new Order();
					o = orderDetails.get(key);
				    System.out.println("Key = " + key + ", Value = " + o.getProductName() + ",getQuantity" + o.getQuantity());
				    hmOrders.put(key,o);
				}

				FileOutputStream fos = new FileOutputStream("hashmapfile.ser");
				ObjectOutputStream oos = new ObjectOutputStream(fos);

				oos.writeObject(hmOrders);
				oos.close();
				fos.close();
				System.out.printf("Updated Serialized HashMap data is saved in hashmap.ser");


			}
			

		}catch(IOException ioe)
		{
			ioe.printStackTrace();
		}

		return true;

	}

	public HashMap<String, Order> getOrderDetails() {

		HashMap<String, Order> map = new HashMap<String, Order>();
		try
		{
			FileInputStream fis = new FileInputStream("hashmapfile.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			map = (HashMap) ois.readObject();
			ois.close();
			fis.close();

			return map;
		}catch(IOException ioe)
		{
			ioe.printStackTrace();
			//return map;
		}catch(ClassNotFoundException c)
		{
			System.out.println("Class not found");
			c.printStackTrace();
			//return map;
		}
		System.out.println("Deserialized HashMap..");

		return map;

	}

	public Order fetchOrderDetails(String orderID, String productID)throws Exception
	{
		Order userOrder = new Order();
		ArrayList<Order> ordArray = new ArrayList<Order>();

		try
		{
			hmOrders = getOrderDetails();


			for (String key : hmOrders.keySet()) {

					Order o = new Order();
					o = hmOrders.get(key);

					
					System.out.println(o.getId());
					System.out.println(o.getProductId());

					if(o.getProductId().equals(productID) && o.getId().equals(orderID)){
						ordArray.add(o);

					}
			}

			for(int i=0; i< ordArray.size(); i++) {

				Order prd = new Order();
				prd = ordArray.get(i);

				userOrder.setId(orderID);
				userOrder.setProductId(productID);
				userOrder.setProductName(prd.getProductName());
				userOrder.setUserName(prd.getUserName());
				userOrder.setOrderPrice(prd.getQuantity() * prd.getOrderPrice());
				userOrder.setQuantity(prd.getQuantity());
				userOrder.setRetailer(prd.getRetailer());
				userOrder.setManufacturer(prd.getManufacturer());
				userOrder.setOrderTime(prd.getOrderTime());
				userOrder.setDeliveryTime(prd.getDeliveryTime());
				userOrder.setStatus(prd.getStatus());
			}
		}
		catch(Exception ex)
		{
			System.out.println("fetchOrderDetails()- " + ex.toString());			
		}
		finally
		{			
			
		}
		return userOrder;
	}

	public void updateOrderDetails(Order newOrder, String orderID, String productID)throws Exception
	{
		Order userOrder = new Order();
		ArrayList<Order> ordArray = new ArrayList<Order>();

		try
		{
			hmOrders = getOrderDetails();

			Iterator<String> it1 = hmOrders.keySet().iterator();
					while(it1.hasNext())
					{
						String key = it1.next();
						Order o = new Order();
						o = hmOrders.get(key);

						if(o.getProductId().equals(productID) && o.getId().equals(orderID)){

							it1.remove();
							break;
						}
					
					}

				hmOrders.put(orderID,newOrder);

			for (String key : hmOrders.keySet()) {					

				 System.out.println("oooooKey = " + key + ", Value = " + hmOrders.get(key).getQuantity());
			}
			boolean flag = insertOrderDetails(hmOrders);
		}
		catch(Exception ex)
		{
			System.out.println("fetchOrderDetails()- " + ex.toString());			
		}
		finally
		{			
			
		}
	}

}