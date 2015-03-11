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
	private static final String OUT_EXCHANGE_NAME = "order";
	private static final String IN_QUEUE_NAME = "init";
	private Connection connection;
	private Channel channel;
	private QueueingConsumer consumer;

	public OrderPriceCalculator() {

		try {
			//initialize factory connection and channel
			ConnectionFactory factory = new ConnectionFactory();
			
			factory.setHost("localhost");
			connection = factory.newConnection();
			channel = connection.createChannel();
			//init output exchange
			channel.exchangeDeclare(OUT_EXCHANGE_NAME, "fanout");
			//init input queue
			channel.queueDeclare(IN_QUEUE_NAME, false, false, false, null);
			consumer = new QueueingConsumer(channel);
			channel.basicConsume(IN_QUEUE_NAME, true, consumer);
			System.out.println("Message queues created");
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

	private void closeConnection(){
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
			Order order;
			try {
				System.out.println("run started");
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				order = deserializeOrder(delivery.getBody());
				System.out.println("calculating order price");
				calculateOrderPrice(order);
				// for (Node node : NextNodes) {
				// node.Queue.put(order);
				// }
				byte[] serializeOrder = serializeOrder(order);
				channel.basicPublish(OUT_EXCHANGE_NAME,"", null,
						serializeOrder);
				System.out.println("published");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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