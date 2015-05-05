package rifl6;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rifl6.base.BaseCalculator;
import rifl6.base.Logger;
import rifl6.base.OrderMessage;
import rifl6.base.OrderMessage.Sender;
import rifl6.base.SenderGUI;
import rifl6.calculators.DeliveryCalculator;
import rifl6.calculators.DiscountCalculator;
import rifl6.calculators.DistanceCalculator;
import rifl6.calculators.FullPriceCalculator;
import rifl6.calculators.NetPriceCalculator;
import rifl6.calculators.OrderPriceCalculator;
import datamodel.CustomerData;
import datamodel.DeliveryData;
import datamodel.DeliveryMethod;
import datamodel.Item;
import datamodel.Order;
import datamodel.PriceData;
import datamodel.Region;

public class RIFLCore {
	private static boolean exit = false;
	
	public static enum Command {none, generate, send, exit};
	public static Command command = Command.none;
	private static List<Order> orders = new ArrayList<Order>();
	private static SenderGUI gui;

	public static void main(String[] args) {
		try {
			BaseCalculator full = new FullPriceCalculator();
			BaseCalculator net = new NetPriceCalculator(full);
			BaseCalculator delivPostal = new DeliveryCalculator(full,DeliveryMethod.PostalDelivery);
			BaseCalculator delivPrivate = new DeliveryCalculator(full,DeliveryMethod.PrivateDelivery);
			BaseCalculator delivTakeAway = new DeliveryCalculator(full,DeliveryMethod.TakeAway);
			BaseCalculator disc = new DiscountCalculator(net);
			BaseCalculator dist = new DistanceCalculator(delivPostal, delivPrivate, delivTakeAway);
			BaseCalculator orderPrice = new OrderPriceCalculator(dist, disc);
			
			List<Thread> threads = new ArrayList<Thread>();
			threads.add(new Thread(full));
			threads.add(new Thread(net));
			threads.add(new Thread(delivPostal));
			threads.add(new Thread(delivPrivate));
			threads.add(new Thread(delivTakeAway));
			threads.add(new Thread(disc));
			threads.add(new Thread(dist));
			threads.add(new Thread(orderPrice));
			
			for (Thread thread : threads) {
				thread.start();
			}
						
			if(!BaseCalculator.AUTOMATIC)
				gui = new SenderGUI("Order Creator");
			else {
				System.out.println("Type \"send\" to send a generated order request");
				System.out.println("Type \"send\" + parameters to send predefined order requests (like: \"send 0 1 0 2 0 1\").\nParameters' meaning:");
				System.out.println("\t\"0\": postal delivery");
				System.out.println("\t\"1\": private delivery");
				System.out.println("\t\"2\": take away");
				System.out.println("Type \"quit\" to exit");
			}
			
			while (!exit) {
				//Wait for command
				if(BaseCalculator.AUTOMATIC) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
					try {
						String input = reader.readLine();
						if("quit".equals(input)) {
							command = Command.exit;
						} else if("send".equals(input)){
							command = Command.generate;
						} else if(input.contains("send")) {
							input = input.replaceAll("send", "");
							input = input.trim();
							try {
								int count = Integer.parseInt(input);
								for(int i=0 ; i <count;i++){
									int random = Logger.getNext(3);
									switch (random) {
									case 0:
										orders.add(createPostalOrder());
										break;
									case 1:
										orders.add(createPrivateOrder());
										break;
									case 2:
										orders.add(createTakeAwayOrder());
										break;

									default:
										System.out.println("e");
										break;
									}
									command = Command.send;
								}
							} catch (Exception e) {
								System.out.println("Not valid count parameter");
							}
							
//							String[] params = input.split(" ");
//							for (String param : params) {
//								if("0".equals(param)){
//									orders.add(createPostalOrder());
//									command = Command.send;
//								} else if("1".equals(param)){
//									orders.add(createPrivateOrder());
//									command = Command.send;
//								} else if("2".equals(param)){
//									orders.add(createTakeAwayOrder());
//									command = Command.send;
//								} else if("send".equals(param)) {
//								} else {
//									System.out.println("Bad parameter: \""+param+"\"");
//								}
//							}
						}
					} catch (IOException e) {
						System.out.println("Invalid input");
					}
				} else {
					while(command==Command.none) {
						Thread.sleep(100);
					}
				}
				
				if (command==Command.exit) {
					System.out.println("Bye-bye!");
					exit = true;
				} else if (command==Command.generate) {
					orders.add(generateRandomOrder());
					
					if(BaseCalculator.AUTOMATIC || gui==null)
						command = Command.send;
					else
						gui.setOrder(orders.get(0));
				}
				if (command==Command.send && orders!=null && orders.size()>0) {
					for (Order order : orders) {
						OrderMessage message = new OrderMessage(order, Sender.Order);
						
						try {
							orderPrice.addOrder(serializeOrder(message));
							System.out.println("Order sent: "+order.getId());
	
						} catch (Exception e) {
							System.err.println("Problem while sending order!");
							System.err.println(e.getMessage());
						}
					}
					orders.clear();
				}
				command = Command.none;

			}
			
			BaseCalculator.isrunning = false;
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		} 
	}

	private static Order createPostalOrder() {
		DeliveryData deliveryData = new DeliveryData(DeliveryMethod.PostalDelivery);
		return createOrder(deliveryData);
	}

	private static Order createPrivateOrder() {
		DeliveryData deliveryData = new DeliveryData(DeliveryMethod.PrivateDelivery);
		return createOrder(deliveryData);
	}

	private static Order createTakeAwayOrder() {
		DeliveryData deliveryData = new DeliveryData(DeliveryMethod.TakeAway);
		return createOrder(deliveryData);
	}
	
	private static Order createOrder(DeliveryData deliveryData) {
		CustomerData customerData = new CustomerData("TEST", Region.Central);
		PriceData priceData = new PriceData();
		Order order = new Order(customerData, deliveryData, priceData);
		for (int i = 0; i < 10; i++) {
			order.addItem(new Item(25000, "TEST" + i));
	    }
		return order;
	}

	private static Order generateRandomOrder() {
		Random rand = new Random();
		// Random region
		Region region = Region.Central;
		switch (rand.nextInt(5)) {
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
		switch (rand.nextInt(3)) {
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
			order.addItem(new Item(rand.nextInt(10000) + 20000, "TEST" + i));
		}
		return order;
	}

	private static byte[] serializeOrder(OrderMessage msg) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(msg);
		return b.toByteArray();
	}
	
	
}



