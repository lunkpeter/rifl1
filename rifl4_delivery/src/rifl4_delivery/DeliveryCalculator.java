package rifl4_delivery;

import javax.jms.JMSException;

import rifl4_base.BaseCalculator;
import datamodel.DeliveryData;
import datamodel.Order;
import datamodel.PriceData;

public class DeliveryCalculator extends BaseCalculator{
	private static final String IN_QUEUE_NAME = "distance";
	private static final String OUT_QUEUE_NAME = "delivery";

	private static double priceThreshold = 50000;
	
	public DeliveryCalculator(String brokerUrl) throws JMSException{
		setConnection(brokerUrl, IN_QUEUE_NAME, OUT_QUEUE_NAME);
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
}
