package rifl3_core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
		Connection connection;
		Channel channel;
		
		
		CustomerData customerData = new CustomerData("TEST", Region.Central);
		DeliveryData deliveryData = new DeliveryData(
				DeliveryMethod.PrivateDelivery);
		PriceData priceData = new PriceData();
		Order order = new Order(customerData, deliveryData, priceData);
		for (int i = 0; i < 10; i++) {
			order.addItem(new Item(25000, "TEST" + i));
	    }
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(OUT_QUEUE_NAME, false, false, false, null);
			
			channel.basicPublish("", OUT_QUEUE_NAME, null,
					serializeOrder(order));
			System.out.println("published");
		} catch (Exception e) {
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
