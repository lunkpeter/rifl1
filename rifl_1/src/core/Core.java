package core;

import java.io.IOException;

import nodes.DeliveryCalculator;
import nodes.DiscountCalculator;
import nodes.DistancePriceCalculator;
import nodes.FullPriceCalculator;
import nodes.NetPriceCalculator;
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
		CustomerData customerData = new CustomerData("TEST", Region.Central);
		DeliveryData deliveryData = new DeliveryData(DeliveryMethod.PrivateDelivery);
		PriceData priceData = new PriceData();
		Order order = new Order(customerData, deliveryData, priceData);
		for (int i = 0; i < 10; i++) {
			order.addItem(new Item(25000, "TEST"+i));
		}
		
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		new OrderPriceCalculator().calculateOrderPrice(order);
		new DistancePriceCalculator().calculateDistance(order.getDeliveryData(), order.getCustomerData());
		new DeliveryCalculator().calculateDelivery(order.getDeliveryData(), order.getPriceData());
		new DiscountCalculator().caclulateDiscount(order.getPriceData());
		new NetPriceCalculator().calculateNetPrice(order.getPriceData());
		new FullPriceCalculator().calculateFullPrice(order.getDeliveryData(), order.getPriceData());
		
		
		System.out.println(order.getPriceData().getPrice());
		
	}
}
