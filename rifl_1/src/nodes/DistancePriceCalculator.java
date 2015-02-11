package nodes;

import java.util.List;

import datamodel.CustomerData;
import datamodel.DeliveryData;
import datamodel.Order;

public class DistancePriceCalculator extends Node {
	private static double northDeliveryCost = 1500;
	private static double southDeliveryCost = 2500;
	private static double eastDeliveryCost = 3500;
	private static double westDeliveryCost = 4500;
	private static double centralDeliveryCost = 500;
	
	public DistancePriceCalculator(Node nextNode) {
		super(nextNode);
	}
	
	public DistancePriceCalculator(List<Node> nextNode) {
		super(nextNode);
	}
	
	public DistancePriceCalculator() {
		super();
	}
	
	private void calculateDistance(DeliveryData deliveryData,
			CustomerData customerData) throws InterruptedException {
		Thread.sleep(500);
		switch (customerData.getRegion()) {
		case North:
			deliveryData.setDeliveryCost(northDeliveryCost);
			break;
		case South:
			deliveryData.setDeliveryCost(southDeliveryCost);
			break;
		case East:
			deliveryData.setDeliveryCost(eastDeliveryCost);
			break;
		case West:
			deliveryData.setDeliveryCost(westDeliveryCost);
			break;
		case Central:
			deliveryData.setDeliveryCost(centralDeliveryCost);
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
				System.out.println("calculating distance");
				calculateDistance(order.getDeliveryData(), order.getCustomerData());
				for (Node node : NextNodes) {
					if(node instanceof FullPriceCalculator){
						((FullPriceCalculator)node).PriceQueue.add(order);
					}else {
						node.Queue.put(order);
					}
					
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			

		}
		
	}
}