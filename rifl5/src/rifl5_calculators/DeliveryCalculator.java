package rifl5_calculators;


import java.io.IOException;

import rifl5_base.BaseCalculator;
import rifl5_base.OrderMessage;
import rifl5_base.OrderMessage.Sender;
import akka.actor.ActorRef;
import datamodel.DeliveryData;
import datamodel.Order;
import datamodel.PriceData;

public class DeliveryCalculator extends BaseCalculator{

	private static double priceThreshold = 50000;
	
	ActorRef fullPriceRef;
	
	public DeliveryCalculator(ActorRef ref){
		fullPriceRef = ref;
	}
	
	protected void calculate(Order order) throws InterruptedException {
		DeliveryData data = order.getDeliveryData();
		PriceData priceData = order.getPriceData();
		double tempPrice = data.getDeliveryCost();
		switch (data.getDeliveryMethod()) {
		case PostalDelivery:
			if(priceData.getPrice() >= priceThreshold){
				data.setDeliveryCost(0);
			}else {
				data.setDeliveryCost(tempPrice);
			}
			break;
		case PrivateDelivery:
			if(priceData.getPrice() >= priceThreshold){
				data.setDeliveryCost(0);
			}else {
				data.setDeliveryCost(tempPrice*3);
			}
			break;
		case TakeAway:
			data.setDeliveryCost(0);
			break;

		default:
			break;
		}
		
	}

	@Override
	protected void send(OrderMessage msg) {
		try {
			msg.setSender(Sender.Delivery);
			fullPriceRef.tell(serializeOrder((OrderMessage) msg), getSelf());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	} 
}
