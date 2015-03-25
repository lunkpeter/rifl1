package rifl4_core;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.Random;

import javax.jms.BytesMessage;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import datamodel.CustomerData;
import datamodel.DeliveryData;
import datamodel.DeliveryMethod;
import datamodel.Item;
import datamodel.Order;
import datamodel.PriceData;
import datamodel.Region;

public class RIFLCore {
	public static enum MessagingMode{Queue, Topic};
	
	public final static MessagingMode mode = MessagingMode.Topic;
	private static boolean exit = false; 
	
	private static InitialContext context;
	// Fields for topic mode
	private static final String OUT_TOPIC_NAME = "topic/raptor_rifl4_init";
	private static TopicConnection topicConnection;
	private static TopicSession topicSession;
	private static TopicPublisher topicPublisher;
	// Fields for queue mode
	private static final String OUT_QUEUE_NAME = "queue/raptor_rifl4_init";
	private static QueueConnection queueConnection;
	private static QueueSession queueSession;
	private static QueueSender queueSender;

	public static void main(String[] args) {
		try {
			setConnection();
			System.out.println("Connection established!");
			
			
			System.out.println("Type \"send\" to send an order request");
			System.out.println("Type \"quit\" to exit");
			while(!exit){
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				try {
					String input = reader.readLine();
					if(input.equals("quit")){
						System.out.println("Bye-bye!");
						exit = true;
					}else if (input.equals("send")) {
						Order order = generateRandomOrder();
						
						try {
							if(mode==MessagingMode.Topic) {
								BytesMessage orderBMessage = topicSession.createBytesMessage();
								orderBMessage.writeBytes(serializeOrder(order));
								topicPublisher.publish(orderBMessage);
								System.out.println("Order sent");
							} else {
								BytesMessage orderBMessage = queueSession.createBytesMessage();
								orderBMessage.writeBytes(serializeOrder(order));
								queueSender.send(orderBMessage);
								System.out.println("Order sent");
							}
						} catch (Exception e) {
							System.err.println("Problem while send order!");
							System.err.println(e.getMessage());
						}
					}
					
					
				} catch (IOException e) {
					System.out.println("Invalid input");
				}
				
				
			}
		} catch(Exception ex) {
			System.err.println(ex.getMessage());
		} finally {
			closeConnection();
		}
	}
	
	private static void setConnection() throws JMSException {
		try {
			context = new InitialContext();
			
			if(mode==MessagingMode.Topic) {
				TopicConnectionFactory factory = (TopicConnectionFactory) context.lookup("TopicConnectionFactory");
				topicConnection = factory.createTopicConnection();
				topicConnection.start();
				topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
				
				Topic topic = (Topic) context.lookup(OUT_TOPIC_NAME);
		        topicPublisher = topicSession.createPublisher(topic);
		        topicPublisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
				
			} else {
				QueueConnectionFactory factory = (QueueConnectionFactory) context.lookup("QueueConnectionFactory");
				queueConnection = factory.createQueueConnection();
				queueConnection.start();
				queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

				Queue queue = (Queue) context.lookup(OUT_QUEUE_NAME);
		        queueSender = queueSession.createSender(queue);
	            queueSender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			}
		} catch (JMSException e) {
			System.err.println("Problem while estabilish connection.");
			System.err.println(e.getMessage());
			e.printStackTrace();
			closeConnection();
			throw e;
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static void closeConnection() {
		try {
			if(topicConnection!=null) {
	            topicConnection.close();
	            if(topicSession!=null) {
	            	topicSession.close();
	            }
	            if(queueSession!=null) {
	            	queueSession.close();
	            }
			}
		} catch (JMSException e) {
			System.err.println("Problem with connection close!");
			System.err.println(e.getMessage());
		}
	}
	
	private static Order generateRandomOrder() {
		Random rand = new Random();
		
		// Random region
		Region region = Region.Central;
		switch(rand.nextInt(5)) {
		case 0:
			region = Region.Central;
			break;
		case 1:
			region = Region.East;
			break;
		case 2:
			region = Region.North;
			break;
		case 3:
			region = Region.South;
			break;
		default:
			region = Region.West;
		}
		CustomerData customerData = new CustomerData("TEST", region);
		
		// Random delivery method
		DeliveryMethod method = DeliveryMethod.PrivateDelivery;
		switch(rand.nextInt(3)) {
		case 0:
			method = DeliveryMethod.PostalDelivery;
			break;
		case 1:
			method = DeliveryMethod.PrivateDelivery;
			break;
		default:
			method = DeliveryMethod.TakeAway;
		}
		DeliveryData deliveryData = new DeliveryData(method);
		
		
		PriceData priceData = new PriceData();
		Order order = new Order(customerData, deliveryData, priceData);
		
		// Random number of items with random price
		int itemsNum = rand.nextInt(15);
		for (int i = 0; i < itemsNum; i++) {
			order.addItem(new Item(rand.nextInt(10000)+20000, "TEST" + i));
	    }
		return order;
	}
	
	private static byte[] serializeOrder(Order order) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(order);
		return b.toByteArray();
	}
}
