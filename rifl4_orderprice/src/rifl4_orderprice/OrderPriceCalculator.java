package rifl4_orderprice;

import java.io.IOException;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueSender;

import rifl4_base.BaseCalculator;
import datamodel.Item;
import datamodel.Order;

public class OrderPriceCalculator extends BaseCalculator {
	private static final String OUT_TOPIC_NAME = "topic/order";
	private static final String IN_TOPIC_NAME = "topic/raptor_rifl4_init";
	
	private static final String OUT_QUEUE_NAME_DELIVERY = "queue/order_delivery";
	private static final String OUT_QUEUE_NAME_DISCOUNT = "queue/order_discount";
	private static final String IN_QUEUE_NAME = "queue/raptor_rifl4_init";
	private QueueSender queueSenderDiscount;

	public OrderPriceCalculator() throws JMSException {
		if(mode==MessagingMode.Topic){
			setConnection(IN_TOPIC_NAME, OUT_TOPIC_NAME);
		} else {
			setConnection(IN_QUEUE_NAME, OUT_QUEUE_NAME_DELIVERY);
			Queue destination = queueSession.createQueue(OUT_QUEUE_NAME_DISCOUNT);
            queueSenderDiscount = queueSession.createSender(destination);
            queueSenderDiscount.setDeliveryMode(javax.jms.DeliveryMode.NON_PERSISTENT);
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

	@Override
	public void run() {
		if(mode==MessagingMode.Topic) {
			super.working();
		} else {
			workingWithQueue();
		}
	}

	private void workingWithQueue() {
		while (!exit) {
			Order order;
			try {
				Message inputMessage = queueReceiver.receive(100);
				if(inputMessage!=null) {
					isrunning=false;
					System.out.println("You have got a message!");
				}
				while (!isrunning) {
					Thread.sleep(50);
				}
				if(inputMessage instanceof BytesMessage) {
					BytesMessage objectMessage = (BytesMessage) inputMessage;
					byte[] data = new byte[(int) objectMessage.getBodyLength()];
					objectMessage.readBytes(data);
					order = deserializeOrder(data);
					
					System.out.println("BEFORE CALC"+order);
					calculate(order);
					System.out.println("AFTER CALC"+order);
					
					BytesMessage orderBMessage = queueSession.createBytesMessage();
					orderBMessage.writeBytes(serializeOrder(order));
					if(queueSender!=null) {
						queueSender.send(orderBMessage);
					}
					if(queueSenderDiscount!=null) {
						queueSenderDiscount.send(orderBMessage);
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
		}
		closeConnection();
	}
}
