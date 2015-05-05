package rifl6.calculators;

import java.io.IOException;

import rifl6.base.BaseCalculator;
import rifl6.base.Logger;
import rifl6.base.OrderGUI;
import rifl6.base.OrderMessage;
import rifl6.base.OrderMessage.Sender;
import datamodel.Item;
import datamodel.Order;

public class OrderPriceCalculator extends BaseCalculator {

	BaseCalculator distanceRef;
	BaseCalculator discountRef;
	

	public OrderPriceCalculator(BaseCalculator dist, BaseCalculator disc){
		distanceRef = dist;
		discountRef = disc;
		if(!AUTOMATIC)
			gui = new OrderGUI("Order Price Calculator",0);
	}

	@Override
	protected void calculate(Order order) throws InterruptedException {
		startTiming();
		Thread.sleep(Logger.getNext());
		
		double tempPrice = 0;
		for (Item item : order.getItems()) {
			tempPrice += item.getPrice();
		}

		order.getPriceData().setPrice(tempPrice);
		
		order.getCalculationData().add(endTiming()+"");
	}

	@Override
	protected void send(OrderMessage msg) {
		try {
			msg.setSender(Sender.Order);
			distanceRef.addOrder(serializeOrder((OrderMessage) msg));
			discountRef.addOrder(serializeOrder((OrderMessage) msg));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	
}
