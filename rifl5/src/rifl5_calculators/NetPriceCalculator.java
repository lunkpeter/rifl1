package rifl5_calculators;

import java.io.IOException;

import akka.actor.ActorRef;
import rifl5_base.BaseCalculator;
import rifl5_base.OrderMessage;
import rifl5_base.OrderMessage.Sender;
import datamodel.Order;
import datamodel.PriceData;

public class NetPriceCalculator extends BaseCalculator {

	private static double netModifier = 1.27;
	ActorRef fullPriceRef;
	
	public NetPriceCalculator(ActorRef ref){
		fullPriceRef = ref;
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
