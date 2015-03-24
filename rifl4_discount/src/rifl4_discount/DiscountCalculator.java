package rifl4_discount;

import javax.jms.JMSException;

import rifl4_base.BaseCalculator;
import datamodel.Order;
import datamodel.PriceData;

public class DiscountCalculator extends BaseCalculator {
	private static final String IN_QUEUE_NAME = "order_discount";
	private static final String OUT_QUEUE_NAME = "discount";
	private static double priceThreshold1 = 100000;
	private static double priceThreshold2 = 200000;

	public DiscountCalculator(String brokerUrl) throws JMSException {
		setConnection(brokerUrl, IN_QUEUE_NAME, OUT_QUEUE_NAME);
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
