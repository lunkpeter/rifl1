package rifl6.calculators;


import java.io.IOException;

import rifl6.base.BaseCalculator;
import rifl6.base.Logger;
import rifl6.base.OrderGUI;
import rifl6.base.OrderMessage;
import rifl6.base.OrderMessage.Sender;
import datamodel.DeliveryData;
import datamodel.DeliveryMethod;
import datamodel.Order;
import datamodel.PriceData;

public class DeliveryCalculator extends BaseCalculator{

	private static double priceThreshold = 50000;
	
	BaseCalculator fullPriceRef;
	
	public DeliveryCalculator(BaseCalculator ref, DeliveryMethod method){
		fullPriceRef = ref;
		int offset = 0;
		String t = "Postal";
		if(method==DeliveryMethod.PrivateDelivery) {
			offset=1;
			t = "Private";
		}
		else if(method==DeliveryMethod.TakeAway) {
			offset=2;
			t = "Take Away";
		}
		if(!AUTOMATIC)
			gui = new OrderGUI(t+" Delivery Calculator",4+offset);
	}
	
	protected void calculate(Order order) throws InterruptedException {
		startTiming();	
		int waitTime = Logger.getNext();
		
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
		order.getCalculationData().add((endTiming()+waitTime)+"");
	}

	@Override
	protected void send(OrderMessage msg) {
		try {
			msg.setSender(Sender.Delivery);
			((FullPriceCalculator)fullPriceRef).addDeliveryOrder(serializeOrder((OrderMessage) msg));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
