package rifl5_core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;

import rifl5_base.OrderMessage;
import rifl5_base.OrderMessage.Sender;
import rifl5_base.SenderGUI;
import rifl5_calculators.DeliveryCalculator;
import rifl5_calculators.DiscountCalculator;
import rifl5_calculators.DistanceCalculator;
import rifl5_calculators.FullPriceCalculator;
import rifl5_calculators.NetPriceCalculator;
import rifl5_calculators.OrderPriceCalculator;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
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
	private static Order order;
	private static SenderGUI gui;

	public static void main(String[] args) {
		try {
			
			
			ActorSystem system = ActorSystem.create("Hello");
			
			
//			system.actorOf(Props.create(Terminator.class, orderPriceRef), "orderterminator");
//			system.actorOf(Props.create(Terminator.class, distRef), "orderterminator");
			
			
			ActorRef fullRef = system.actorOf(Props.create(FullPriceCalculator.class), "fullprice");
			ActorRef netRef = system.actorOf(Props.create(NetPriceCalculator.class, fullRef), "net");
			ActorRef delivRef = system.actorOf(Props.create(DeliveryCalculator.class, fullRef), "deliv");
			ActorRef discRef = system.actorOf(Props.create(DiscountCalculator.class, netRef), "discount");
			ActorRef distRef = system.actorOf(Props.create(DistanceCalculator.class, delivRef), "distance");
			ActorRef orderPriceRef = system.actorOf(Props.create(OrderPriceCalculator.class, distRef,discRef), "orderprice");
			gui = new SenderGUI("Order Creator");
			
//			System.out.println("Type \"send\" to send an order request");
//			System.out.println("Type \"quit\" to exit");
			while (!exit) {
				//Wait for command
				while(command==Command.none) {
						Thread.sleep(100);
				}
				
				if (command==Command.exit) {
					System.out.println("Bye-bye!");
					exit = true;
				} else if (command==Command.generate) {
					order = generateRandomOrder();
					gui.setOrder(order);
				} else if (command==Command.send && order!=null) {
					OrderMessage message = new OrderMessage(order, Sender.Order);
					
					try {
						orderPriceRef.tell(serializeOrder(message), ActorRef.noSender());
						System.out.println("Order sent");

					} catch (Exception e) {
						System.err.println("Problem while sending order!");
						System.err.println(e.getMessage());
					}
					order = null;
				}
				command = Command.none;

			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		} 
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



