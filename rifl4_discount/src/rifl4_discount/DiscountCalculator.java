package rifl4_discount;

import javax.jms.JMSException;

import rifl4_base.BaseCalculator;
import datamodel.Order;
import datamodel.PriceData;

public class DiscountCalculator extends BaseCalculator {
	private static final String IN_TOPIC_NAME = "topic/order";
	private static final String OUT_TOPIC_NAME = "topic/discount";
	
	private static final String IN_QUEUE_NAME = "queue/order_discount";
	private static final String OUT_QUEUE_NAME = "queue/discount";
	
	private static double priceThreshold1 = 100000;
	private static double priceThreshold2 = 200000;

	public DiscountCalculator() throws JMSException {
		if(mode==MessagingMode.Topic)
			setConnection(IN_TOPIC_NAME, OUT_TOPIC_NAME);
		else
			setConnection(IN_QUEUE_NAME, OUT_QUEUE_NAME);
	}

	protected void calculate(Order order) throws InterruptedException {
		PriceData data = order.getPriceData();
		double tempPrice = data.getPrice();
		Thread.sleep(500);
		if (data.getPrice() >= priceThreshold1) {
			if (data.getPrice() >= priceThreshold2) {
				data.setPrice(tempPrice * 0.8);
			} else {
				data.setPrice(tempPrice * 0.9);
			}
		}

	}
}
