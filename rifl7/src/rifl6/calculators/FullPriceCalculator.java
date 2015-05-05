package rifl6.calculators;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rifl6.base.BaseCalculator;
import rifl6.base.Logger;
import rifl6.base.OrderGUI;
import rifl6.base.OrderMessage;
import rifl6.base.OrderMessage.Sender;
import datamodel.DeliveryData;
import datamodel.Order;
import datamodel.PriceData;

public class FullPriceCalculator extends BaseCalculator {
	
	
	private List<OrderMessage> delivOrders = new ArrayList<OrderMessage>();

	public FullPriceCalculator(){
		if(!AUTOMATIC)
			gui = new OrderGUI("Full Price Calculator",7);
	}
	
	@Override
	public void run() {
		isrunning=true;
		while(isrunning) {
			if(orders.size()>0 && delivOrders.size()>0) {
				try {
					OrderMessage delivOrderMessage = null;
					OrderMessage priceOrderMessage = null;

					for (OrderMessage dO : delivOrders) {
						for (OrderMessage pO : orders) {
							if(dO.getOrder().getId()==pO.getOrder().getId()) {
								delivOrderMessage=dO;
								priceOrderMessage=pO;
							}
						}
					}
					if(delivOrderMessage != null 
							&& priceOrderMessage != null){
						Order priceOrder = priceOrderMessage.getOrder();
						Order delivOrder = delivOrderMessage.getOrder();
						
						
						if(AUTOMATIC || gui == null) {
							
						} else {
							gui.setOrder(priceOrder);
							while(!gui.canCalculate)
								Thread.sleep(100);
							gui.canCalculate = false;
						}
						calculate(delivOrder, priceOrder);
						
						if(AUTOMATIC || gui == null) {
							
						} else {
							gui.setAfter(priceOrder);
							while(!gui.canSend)
								Thread.sleep(100);
							gui.canSend = false;
						}
						send(priceOrderMessage);

												
						delivOrders.remove(delivOrderMessage);
						orders.remove(priceOrderMessage);
					} else {
						Thread.sleep(100);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void send(OrderMessage msg) {
		msg.setSender(Sender.Full);
		if(AUTOMATIC && FULL_CONSOLE_LOG)
			System.out.println(msg);
		else if (AUTOMATIC && !FULL_CONSOLE_LOG)
			System.out.println("Finished order: "+msg.getOrder().getId());
	}
	
	protected void calculate(Order order, Order priceorder) throws InterruptedException {
		startTiming();
		Thread.sleep(Logger.getNext());
		
		DeliveryData deliveryData = order.getDeliveryData();
		PriceData priceData = priceorder.getPriceData();
				
		double tempPrice = priceData.getPrice();
		double tempNetPrice = priceData.getNetPrice();

		priceData.setPrice(tempPrice + deliveryData.getDeliveryCost());
		priceData.setNetPrice(tempNetPrice + deliveryData.getDeliveryCost());
		
		for (String data : order.getCalculationData()) {
			if(!priceorder.getCalculationData().contains(data)){
				priceorder.getCalculationData().add(data);
			}
		}
		priceorder.getCalculationData().add(endTiming()+"");
		Logger.logOrderTiming(priceorder);
	}

	@Override
	protected void calculate(Order order) throws InterruptedException {
		
	}
	
	public void addPriceOrder(byte[] msg) {
		addOrder(msg);
	}
	
	public void addDeliveryOrder(byte[] msg) {
		if (msg instanceof byte[]) {
			try {
				OrderMessage orderMessage = deserializeOrder((byte[]) msg);
				delivOrders.add(orderMessage);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
}
