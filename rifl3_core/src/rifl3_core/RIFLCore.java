package rifl3_core;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import datamodel.CustomerData;
import datamodel.DeliveryData;
import datamodel.DeliveryMethod;
import datamodel.Item;
import datamodel.Order;
import datamodel.PriceData;
import datamodel.Region;

public class RIFLCore {
	
	
	
	public static void main(String[] args) {
		String brokerIP="localhost";
		if(args.length>0) {
			brokerIP=args[1];
		}
		
		final String OUT_QUEUE_NAME = "init";
		Connection connection = null;
		Channel channel = null;
		boolean exit = false;
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(brokerIP);
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(OUT_QUEUE_NAME, false, false, false, null);
			System.out.println("AMQP connection established at: "+brokerIP);
			System.out.println("Local IP: "+getLocalHostLANAddress().getHostAddress());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
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
						
						
						channel.basicPublish("", OUT_QUEUE_NAME, null,
								serializeOrder(order));
						System.out.println("Order sent");
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
				
			} catch (IOException e) {
				System.out.println("Invalid input");
			}
			
			
		}
		
		try {
			channel.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		
	}
	
	private static byte[] serializeOrder(Order order) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(order);
		return b.toByteArray();
	}
	
	private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
	    try {
	        InetAddress candidateAddress = null;
	        // Iterate all NICs (network interface cards)...
	        for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
	            NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
	            // Iterate all IP addresses assigned to each card...
	            for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
	                InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
	                if (!inetAddr.isLoopbackAddress()) {

	                    if (inetAddr.isSiteLocalAddress()) {
	                        // Found non-loopback site-local address. Return it immediately...
	                        return inetAddr;
	                    }
	                    else if (candidateAddress == null) {
	                        // Found non-loopback address, but not necessarily site-local.
	                        // Store it as a candidate to be returned if site-local address is not subsequently found...
	                        candidateAddress = inetAddr;
	                        // Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
	                        // only the first. For subsequent iterations, candidate will be non-null.
	                    }
	                }
	            }
	        }
	        if (candidateAddress != null) {
	            // We did not find a site-local address, but we found some other non-loopback address.
	            // Server might have a non-site-local address assigned to its NIC (or it might be running
	            // IPv6 which deprecates the "site-local" concept).
	            // Return this non-loopback candidate address...
	            return candidateAddress;
	        }
	        // At this point, we did not find a non-loopback address.
	        // Fall back to returning whatever InetAddress.getLocalHost() returns...
	        InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
	        if (jdkSuppliedAddress == null) {
	            throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
	        }
	        return jdkSuppliedAddress;
	    }
	    catch (Exception e) {
	        UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
	        unknownHostException.initCause(e);
	        throw unknownHostException;
	    }
	}
	


}
