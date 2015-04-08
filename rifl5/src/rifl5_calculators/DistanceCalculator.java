package rifl5_calculators;


import java.io.IOException;

import rifl5_base.BaseCalculator;
import rifl5_base.OrderGUI;
import rifl5_base.OrderMessage;
import rifl5_base.OrderMessage.Sender;
import akka.actor.ActorRef;
import datamodel.CustomerData;
import datamodel.DeliveryData;
import datamodel.Order;

public class DistanceCalculator extends BaseCalculator{
	private static double northDeliveryCost = 1500;
	private static double southDeliveryCost = 2500;
	private static double eastDeliveryCost = 3500;
	private static double westDeliveryCost = 4500;
	private static double centralDeliveryCost = 500;
	
	ActorRef deliveryRef;
		
	
	public DistanceCalculator(ActorRef ref){
		deliveryRef = ref;
		gui = new OrderGUI("Distance Calculator");
	}
	
	protected void calculate(Order order) throws InterruptedException {
		DeliveryData deliveryData = order.getDeliveryData();
		CustomerData customerData = order.getCustomerData();
		switch (customerData.getRegion()) {
		case North:
			deliveryData.setDeliveryCost(northDeliveryCost);
			break;
		case South:
			deliveryData.setDeliveryCost(southDeliveryCost);
			break;
		case East:
			deliveryData.setDeliveryCost(eastDeliveryCost);
			break;
		case West:
			deliveryData.setDeliveryCost(westDeliveryCost);
			break;
		case Central:
			deliveryData.setDeliveryCost(centralDeliveryCost);
			break;

		default:
			break;
		}
		
		
	}

	@Override
	protected void send(OrderMessage msg) {
		try {
			msg.setSender(Sender.Distance);
			deliveryRef.tell(serializeOrder((OrderMessage) msg), getSelf());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
