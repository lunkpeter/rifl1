package rifl4_base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.jms.BytesMessage;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import datamodel.Order;

public abstract class BaseCalculator implements Runnable{
	public boolean exit;
	public boolean isrunning = false;
	
	public static enum MessagingMode{Queue, Topic};
	
	public final static MessagingMode mode = MessagingMode.Topic; 

	protected InitialContext context;
	// Fields for topic mode
	protected TopicConnection connection;
	protected TopicSession topicSession;
	protected TopicPublisher topicPublisher;
	protected TopicSubscriber topicSubscriber;
	// Fields for queue mode
	protected QueueConnection queueConnection;
	protected QueueSession queueSession;
	protected QueueSender queueSender;
	protected QueueReceiver queueReceiver;
	
	public void setConnection(String IN_NAME, String OUT_NAME) throws JMSException {
		try {
			context = new InitialContext();
			
			if(mode==MessagingMode.Topic) {
				TopicConnectionFactory factory = (TopicConnectionFactory) context.lookup("TopicConnectionFactory");
				connection = factory.createTopicConnection();
				connection.start();
				topicSession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
				if(OUT_NAME!=null) {
					Topic topic = (Topic) context.lookup(OUT_NAME);
		            topicPublisher = topicSession.createPublisher(topic);
		            topicPublisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
				}
	
				if(IN_NAME!=null) {
		            Topic topic = (Topic) context.lookup(IN_NAME);
		            topicSubscriber = topicSession.createSubscriber(topic);
				}
			} else {
				QueueConnectionFactory factory = (QueueConnectionFactory) context.lookup("QueueConnectionFactory");
				queueConnection = factory.createQueueConnection();
				queueConnection.start();
				queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
				if(OUT_NAME!=null) {
					Queue queue = (Queue) context.lookup(OUT_NAME);
		            queueSender = queueSession.createSender(queue);
		            queueSender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
				}
	
				if(IN_NAME!=null) {
		            Queue queue = (Queue) context.lookup(IN_NAME);
		            queueReceiver = queueSession.createReceiver(queue);
				}
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

	protected abstract void calculate(Order order) throws InterruptedException;

	public void run() {
		working();
	}

	protected void working() {
		while (!exit) {
			Message inputMessage = null;
			try {
				if(mode==MessagingMode.Topic) {
					inputMessage=topicSubscriber.receive(100);
				} else {
					inputMessage=queueReceiver.receive(100);
				}
			} catch (JMSException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(inputMessage!=null) {
				isrunning=false;
				System.out.println("You have got a message!");
				while(!isrunning) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Order order=null;
				try {
					if(inputMessage instanceof BytesMessage) {
						BytesMessage objectMessage = (BytesMessage) inputMessage;
						byte[] data = new byte[(int) objectMessage.getBodyLength()];
						objectMessage.readBytes(data);
						order = deserializeOrder(data);
						
						System.out.println("BEFORE CALC"+order);
						calculate(order);
						System.out.println("AFTER CALC"+order);
						
						if(mode==MessagingMode.Topic) {
							if(topicPublisher!=null) {
								BytesMessage orderBMessage = topicSession.createBytesMessage();
								orderBMessage.writeBytes(serializeOrder(order));
								topicPublisher.publish(orderBMessage);
							}
						} else {
							if(queueSender!=null) {
								BytesMessage orderBMessage = queueSession.createBytesMessage();
								orderBMessage.writeBytes(serializeOrder(order));
								queueSender.send(orderBMessage);
							}
						}
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

	protected byte[] serializeOrder(Order order) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(order);
		return b.toByteArray();
	}

	protected Order deserializeOrder(byte[] message) throws IOException,
			ClassNotFoundException {
		Order ret = null;
		ByteArrayInputStream b = new ByteArrayInputStream(message);
		ObjectInputStream o = new ObjectInputStream(b);
		ret = (Order) o.readObject();
		return ret;
	}

	protected void closeConnection() {
		try {
			if(connection!=null) {
	            connection.close();
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
}
