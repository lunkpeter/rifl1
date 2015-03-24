package rilf3_discount;

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

public class DiscountCalculator implements Runnable {
	public boolean exit;
	public boolean isrunning = false;
	private static final String IN_EXCHANGE_NAME = "order";
	private static final String OUT_QUEUE_NAME = "discount";
	private static double priceThreshold1 = 100000;
	private static double priceThreshold2 = 200000;
	private Connection connection;
	private Channel channel;
	private QueueingConsumer consumer;

	public DiscountCalculator(String brokerIP) {
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

	private void caclulateDiscount(PriceData data) throws InterruptedException {
		double tempPrice = data.getPrice();
		Thread.sleep(500);
		if (data.getPrice() >= priceThreshold1) {
			if (data.getPrice() >= priceThreshold2) {
				data.setPrice(tempPrice * 0.8);
			} else {
				data.setPrice(tempPrice * 0.9);
			}
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
					System.out.println("BEFORE CALC"+order.toString());
					caclulateDiscount(order.getPriceData());
					channel.basicPublish("", OUT_QUEUE_NAME, null,
							serializeOrder(order));
					System.out.println("AFTER CALC"+order.toString());
					isrunning = false;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
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

	private void closeConnection() {
		try {
			channel.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
