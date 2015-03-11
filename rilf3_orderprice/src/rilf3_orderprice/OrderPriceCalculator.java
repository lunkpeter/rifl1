package rilf3_orderprice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import datamodel.Item;
import datamodel.Order;

public class OrderPriceCalculator implements Runnable {
	public boolean exit;
	public boolean isrunning = false;
	private static final String OUT_EXCHANGE_NAME = "order";
	private static final String IN_QUEUE_NAME = "init";
	private Connection connection;
	private Channel channel;
	private QueueingConsumer consumer;

	public OrderPriceCalculator(String brokerIP) {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(brokerIP);
			connection = factory.newConnection();
			channel = connection.createChannel();
			// init output exchange
			channel.exchangeDeclare(OUT_EXCHANGE_NAME, "fanout");
			// init input queue
			channel.queueDeclare(IN_QUEUE_NAME, false, false, false, null);
			consumer = new QueueingConsumer(channel);
			channel.basicConsume(IN_QUEUE_NAME, true, consumer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void calculateOrderPrice(Order order) throws InterruptedException {
		double tempPrice = 0;
		Thread.sleep(500);
		for (Item item : order.getItems()) {
			tempPrice += item.getPrice();
		}

		order.getPriceData().setPrice(tempPrice);
	}

	private void closeConnection() {
		try {
			channel.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
					System.out.println("BEFORE CALC "+order.toString());
					calculateOrderPrice(order);
					byte[] serializeOrder = serializeOrder(order);
					channel.basicPublish(OUT_EXCHANGE_NAME, "", null,
							serializeOrder);
					System.out.println("AFTER CALC "+order.toString());
					isrunning = false;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
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

}
