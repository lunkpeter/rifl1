package rifl6.calculators;


import java.io.IOException;

import com.sample.DroolsManager.Event.ProcessType;

import rifl6.base.BaseCalculator;
import rifl6.base.OrderGUI;
import rifl6.base.OrderMessage;
import rifl6.base.OrderMessage.Sender;
import datamodel.Order;
import datamodel.PriceData;

public class DiscountCalculator extends BaseCalculator {
	
	private static double priceThreshold1 = 100000;
	private static double priceThreshold2 = 200000;
	BaseCalculator netPriceRef;

	int a = 0;
	
	public DiscountCalculator(BaseCalculator ref){
		netPriceRef = ref;
		type = ProcessType.DISCOUNT;
		if(!AUTOMATIC)
			gui = new OrderGUI("Discount Calculator",1);
	}

	protected void calculate(Order order) throws InterruptedException {
		PriceData data = order.getPriceData();
		double tempPrice = data.getPrice();
//		Thread.sleep(1500);
		if (data.getPrice() >= priceThreshold1) {
			if (data.getPrice() >= priceThreshold2) {
				data.setPrice(tempPrice * 0.8);
			} else {
				data.setPrice(tempPrice * 0.9);
			}
		}
		a++;
		if(a>5) {
			Thread.sleep(1500);
			a=0;
		}
		

	}

	@Override
	protected void send(OrderMessage msg) {
		try {
			msg.setSender(Sender.Discount);
			netPriceRef.addOrder(serializeOrder((OrderMessage) msg));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
