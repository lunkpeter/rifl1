package rifl4_core;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import datamodel.CustomerData;
import datamodel.DeliveryData;
import datamodel.DeliveryMethod;
import datamodel.Item;
import datamodel.Order;
import datamodel.PriceData;
import datamodel.Region;

public class RIFLCore {
	
	
	
	public static void main(String[] args) {
		String brokerUrl="tcp://localhost:61616";
		if(args.length>0) {
			brokerUrl=args[1];
		}
		boolean exit = false;
		
		final String OUT_QUEUE_NAME = "raptor_rifl4_init";
		Connection connection = null;
		Session session = null;
		Destination destination = null;
		MessageProducer producer = null;
		
		try {
			ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
			connection = factory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(OUT_QUEUE_NAME);
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            
			System.out.println("Connection established at: "+brokerUrl);
			
			
			System.out.println("Type \"send\" to send an order request");
			System.out.println("Type \"quit\" to exit");
			while(!exit){
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				try {
					String input = reader.readLine();
					switch (input) {
					case "quit":
						exit = true;
						break;
						
					case "send":
						CustomerData customerData = new CustomerData("TEST", Region.Central);
						DeliveryData deliveryData = new DeliveryData(
								DeliveryMethod.PrivateDelivery);
						PriceData priceData = new PriceData();
						Order order = new Order(customerData, deliveryData, priceData);
						for (int i = 0; i < 10; i++) {
							order.addItem(new Item(25000, "TEST" + i));
					    }
						try {
							BytesMessage orderBMessage = session.createBytesMessage();
							orderBMessage.writeBytes(serializeOrder(order));
							producer.send(orderBMessage);
							System.out.println("Order sent");
						} catch (Exception e) {
							System.err.println("Problem while send order!");
							System.err.println(e.getMessage());
						}
						break;
	
					default:
						break;
					}
					
				} catch (IOException e) {
					System.out.println("Invalid input");
				}
				
				
			}
		} catch(Exception ex) {
			System.err.println(ex.getMessage());
		} finally {
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
	
	private static byte[] serializeOrder(Order order) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(order);
		return b.toByteArray();
	}
	
//	private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
//	    try {
//	        InetAddress candidateAddress = null;
//	        // Iterate all NICs (network interface cards)...
//	        for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
//	            NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
//	            // Iterate all IP addresses assigned to each card...
//	            for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
//	                InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
//	                if (!inetAddr.isLoopbackAddress()) {
//
//	                    if (inetAddr.isSiteLocalAddress()) {
//	                        // Found non-loopback site-local address. Return it immediately...
//	                        return inetAddr;
//	                    }
//	                    else if (candidateAddress == null) {
//	                        // Found non-loopback address, but not necessarily site-local.
//	                        // Store it as a candidate to be returned if site-local address is not subsequently found...
//	                        candidateAddress = inetAddr;
//	                        // Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
//	                        // only the first. For subsequent iterations, candidate will be non-null.
//	                    }
//	                }
//	            }
//	        }
//	        if (candidateAddress != null) {
//	            // We did not find a site-local address, but we found some other non-loopback address.
//	            // Server might have a non-site-local address assigned to its NIC (or it might be running
//	            // IPv6 which deprecates the "site-local" concept).
//	            // Return this non-loopback candidate address...
//	            return candidateAddress;
//	        }
//	        // At this point, we did not find a non-loopback address.
//	        // Fall back to returning whatever InetAddress.getLocalHost() returns...
//	        InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
//	        if (jdkSuppliedAddress == null) {
//	            throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
//	        }
//	        return jdkSuppliedAddress;
//	    }
//	    catch (Exception e) {
//	        UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
//	        unknownHostException.initCause(e);
//	        throw unknownHostException;
//	    }
//	}
	


}
