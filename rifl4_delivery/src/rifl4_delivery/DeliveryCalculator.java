package rifl4_delivery;

import javax.jms.JMSException;

import rifl4_base.BaseCalculator;
import datamodel.DeliveryData;
import datamodel.Order;
import datamodel.PriceData;

public class DeliveryCalculator extends BaseCalculator{
	private static final String IN_TOPIC_NAME = "topic/distance";
	private static final String OUT_TOPIC_NAME = "topic/delivery";

	private static final String IN_QUEUE_NAME = "queue/distance";
	private static final String OUT_QUEUE_NAME = "queue/delivery";

	private static double priceThreshold = 50000;
	
	public DeliveryCalculator() throws JMSException{
		if(mode==MessagingMode.Topic)
			setConnection(IN_TOPIC_NAME, OUT_TOPIC_NAME);
		else
			setConnection(IN_QUEUE_NAME, OUT_QUEUE_NAME);
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
