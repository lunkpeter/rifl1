package rifl3_fullprice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

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
	private List<QueueingConsumer.Delivery> delivOrders;
	private List<QueueingConsumer.Delivery> priceOrders;

	public FullPriceCalculator(String brokerIP) {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(brokerIP);
			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.queueDeclare(IN_QUEUE_PRICE_NAME, false, false, false, null);
			channel.queueDeclare(IN_QUEUE_DELIV_NAME, false, false, false, null);

			priceConsumer = new QueueingConsumer(channel);
			boolean autoAck = false;
			channel.basicConsume(IN_QUEUE_PRICE_NAME, autoAck, priceConsumer);
			deliveryConsumer = new QueueingConsumer(channel);
			channel.basicConsume(IN_QUEUE_DELIV_NAME, autoAck, deliveryConsumer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		priceOrders = new ArrayList<QueueingConsumer.Delivery>();
		delivOrders = new ArrayList<QueueingConsumer.Delivery>();
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
				try {
					QueueingConsumer.Delivery price = priceConsumer.nextDelivery(100);
					if(price!=null)
						priceOrders.add(price);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				try {
					QueueingConsumer.Delivery delivery = deliveryConsumer.nextDelivery(100);
					if(delivery!=null)
						delivOrders.add(delivery);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(delivOrders.size()>0) {
					int priceIndex = -1;
					int deliveryIndex = -1;
					for (Delivery priceOrderD : priceOrders) {
						for (Delivery deliveryOrderD : delivOrders) {
							try {
								Order priceOrder = deserializeOrder(priceOrderD.getBody());
								Order deliveryOrder = deserializeOrder(deliveryOrderD.getBody());
								
								if(priceOrder.getId()==deliveryOrder.getId() && isrunning) {
									priceIndex = priceOrders.indexOf(priceOrderD);
									deliveryIndex = delivOrders.indexOf(deliveryOrderD);
									try {
										System.out.println("BEFORE CALC\n"+priceOrder.toString());
										calculateFullPrice(deliveryOrder.getDeliveryData(),
												priceOrder.getPriceData());
										System.out.println("AFTER CALC\n"+priceOrder.toString());
										
										channel.basicAck(priceOrderD.getEnvelope().getDeliveryTag(), false);
										channel.basicAck(deliveryOrderD.getEnvelope().getDeliveryTag(), false);
										
										isrunning = false;
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							} catch (ClassNotFoundException | IOException e1) {
								e1.printStackTrace();
							}
						}
					}
					if(priceIndex>-1 && deliveryIndex>-1 && !isrunning) {
						System.out.println("Price order enqueued: "+((priceOrders.remove(priceIndex)!=null)?"OK":"NOT"));
						System.out.println("Delivery order enqueued: "+((delivOrders.remove(deliveryIndex)!=null)?"OK":"NOT"));
						
					}
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
			System.out.println("Connections cloased successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
