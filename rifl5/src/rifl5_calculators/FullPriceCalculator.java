package rifl5_calculators;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rifl5_base.BaseCalculator;
import rifl5_base.OrderGUI;
import rifl5_base.OrderMessage;
import rifl5_base.OrderMessage.Sender;
import datamodel.DeliveryData;
import datamodel.Order;
import datamodel.PriceData;

public class FullPriceCalculator extends BaseCalculator {
	
	
	private List<Order> delivOrders = new ArrayList<Order>();
	private List<Order> priceOrders = new ArrayList<Order>();

	public FullPriceCalculator(){
		gui = new OrderGUI("Full Price Calculator");
	}

	
	protected void calculate(Order order, Order priceorder) throws InterruptedException {
		DeliveryData deliveryData = order.getDeliveryData();
		PriceData priceData = priceorder.getPriceData();
		double tempPrice = priceData.getPrice();
		double tempNetPrice = priceData.getNetPrice();

		priceData.setPrice(tempPrice + deliveryData.getDeliveryCost());
		priceData.setNetPrice(tempNetPrice + deliveryData.getDeliveryCost());
	}
	
	
	@Override
	public void onReceive(Object msg) {
		//add order to lists based on sender
		
		if (msg instanceof byte[]) {
			OrderMessage orderMessage;
			try {
				orderMessage = deserializeOrder((byte[]) msg);
				if(orderMessage.getSender().equals(Sender.Delivery)){
					delivOrders.add(orderMessage.getOrder());
					System.out.println(this.getClass().getName()+" Addied deliv" + orderMessage);
				}else if (orderMessage.getSender().equals(Sender.Net)) {
					priceOrders.add(orderMessage.getOrder());
					System.out.println(this.getClass().getName()+" added price" + orderMessage);
				}else {
					unhandled(msg);
				}
				
				Order delivOrder = null;
				Order priceOrder = null;
				
				for (Order dO : delivOrders) {
					for (Order pO : priceOrders) {
						if(dO.getId()==pO.getId()) {
							delivOrder=dO;
							priceOrder=pO;
						}
					}
				}
				if(delivOrder == null || priceOrder == null){
					return;
				}
				
				
				
				System.out.println(this.getClass().getName()+" BEFORE CALC" + orderMessage);
				gui.setOrder(priceOrder);
				while(!gui.canCalculate)
					Thread.sleep(100);
				gui.canCalculate = false;
				calculate(delivOrder, priceOrder);
				System.out.println(this.getClass().getName()+" AFTER CALC" + orderMessage);

				gui.setAfter(priceOrder);
				while(!gui.canSend)
					Thread.sleep(100);
				gui.canSend = false;
				send(orderMessage);
			} catch (ClassNotFoundException e) {
				unhandled(msg);
				e.printStackTrace();
			} catch (IOException e) {
				unhandled(msg);
				e.printStackTrace();
			} catch (InterruptedException e) {
				unhandled(msg);
				e.printStackTrace();
			}
		} else {
			unhandled(msg);
		}
		

		//getSender().tell(Msg.DONE, getSelf());
	
	}

	@Override
	protected void send(OrderMessage msg) {
		msg.setSender(Sender.Full);
		System.out.println(msg);
		
	}


	@Override
	protected void calculate(Order order) throws InterruptedException {
		
	}

	
}
