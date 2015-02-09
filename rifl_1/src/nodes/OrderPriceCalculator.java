package nodes;

import datamodel.Item;
import datamodel.Order;

public class OrderPriceCalculator {
	
	public void calculateOrderPrice(Order order){
		double tempPrice = 0;
		for (Item item : order.getItems()) {
			tempPrice += item.getPrice();
		}
		
		order.getPriceData().setPrice(tempPrice);
	}
}
