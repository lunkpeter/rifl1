package rifl2.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import rifl2.datamodel.DeliveryData;
import rifl2.datamodel.Order;
import rifl2.datamodel.PriceData;
import rifl2.interfaces.IFullPriceCalculator;

public class FullPriceCalculator implements IFullPriceCalculator{

	private BlockingQueue<Order> PriceQueue;
	private boolean isrunning;
	private BlockingQueue<Order> Queue;
	private boolean exit;
	
	
	
	@Override
	public void setRunning(boolean running) {
		isrunning = running;
		
	}

	@Override
	public void enQueue(Order order) {
		Queue.add(order);
	}


	@Override
	public void enQueuePrice(Order order) {
		PriceQueue.add(order);
	}
	
	@Override
	public void init() {
		isrunning = false;
		exit = false;
		Queue = new ArrayBlockingQueue<Order>(1);
		PriceQueue = new ArrayBlockingQueue<Order>(1);
		Runnable run = new Runnable() {

			@Override
			public void run() {
				while (!exit) {
					if (isrunning) {
						Order delivorder;
						Order priceorder;
						try {
							delivorder = Queue.take();
							priceorder = PriceQueue.take();
							System.out.println("*************Calculating full price ******************");
							System.out.println("Order price before: "+priceorder.getPriceData().getPrice());
							calculateFullPrice(delivorder.getDeliveryData(),
									priceorder.getPriceData());
							System.out.println("Order price after: "+priceorder.getPriceData().getPrice());
							isrunning = false;
							
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}else {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}
			}
		};
		Thread thread = new Thread(run);
		thread.start();
	}
	
	private void calculateFullPrice(DeliveryData deliveryData,
			PriceData priceData) throws InterruptedException {
		Thread.sleep(500);
		double tempPrice = priceData.getPrice();
		double tempNetPrice = priceData.getNetPrice();

		priceData.setPrice(tempPrice + deliveryData.getDeliveryCost());
		priceData.setNetPrice(tempNetPrice + deliveryData.getDeliveryCost());
	}

}
