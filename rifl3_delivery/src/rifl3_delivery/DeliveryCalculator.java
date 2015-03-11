package rifl3_delivery;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import datamodel.DeliveryData;
import datamodel.Order;
import datamodel.PriceData;

public class DeliveryCalculator implements Runnable{
	public boolean exit;
	private static final String IN_QUEUE_NAME = "distance";
	private static final String OUT_QUEUE_NAME = "delivery";
	private Connection connection;
	private Channel channel;
	private QueueingConsumer consumer;

	private static double priceThreshold = 50000;
	
	public DeliveryCalculator(String brokerIP){
		try {
			ConnectionFactory factory = new ConnectionFactory();
		    factory.setHost(brokerIP);
			connection = factory.newConnection();
			channel = connection.createChannel();

		    channel.queueDeclare(IN_QUEUE_NAME, false, false, false, null);
		    System.out.println(" [*] DELIVERY Waiting for messages. To exit press CTRL+C");
		    
		    consumer = new QueueingConsumer(channel);
		    channel.basicConsume(IN_QUEUE_NAME, true, consumer);
		    
		    channel.queueDeclare(OUT_QUEUE_NAME, false, false, false, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	}
	

	private void calculateDelivery(DeliveryData data, PriceData priceData) throws InterruptedException {
		Thread.sleep(500);
		double tempPrice = data.getDeliveryCost();
		switch (data.getDeliveryMethod()) {
		case PostalDelivery:
			if(priceData.getPrice() >= priceThreshold){
				data.setDeliveryCost(0);
			}else {
				data.setDeliveryCost(tempPrice);
			}
			break;
		case PrivateDelivery:
			if(priceData.getPrice() >= priceThreshold){
				data.setDeliveryCost(0);
			}else {
				data.setDeliveryCost(tempPrice*3);
			}
			break;
		case TakeAway:
			data.setDeliveryCost(0);
			break;

		default:
			break;
		}
	} 

	@Override
	public void run() {
		while (!exit) {
			Order order;
			try {
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				order = deserializeOrder(delivery.getBody());
				System.out.println("calculating delivery");
				calculateDelivery(order.getDeliveryData(), order.getPriceData());
				channel.basicPublish("", OUT_QUEUE_NAME, null,
						serializeOrder(order));
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private byte[] serializeOrder(Order order) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(order);
		return b.toByteArray();
	}

	private Order deserializeOrder(byte[] message) throws IOException,
			ClassNotFoundException {
		Order ret = null;
		ByteArrayInputStream b = new ByteArrayInputStream(message);
		ObjectInputStream o = new ObjectInputStream(b);
		ret = (Order) o.readObject();
		return ret;
	}
	
	private void closeConnection(){
		try {
			channel.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
