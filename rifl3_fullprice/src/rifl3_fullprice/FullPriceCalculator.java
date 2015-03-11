package rifl3_fullprice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import datamodel.DeliveryData;
import datamodel.Order;
import datamodel.PriceData;

public class FullPriceCalculator implements Runnable {
	public boolean exit;
	public boolean isrunning = false;
	private Connection connection;
	private Channel channel;
	private QueueingConsumer priceConsumer;
	private QueueingConsumer deliveryConsumer;
	private static final String IN_QUEUE_PRICE_NAME = "net";
	private static final String IN_QUEUE_DELIV_NAME = "delivery";

	public FullPriceCalculator(String brokerIP) {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(brokerIP);
			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.queueDeclare(IN_QUEUE_PRICE_NAME, false, false, false, null);
			channel.queueDeclare(IN_QUEUE_DELIV_NAME, false, false, false, null);
			System.out
					.println(" [*] Waiting for messages. To exit press CTRL+C");

			priceConsumer = new QueueingConsumer(channel);
			channel.basicConsume(IN_QUEUE_PRICE_NAME, true, priceConsumer);
			deliveryConsumer = new QueueingConsumer(channel);
			channel.basicConsume(IN_QUEUE_DELIV_NAME, true, deliveryConsumer);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void calculateFullPrice(DeliveryData deliveryData,
			PriceData priceData) throws InterruptedException {
		Thread.sleep(500);
		double tempPrice = priceData.getPrice();
		double tempNetPrice = priceData.getNetPrice();

		priceData.setPrice(tempPrice + deliveryData.getDeliveryCost());
		priceData.setNetPrice(tempNetPrice + deliveryData.getDeliveryCost());
	}

	@Override
	public void run() {
		while (!exit) {
			if (isrunning) {
				Order delivorder;
				Order priceorder;
				try {
					QueueingConsumer.Delivery price = priceConsumer
							.nextDelivery();
					priceorder = deserializeOrder(price.getBody());
					QueueingConsumer.Delivery delivery = deliveryConsumer
							.nextDelivery();
					delivorder = deserializeOrder(delivery.getBody());

					System.out.println("calculating full price");
					calculateFullPrice(delivorder.getDeliveryData(),
							priceorder.getPriceData());
					System.out.println(priceorder.toString());
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
