package rifl5_calculators;

import java.io.IOException;

import rifl5_base.BaseCalculator;
import rifl5_base.OrderMessage;
import rifl5_base.OrderMessage.Sender;
import akka.actor.ActorRef;
import datamodel.Item;
import datamodel.Order;

public class OrderPriceCalculator extends BaseCalculator {

	ActorRef distanceRef;
	ActorRef discountRef;
	

	public OrderPriceCalculator(ActorRef dist, ActorRef disc){
		distanceRef = dist;
		discountRef = disc;
	}

	@Override
	protected void calculate(Order order) throws InterruptedException {
		double tempPrice = 0;
		Thread.sleep(500);
		for (Item item : order.getItems()) {
			tempPrice += item.getPrice();
		}

		order.getPriceData().setPrice(tempPrice);
		
		
	}

	@Override
	protected void send(OrderMessage msg) {
		try {
			msg.setSender(Sender.Order);
			distanceRef.tell(serializeOrder((OrderMessage) msg), getSelf());
			discountRef.tell(serializeOrder((OrderMessage) msg), getSelf());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	
}
