package nodes;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import datamodel.DeliveryData;
import datamodel.Order;
import datamodel.PriceData;

public class FullPriceCalculator extends Node{
	
	public BlockingQueue<Order> PriceQueue;
	public BlockingQueue<Order> OutList;
	
	public FullPriceCalculator() {
		super();
		PriceQueue = new ArrayBlockingQueue<Order>(1, true);
		OutList = new ArrayBlockingQueue<Order>(1, true);
	}
	
	public FullPriceCalculator(List<Node> nextNode) {
		super(nextNode);
		PriceQueue = new ArrayBlockingQueue<Order>(1, true);
		OutList = new ArrayBlockingQueue<Order>(1, true);
	}
	
	public FullPriceCalculator(Node nextNode) {
		super(nextNode);
		PriceQueue = new ArrayBlockingQueue<Order>(1, true);
		OutList = new ArrayBlockingQueue<Order>(1, true);
	}

	private void calculateFullPrice(DeliveryData deliveryData, PriceData priceData) throws InterruptedException{
		Thread.sleep(500);
		double tempPrice = priceData.getPrice();
		double tempNetPrice = priceData.getNetPrice();
		
		priceData.setPrice(tempPrice+deliveryData.getDeliveryCost());
		priceData.setNetPrice(tempNetPrice+deliveryData.getDeliveryCost());
	}

	@Override
	public void run() {
		while (!exit) {
			Order delivorder;
			Order priceorder;
			try {
				delivorder = Queue.take();
				priceorder = PriceQueue.take();
				System.out.println("calculating full price");
				calculateFullPrice(delivorder.getDeliveryData(), priceorder.getPriceData());
				OutList.put(priceorder);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
