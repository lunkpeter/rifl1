package rifl6.calculators;


import java.io.IOException;

import com.sample.DroolsManager.Event.ProcessType;

import rifl6.base.BaseCalculator;
import rifl6.base.OrderGUI;
import rifl6.base.OrderMessage;
import rifl6.base.OrderMessage.Sender;
import datamodel.DeliveryData;
import datamodel.Order;
import datamodel.PriceData;

public class DeliveryCalculator extends BaseCalculator{

	private static double priceThreshold = 50000;
	
	BaseCalculator fullPriceRef;
	
	public DeliveryCalculator(BaseCalculator ref, ProcessType type){
		fullPriceRef = ref;
		this.type=type;
		if(!AUTOMATIC)
			gui = new OrderGUI(type+" delivery Calculator",4);
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
			((FullPriceCalculator)fullPriceRef).addDeliveryOrder(serializeOrder((OrderMessage) msg));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
