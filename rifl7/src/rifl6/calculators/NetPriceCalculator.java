package rifl6.calculators;

import java.io.IOException;

import rifl6.base.BaseCalculator;
import rifl6.base.Logger;
import rifl6.base.OrderGUI;
import rifl6.base.OrderMessage;
import rifl6.base.OrderMessage.Sender;
import datamodel.Order;
import datamodel.PriceData;

public class NetPriceCalculator extends BaseCalculator {

	private static double netModifier = 1.27;
	BaseCalculator fullPriceRef;
	
	public NetPriceCalculator(BaseCalculator ref){
		fullPriceRef = ref;
		if(!AUTOMATIC)
			gui = new OrderGUI("Net Price Calculator",2);
	}

	protected void calculate(Order order) throws InterruptedException {
		startTiming();
		Thread.sleep(Logger.getNext());
		
		PriceData data = order.getPriceData();
		data.setNetPrice(data.getPrice() / netModifier);
		
		order.getCalculationData().add(endTiming()+"");
	}

	@Override
	protected void send(OrderMessage msg) {
		try {
			msg.setSender(Sender.Net);
			((FullPriceCalculator)fullPriceRef).addPriceOrder(serializeOrder((OrderMessage) msg));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
