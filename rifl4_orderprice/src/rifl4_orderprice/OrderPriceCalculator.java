package rifl4_orderprice;

import java.io.IOException;

import javax.jms.BytesMessage;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;

import rifl4_base.BaseCalculator;
import datamodel.Item;
import datamodel.Order;

public class OrderPriceCalculator extends BaseCalculator {
	private static final String OUT_QUEUE_NAME = "order_delivery";
	private static final String OUT_QUEUE_NAME2 = "order_discount";
	private static final String IN_QUEUE_NAME = "init";

	public OrderPriceCalculator(String brokerUrl) throws JMSException {
		setConnection(brokerUrl, IN_QUEUE_NAME, OUT_QUEUE_NAME);

		if(OUT_QUEUE_NAME2!=null) {
			destination2 = session.createQueue(OUT_QUEUE_NAME2);
            producer2 = session.createProducer(destination2);
            producer2.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		}
	}

	protected void calculate(Order order) throws InterruptedException {
		double tempPrice = 0;
		Thread.sleep(500);
		for (Item item : order.getItems()) {
			tempPrice += item.getPrice();
		}

		order.getPriceData().setPrice(tempPrice);
	}
	private Queue destination2;
	private MessageProducer producer2;

	@Override
	public void run() {
		while (!exit) {
			if (isrunning) {
				Order order;
				try {
					Message inputMessage = consumer.receive();
					
					if(inputMessage instanceof BytesMessage) {
						BytesMessage objectMessage = (BytesMessage) inputMessage;
						byte[] data = new byte[(int) objectMessage.getBodyLength()];
						objectMessage.readBytes(data);
						order = deserializeOrder(data);
						
						System.out.println("BEFORE CALC"+order);
						calculate(order);
						System.out.println("AFTER CALC"+order);
						
						if(producer!=null) {
							BytesMessage orderBMessage = session.createBytesMessage();
							orderBMessage.writeBytes(serializeOrder(order));
							producer.send(orderBMessage);
						}
						if(producer2!=null) {
							BytesMessage orderBMessage = session.createBytesMessage();
							orderBMessage.writeBytes(serializeOrder(order));
							producer2.send(orderBMessage);
						}
						isrunning = false;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JMSException e) {
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
}
