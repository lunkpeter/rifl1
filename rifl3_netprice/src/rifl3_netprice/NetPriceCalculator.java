package rifl3_netprice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import datamodel.Order;
import datamodel.PriceData;

public class NetPriceCalculator implements Runnable {
	public boolean exit;
	public boolean isrunning = false;
	private static final String IN_QUEUE_NAME = "discount";
	private static final String OUT_QUEUE_NAME = "net";
	private Connection connection;
	private Channel channel;
	private QueueingConsumer consumer;

	private static double netModifier = 1.27;

	public NetPriceCalculator(String brokerIP) {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(brokerIP);
			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.queueDeclare(IN_QUEUE_NAME, false, false, false, null);

			consumer = new QueueingConsumer(channel);
			channel.basicConsume(IN_QUEUE_NAME, true, consumer);

			channel.queueDeclare(OUT_QUEUE_NAME, false, false, false, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void calculateNetPrice(PriceData data) throws InterruptedException {
		Thread.sleep(500);
		data.setNetPrice(data.getPrice() * netModifier);
	}

	@Override
	public void run() {
		while (!exit) {
			if (isrunning) {
				Order order;
				try {
					QueueingConsumer.Delivery delivery = consumer
							.nextDelivery();
					order = deserializeOrder(delivery.getBody());
					System.out.println("BEFORE CALC"+order.toString());
					calculateNetPrice(order.getPriceData());
					channel.basicPublish("", OUT_QUEUE_NAME, null,
							serializeOrder(order));
					System.out.println("AFTER CALC"+order.toString());
					isrunning = false;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
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

	private void closeConnection() {
		try {
			channel.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
