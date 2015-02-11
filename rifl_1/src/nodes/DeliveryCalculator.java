package nodes;

import java.util.List;

import datamodel.DeliveryData;
import datamodel.Order;
import datamodel.PriceData;

public class DeliveryCalculator extends Node{
	
	private static double priceThreshold = 50000;
	
	public DeliveryCalculator(Node next){
		super(next);
	}
	
	public DeliveryCalculator(List<Node> next){
		super(next);
	}
	
	
	public DeliveryCalculator() {
		super();
	}

	private void calculateDelivery(DeliveryData data, PriceData priceData) throws InterruptedException {
		Thread.sleep(500);
		double tempPrice = data.getDeliveryCost();
		switch (data.getDeliveryMethod()) {
		case PostalDelivery:
			if(priceData.getPrice() >= priceThreshold){
				data.setDeliveryCost(0);
			}else {
				data.setDeliveryCost(tempPrice);
			}
			break;
		case PrivateDelivery:
			if(priceData.getPrice() >= priceThreshold){
				data.setDeliveryCost(0);
			}else {
				data.setDeliveryCost(tempPrice*3);
			}
			break;
		case TakeAway:
			data.setDeliveryCost(0);
			break;

		default:
			break;
		}
	}

	@Override
	public void run() {
		while (!exit) {
			Order order;
			try {
				order = Queue.take();
				System.out.println("calculating delivery");
				calculateDelivery(order.getDeliveryData(), order.getPriceData());
				for (Node node : NextNodes) {
					node.Queue.put(order);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
}
