package rifl2.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import rifl2.datamodel.Order;
import rifl2.datamodel.PriceData;
import rifl2.interfaces.IDiscountCalculator;
import rifl2.interfaces.INetPriceCalculator;

public class DiscountCalculator implements IDiscountCalculator{

	private boolean isrunning;
	private BlockingQueue<Order> Queue;
	private boolean exit;
	private static double priceThreshold1 = 100000;
	private static double priceThreshold2 = 200000;
	
	private INetPriceCalculator next;

	public void init() {
		isrunning = false;
		exit = false;
		Queue = new ArrayBlockingQueue<Order>(1);
		Runnable run = new Runnable() {

			@Override
			public void run() {
				while (!exit) {
					if (isrunning) {
						Order order;
						try {
							// Fetch the next order from the input queue
							order = Queue.take();
							// Execute the business logic separated in a method
							System.out.println("*************Calculating discount ******************");
							System.out.println("Order price before: "+order.getPriceData().getPrice());
							caclulateDiscount(
									order.getPriceData());
							System.out.println("Order price after: "+order.getPriceData().getPrice());
							// After the logic has been executed insert the
							// product into the next production units
							// This is done via traversing through the NextPanel
							// attribute of the owning panel
							if(next != null)
							{
								next.enQueue(order);
							}
							// after the calculation is complete set the
							// isrunning flag accordingly
							isrunning = false;
							// Update the UI
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						// if the running flag is not set, suspend data
						// processing and wait instead
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

	@Override
	public void setRunning(boolean running) {
		isrunning =running;

	}

	@Override
	public void enQueue(Order order) {
		Queue.add(order);

	}
	
	private void caclulateDiscount(PriceData data) throws InterruptedException {
		double tempPrice = data.getPrice();
		Thread.sleep(500);
		if (data.getPrice() >= priceThreshold1) {
			if (data.getPrice() >= priceThreshold2) {
				data.setPrice(tempPrice * 0.8);
			} else {
				data.setPrice(tempPrice * 0.9);
			}
		}

	}
	
	public void setNetPrice(INetPriceCalculator calc)
	{
		next = calc;
	}

}
