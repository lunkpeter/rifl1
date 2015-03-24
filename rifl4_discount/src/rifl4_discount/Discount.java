package rifl4_discount;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.JMSException;

public class Discount {
	public static void main(String[] args) {
		boolean exit = false;
		String brokerUrl="tcp://localhost:61616";
		if(args.length>0) {
			brokerUrl=args[1];
		}
		
		DiscountCalculator calc = null;
		try {
			calc = new DiscountCalculator(brokerUrl);
			Thread mythread = new Thread(calc);
			mythread.start();
			
			System.out.println("Connection established at: "+brokerUrl);
			
			System.out.println("Type \"step\" to step the workflow");
			System.out.println("Type \"quit\" to exit");
			while(!exit)
			{
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				try {
					String input = reader.readLine();
					switch (input) {
					case "step":
						calc.isrunning = true;
						break;
					case "quit":
						exit = true;
						break;
	
					default:
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (JMSException ex) {
			
		}finally {
			calc.exit = true;
		}
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
