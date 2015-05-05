package rifl6.calculators;


import java.io.IOException;



import rifl6.base.BaseCalculator;
import rifl6.base.Logger;
import rifl6.base.OrderGUI;
import rifl6.base.OrderMessage;
import rifl6.base.OrderMessage.Sender;
import datamodel.CustomerData;
import datamodel.DeliveryData;
import datamodel.Order;

public class DistanceCalculator extends BaseCalculator{
	private static double northDeliveryCost = 1500;
	private static double southDeliveryCost = 2500;
	private static double eastDeliveryCost = 3500;
	private static double westDeliveryCost = 4500;
	private static double centralDeliveryCost = 500;
	
	BaseCalculator deliveryPostal;
	BaseCalculator deliveryPrivate;
	BaseCalculator takeAway;
		
	
	public DistanceCalculator(BaseCalculator deliveryPostal,
			BaseCalculator deliveryPrivate, BaseCalculator takeAway){
		this.deliveryPostal = deliveryPostal;
		this.deliveryPrivate = deliveryPrivate;
		this.takeAway = takeAway;
		if(!AUTOMATIC)
			gui = new OrderGUI("Distance Calculator",3);
	}
	
	protected void calculate(Order order) throws InterruptedException {
		startTiming();
		Thread.sleep(Logger.getNext());
		
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
		
		order.getCalculationData().add(endTiming()+"");
	}

	@Override
	protected void send(OrderMessage msg) {
		try {
			msg.setSender(Sender.Distance);
			switch (msg.getOrder().getDeliveryData().getDeliveryMethod()) {
			case PostalDelivery:
				deliveryPostal.addOrder(serializeOrder((OrderMessage) msg));
				break;

			case PrivateDelivery:
				deliveryPrivate.addOrder(serializeOrder((OrderMessage) msg));
				break;

			case TakeAway:
				takeAway.addOrder(serializeOrder((OrderMessage) msg));
				break;
				
			default:
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
