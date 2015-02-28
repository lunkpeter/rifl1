package rifl2.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import rifl2.datamodel.Order;
import rifl2.datamodel.PriceData;
import rifl2.interfaces.IFullPriceCalculator;
import rifl2.interfaces.INetPriceCalculator;

public class NetPriceCalculator implements INetPriceCalculator{
	
	private boolean isrunning;
	private BlockingQueue<Order> Queue;
	private boolean exit;
	private static double netModifier = 1.27;
	
	private IFullPriceCalculator next;
	
	
	@Override
	public void setRunning(boolean running) {
		isrunning = running;
		
	}

	@Override
	public void enQueue(Order order) {
		Queue.add(order);
	}

	@Override
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
							System.out.println("*************Calculating net price ******************");
							System.out.println("Order net price before: "+order.getPriceData().getNetPrice());
							calculateNetPrice(order.getPriceData());
							System.out.println("Order net price before: "+order.getPriceData().getNetPrice());
							// After the logic has been executed insert the
							// product into the next production units
							// This is done via traversing through the NextPanel
							// attribute of the owning panel
							if(next != null)
							{
								next.enQueuePrice(order);
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
	
	private void calculateNetPrice(PriceData data) throws InterruptedException {
		Thread.sleep(500);
		data.setNetPrice(data.getPrice() / netModifier);
	}
	
	public void setFullPrice(IFullPriceCalculator calc)
	{
		next = calc;
	}

}
