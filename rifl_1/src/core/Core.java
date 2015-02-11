package core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nodes.DeliveryCalculator;
import nodes.DiscountCalculator;
import nodes.DistancePriceCalculator;
import nodes.FullPriceCalculator;
import nodes.NetPriceCalculator;
import nodes.Node;
import nodes.OrderPriceCalculator;
import datamodel.CustomerData;
import datamodel.DeliveryData;
import datamodel.DeliveryMethod;
import datamodel.Item;
import datamodel.Order;
import datamodel.PriceData;
import datamodel.Region;

public class Core {

	public static void main(String[] args) {
		FullPriceCalculator fullpc = new FullPriceCalculator();
		NetPriceCalculator netpc = new NetPriceCalculator(fullpc);
		DeliveryCalculator deliverypc = new DeliveryCalculator(fullpc);
		DistancePriceCalculator distancepc = new DistancePriceCalculator(
				deliverypc);
		DiscountCalculator discountpc = new DiscountCalculator(netpc);

		List<Node> tempnodes = new ArrayList<Node>();
		tempnodes.add(distancepc);
		tempnodes.add(discountpc);

		OrderPriceCalculator opc = new OrderPriceCalculator(tempnodes);

		Thread orderPriceThread = new Thread(opc);
		Thread distanceThread = new Thread(distancepc);
		Thread discountThread = new Thread(discountpc);
		Thread deliveryThread = new Thread(deliverypc);
		Thread netThread = new Thread(netpc);
		Thread fullThread = new Thread(fullpc);

		orderPriceThread.start();
		distanceThread.start();
		discountThread.start();
		deliveryThread.start();
		netThread.start();
		fullThread.start();

		try {
			System.in.read();

			CustomerData customerData = new CustomerData("TEST", Region.Central);
			DeliveryData deliveryData = new DeliveryData(
					DeliveryMethod.PrivateDelivery);
			PriceData priceData = new PriceData();
			Order order = new Order(customerData, deliveryData, priceData);
			for (int i = 0; i < 10; i++) {
				order.addItem(new Item(25000, "TEST" + i));
			}
			System.out.println("adding started");
			opc.Queue.put(order);
			System.out.println("added");
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			
		while (true) {
			Order order;
			try {
				order = fullpc.OutList.take();
				System.out.println(order.getPriceData().getNetPrice()+" "+order.getPriceData().getPrice());
				opc.Queue.put(order);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}	



		
	}

}
