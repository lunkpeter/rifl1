package nodes;

import java.util.List;

import datamodel.Item;
import datamodel.Order;

public class OrderPriceCalculator extends Node{
	
	

	private void calculateOrderPrice(Order order) throws InterruptedException{
		double tempPrice = 0;
		Thread.sleep(500);
		for (Item item : order.getItems()) {
			tempPrice += item.getPrice();
		}
		
		order.getPriceData().setPrice(tempPrice);
	}
	
	public OrderPriceCalculator(Node nextNode) {
		super(nextNode);
	}
	
	public OrderPriceCalculator(List<Node> nextNode) {
		super(nextNode);
	}
	
	public OrderPriceCalculator() {
		super();
	}

	@Override
	public void run() {
		while (!exit) {
			Order order;
			try {
				order = Queue.take();
				System.out.println("calculating order price");
				calculateOrderPrice(order);
				for (Node node : NextNodes) {
					node.Queue.put(order);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
}
