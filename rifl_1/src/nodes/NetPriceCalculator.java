package nodes;

import java.util.List;

import datamodel.Order;
import datamodel.PriceData;

public class NetPriceCalculator extends Node{
	

	private static double netModifier = 1.27;
	
	public NetPriceCalculator() {
		super();
	}
	
	public NetPriceCalculator(Node nextNode) {
		super(nextNode);
	}
	
	public NetPriceCalculator(List<Node> nextNode) {
		super(nextNode);
	}
	
	private void calculateNetPrice(PriceData data) throws InterruptedException{
		Thread.sleep(500);
		data.setNetPrice(data.getPrice()*netModifier);
	}

	@Override
	public void run() {
		while (!exit) {
			Order order;
			try {
				order = Queue.take();
				System.out.println("calculating net price");
				calculateNetPrice(order.getPriceData());
				for (Node node : NextNodes) {
					if(node instanceof FullPriceCalculator){
						((FullPriceCalculator)node).PriceQueue.put(order);
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
