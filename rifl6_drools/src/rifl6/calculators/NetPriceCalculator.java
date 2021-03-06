package rifl6.calculators;

import java.io.IOException;

import com.sample.DroolsManager.Event.ProcessType;

import rifl6.base.BaseCalculator;
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

		type = ProcessType.NET;
		if(!AUTOMATIC)
			gui = new OrderGUI("Net Price Calculator",2);
	}

	protected void calculate(Order order) throws InterruptedException {
		PriceData data = order.getPriceData();
		data.setNetPrice(data.getPrice() / netModifier);
//		Thread.sleep(500);
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
