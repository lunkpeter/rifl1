package rifl3_core;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import datamodel.CustomerData;
import datamodel.DeliveryData;
import datamodel.DeliveryMethod;
import datamodel.Item;
import datamodel.Order;
import datamodel.PriceData;
import datamodel.Region;

public class RIFLCore {
	
	
	
	public static void main(String[] args) {
		
		final String OUT_QUEUE_NAME = "init";
		Connection connection = null;
		Channel channel = null;
		boolean exit = false;
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(OUT_QUEUE_NAME, false, false, false, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		while(!exit){
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try {
				String input = reader.readLine();
				switch (input) {
				case "quit":
					exit = true;
					break;
					
				case "send":
					CustomerData customerData = new CustomerData("TEST", Region.Central);
					DeliveryData deliveryData = new DeliveryData(
							DeliveryMethod.PrivateDelivery);
					PriceData priceData = new PriceData();
					Order order = new Order(customerData, deliveryData, priceData);
					for (int i = 0; i < 10; i++) {
						order.addItem(new Item(25000, "TEST" + i));
				    }
					try {
						
						
						channel.basicPublish("", OUT_QUEUE_NAME, null,
								serializeOrder(order));
						System.out.println("published");
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
				
			} catch (IOException e) {
				System.out.println("Invalid input");
			}
			
			
		}
		
		try {
			channel.close();
			connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		
	}
	
	private static byte[] serializeOrder(Order order) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(order);
		return b.toByteArray();
	}
	


}
