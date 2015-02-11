package nodes;

import java.util.List;

import datamodel.Order;
import datamodel.PriceData;

public class DiscountCalculator extends Node{
	private static double priceThreshold1 = 100000;
	private static double priceThreshold2 = 200000;
	
	public DiscountCalculator(Node nextNode) {
		super(nextNode);
	}
	
	public DiscountCalculator(List<Node> nextNode) {
		super(nextNode);
	}

	public DiscountCalculator() {
		super();
	}
	
	private void caclulateDiscount(PriceData data) throws InterruptedException{
		double tempPrice = data.getPrice();
		Thread.sleep(500);
		if(data.getPrice() >= priceThreshold1){
			if(data.getPrice() >= priceThreshold2){
				data.setPrice(tempPrice*0.8);
			}else {
				data.setPrice(tempPrice*0.9);
			}
		}
		
	}

	@Override
	public void run() {
		while (!exit) {
			Order order;
			try {
				order = Queue.take();
				System.out.println("calculating discount");
				caclulateDiscount(order.getPriceData());
				for (Node node : NextNodes) {
						node.Queue.put(order);				
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
}
