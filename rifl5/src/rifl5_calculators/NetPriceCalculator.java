package rifl5_calculators;

import java.io.IOException;

import rifl5_base.BaseCalculator;
import rifl5_base.OrderGUI;
import rifl5_base.OrderMessage;
import rifl5_base.OrderMessage.Sender;
import akka.actor.ActorRef;
import datamodel.Order;
import datamodel.PriceData;

public class NetPriceCalculator extends BaseCalculator {

	private static double netModifier = 1.27;
	ActorRef fullPriceRef;
	
	public NetPriceCalculator(ActorRef ref){
		fullPriceRef = ref;
		gui = new OrderGUI("Net Price Calculator",2);
	}

	protected void calculate(Order order) throws InterruptedException {
		PriceData data = order.getPriceData();
		data.setNetPrice(data.getPrice() / netModifier);
		
		
	}

	@Override
	protected void send(OrderMessage msg) {
		try {
			msg.setSender(Sender.Net);
			fullPriceRef.tell(serializeOrder((OrderMessage) msg), getSelf());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
