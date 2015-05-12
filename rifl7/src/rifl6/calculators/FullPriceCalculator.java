package rifl6.calculators;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import rifl6.RIFLCore;
import rifl6.RIFLCore.Command;
import rifl6.base.BaseCalculator;
import rifl6.base.Logger;
import rifl6.base.OrderGUI;
import rifl6.base.OrderMessage;
import rifl6.base.OrderMessage.Sender;
import datamodel.DeliveryData;
import datamodel.Order;
import datamodel.PriceData;

public class FullPriceCalculator extends BaseCalculator {
	
	
	private BlockingQueue<OrderMessage> delivOrders = new ArrayBlockingQueue<OrderMessage>(10000);
	
	public static int max = 0;

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

					Thread.sleep(10);
					
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
						//calculate(delivOrder, priceOrder);
						for (String data : delivOrder.getCalculationData().subList(1, delivOrder.getCalculationData().size())) {
							priceOrder.getCalculationData().add(data);
						}
						priceOrder.getCalculationData().add(Logger.getNext()+"");
						Logger.logOrderTiming(priceOrder);
						
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
		max--;
		if(max==(max/100)*100) {
			System.out.println("Maradt: "+max);
		}
		if(max<100) {
			System.out.println("Maradt: "+max);
		}
		if(max==0) {
			System.out.println("Finished");
			Logger.flush();
		}
//		if(AUTOMATIC && FULL_CONSOLE_LOG)
//			System.out.println(msg);
//		else if (AUTOMATIC && !FULL_CONSOLE_LOG)
//			System.out.println("Finished order: "+msg.getOrder().getId());
	}
	
	protected void calculate(Order order, Order priceorder) throws InterruptedException {
		startTiming();
		int waitTime = Logger.getNext();
		
		DeliveryData deliveryData = order.getDeliveryData();
		PriceData priceData = priceorder.getPriceData();
				
		double tempPrice = priceData.getPrice();
		double tempNetPrice = priceData.getNetPrice();

		priceData.setPrice(tempPrice + deliveryData.getDeliveryCost());
		priceData.setNetPrice(tempNetPrice + deliveryData.getDeliveryCost());
		
		// Az els�t lesz�m�tva az �sszes kelleni fog (a k�t �gon lehetnek egyez� �rt�kek)
		for (String data : order.getCalculationData().subList(1, order.getCalculationData().size())) {
			priceorder.getCalculationData().add(data);
		}
		priceorder.getCalculationData().add((endTiming()+waitTime)+"");
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
