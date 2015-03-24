package rifl4_base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import datamodel.Order;

public abstract class BaseCalculator implements Runnable{
	public boolean exit;
	public boolean isrunning = false;

	protected Connection connection;
	protected Session session;
	protected Queue destination;
	protected MessageProducer producer;
	protected MessageConsumer consumer;
	
	public void setConnection(String brokerUrl, String IN_QUEUE_NAME, String OUT_QUEUE_NAME) throws JMSException {
		try {
			ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
			connection = factory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			if(OUT_QUEUE_NAME!=null) {
				destination = session.createQueue(OUT_QUEUE_NAME);
	            producer = session.createProducer(destination);
	            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			}

			if(IN_QUEUE_NAME!=null) {
	            Destination destination = session.createQueue(IN_QUEUE_NAME);
	            consumer = session.createConsumer(destination);
			}
		} catch (JMSException e) {
			System.err.println("Problem while estabilish connection.");
			closeConnection();
			throw e;
		}

	}

	protected abstract void calculate(Order order) throws InterruptedException;

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
	            if(session!=null) {
	            	session.close();
	            }
			}
		} catch (JMSException e) {
			System.err.println("Problem with connection close!");
			System.err.println(e.getMessage());
		}
	}
}
