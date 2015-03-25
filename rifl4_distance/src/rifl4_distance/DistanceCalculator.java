package rifl4_distance;

import javax.jms.JMSException;

import rifl4_base.BaseCalculator;
import datamodel.CustomerData;
import datamodel.DeliveryData;
import datamodel.Order;

public class DistanceCalculator extends BaseCalculator{
	private static double northDeliveryCost = 1500;
	private static double southDeliveryCost = 2500;
	private static double eastDeliveryCost = 3500;
	private static double westDeliveryCost = 4500;
	private static double centralDeliveryCost = 500;
	
	private static final String IN_TOPIC_NAME = "topic/order";
	private static final String OUT_TOPIC_NAME = "topic/distance";
	
	private static final String IN_QUEUE_NAME = "queue/order_delivery";
	private static final String OUT_QUEUE_NAME = "queue/distance";
	
	
	public DistanceCalculator() throws JMSException{
		if(mode==MessagingMode.Topic)
			setConnection(IN_TOPIC_NAME, OUT_TOPIC_NAME);
		else
			setConnection(IN_QUEUE_NAME, OUT_QUEUE_NAME);
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
}
