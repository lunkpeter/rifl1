package rifl4_fullprice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;
import javax.naming.NamingException;

import rifl4_base.BaseCalculator;
import datamodel.DeliveryData;
import datamodel.Order;
import datamodel.PriceData;

public class FullPriceCalculator extends BaseCalculator {
	public boolean exit;
	public boolean isrunning = false;
	
	private static final String IN_TOPIC_PRICE_NAME = "topic/net";
	private static final String IN_TOPIC_DELIV_NAME = "topic/delivery";
	private TopicSubscriber subscriberDelivery;
	
	private static final String IN_QUEUE_PRICE_NAME = "queue/net";
	private static final String IN_QUEUE_DELIV_NAME = "queue/delivery";
	private QueueReceiver receiverDelivery;
	
	private List<Order> delivOrders = new ArrayList<Order>();
	private List<Order> priceOrders = new ArrayList<Order>();

	public FullPriceCalculator() throws JMSException, NamingException {
		if(mode==MessagingMode.Topic) {
			setConnection(IN_TOPIC_PRICE_NAME, null);
	
			Topic destination2 = (Topic) context.lookup(IN_TOPIC_DELIV_NAME);
            subscriberDelivery = topicSession.createSubscriber(destination2);
		} else {
			setConnection(IN_QUEUE_PRICE_NAME, null);
			
			Queue destination2 = (Queue) context.lookup(IN_QUEUE_DELIV_NAME);
			receiverDelivery = queueSession.createReceiver(destination2);
		}
	}

	@Override
	protected void calculate(Order order) throws InterruptedException {
		DeliveryData deliveryData = order.getDeliveryData();
		PriceData priceData = order.getPriceData();
		double tempPrice = priceData.getPrice();
		double tempNetPrice = priceData.getNetPrice();

		priceData.setPrice(tempPrice + deliveryData.getDeliveryCost());
		priceData.setNetPrice(tempNetPrice + deliveryData.getDeliveryCost());
	}

	@Override
	public void run() {
		while (!exit) {
			Order priceOrder = null;
			Order delivOrder = null;
			try {
				Message inputMessage=null;
				if(mode==MessagingMode.Topic) {
					inputMessage = topicSubscriber.receive(100);
				} else {
					inputMessage = queueReceiver.receive(100);
				}
				
				if(inputMessage instanceof BytesMessage) {
					BytesMessage objectMessage = (BytesMessage) inputMessage;
					byte[] data = new byte[(int) objectMessage.getBodyLength()];
					objectMessage.readBytes(data);
					priceOrders.add(deserializeOrder(data));
				}
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

			try {
				Message inputMessage=null;
				if(mode==MessagingMode.Topic) {
					inputMessage = subscriberDelivery.receive(100);
				} else {
					inputMessage = receiverDelivery.receive(100);
				}
				
				if(inputMessage instanceof BytesMessage) {
					BytesMessage objectMessage = (BytesMessage) inputMessage;
					byte[] data = new byte[(int) objectMessage.getBodyLength()];
					objectMessage.readBytes(data);
					delivOrders.add(deserializeOrder(data));
				}
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
			

			try {
				for (Order dO : delivOrders) {
					for (Order pO : priceOrders) {
						if(dO.getId()==pO.getId()) {
							delivOrder=dO;
							priceOrder=pO;
						}
					}
				}
				if(delivOrder!=null && priceOrder!=null) {
					isrunning=false;
					System.out.println("You have got a message!");
					while (!isrunning) {
						Thread.sleep(50);
					}
					delivOrders.remove(delivOrder);
					priceOrders.remove(priceOrder);
					
					priceOrder.setDeliveryData(delivOrder.getDeliveryData());
					
					System.out.println("BEFORE CALC"+priceOrder);
					calculate(priceOrder);
					System.out.println("AFTER CALC"+priceOrder);
					
					isrunning = false;
				}
					
			} catch (InterruptedException intex) {
				
			}
		}
		closeConnection();
	}
}
