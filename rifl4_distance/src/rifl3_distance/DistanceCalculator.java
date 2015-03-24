package rifl3_distance;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import datamodel.CustomerData;
import datamodel.DeliveryData;
import datamodel.Order;

public class DistanceCalculator implements Runnable{
	private static double northDeliveryCost = 1500;
	private static double southDeliveryCost = 2500;
	private static double eastDeliveryCost = 3500;
	private static double westDeliveryCost = 4500;
	private static double centralDeliveryCost = 500;
	private static final String IN_EXCHANGE_NAME = "order";
	private static final String OUT_QUEUE_NAME = "distance";
	public boolean exit;
	public boolean isrunning = false;
	private Connection connection;
	private Channel channel;
	private QueueingConsumer consumer;
	

	
	public DistanceCalculator(String brokerIP){
		try {
			ConnectionFactory factory = new ConnectionFactory();
		    factory.setHost(brokerIP);
			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.exchangeDeclare(IN_EXCHANGE_NAME, "fanout");
			String queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, IN_EXCHANGE_NAME, "");

			consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, true, consumer);
			
			channel.queueDeclare(OUT_QUEUE_NAME, false, false, false, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void calculateDistance(DeliveryData deliveryData,
			CustomerData customerData) throws InterruptedException {
		Thread.sleep(500);
		switch (customerData.getRegion()) {
		case North:
			deliveryData.setDeliveryCost(northDeliveryCost);
			break;
		case South:
			deliveryData.setDeliveryCost(southDeliveryCost);
			break;
		case East:
			deliveryData.setDeliveryCost(eastDeliveryCost);
			break;
		case West:
			deliveryData.setDeliveryCost(westDeliveryCost);
			break;
		case Central:
			deliveryData.setDeliveryCost(centralDeliveryCost);
			break;

		default:
			break;
		}
	}

	@Override
	public void run() {
		while (!exit) {
			if(isrunning){
			Order order;
			try {
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				order = deserializeOrder(delivery.getBody());
				System.out.println("BEFORE CALC"+order.toString());
				calculateDistance(order.getDeliveryData(), order.getCustomerData());
				System.out.println("AFTER CALC"+order.toString());
				channel.basicPublish("", OUT_QUEUE_NAME, null,
						serializeOrder(order));
				isrunning = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			}else {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			

		}
		closeConnection();
		
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
