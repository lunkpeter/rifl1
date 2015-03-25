package rifl4_netprice;

import javax.jms.JMSException;

import rifl4_base.BaseCalculator;
import datamodel.Order;
import datamodel.PriceData;

public class NetPriceCalculator extends BaseCalculator {
	private static final String IN_TOPIC_NAME = "topic/discount";
	private static final String OUT_TOPIC_NAME = "topic/net";

	private static final String IN_QUEUE_NAME = "queue/discount";
	private static final String OUT_QUEUE_NAME = "queue/net";

	private static double netModifier = 1.27;

	public NetPriceCalculator() throws JMSException {
		if(mode==MessagingMode.Topic)
			setConnection(IN_TOPIC_NAME, OUT_TOPIC_NAME);
		else
			setConnection(IN_QUEUE_NAME, OUT_QUEUE_NAME);
	}

	protected void calculate(Order order) throws InterruptedException {
		PriceData data = order.getPriceData();
		data.setNetPrice(data.getPrice() / netModifier);
	}
}
