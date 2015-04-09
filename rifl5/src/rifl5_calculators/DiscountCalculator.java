package rifl5_calculators;


import java.io.IOException;

import rifl5_base.BaseCalculator;
import rifl5_base.OrderGUI;
import rifl5_base.OrderMessage;
import rifl5_base.OrderMessage.Sender;
import akka.actor.ActorRef;
import datamodel.Order;
import datamodel.PriceData;

public class DiscountCalculator extends BaseCalculator {
	
	private static double priceThreshold1 = 100000;
	private static double priceThreshold2 = 200000;
	ActorRef netPriceRef;

	public DiscountCalculator(ActorRef ref){
		netPriceRef = ref;
		gui = new OrderGUI("Discount Calculator",1);
	}

	protected void calculate(Order order) throws InterruptedException {
		PriceData data = order.getPriceData();
		double tempPrice = data.getPrice();
		Thread.sleep(500);
		if (data.getPrice() >= priceThreshold1) {
			if (data.getPrice() >= priceThreshold2) {
				data.setPrice(tempPrice * 0.8);
			} else {
				data.setPrice(tempPrice * 0.9);
			}
		}
		
		

	}

	@Override
	protected void send(OrderMessage msg) {
		try {
			msg.setSender(Sender.Discount);
			netPriceRef.tell(serializeOrder((OrderMessage) msg), getSelf());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
