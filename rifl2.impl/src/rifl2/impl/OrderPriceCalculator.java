package rifl2.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import rifl2.datamodel.Item;
import rifl2.datamodel.Order;
import rifl2.interfaces.IDiscountCalculator;
import rifl2.interfaces.IDistanceCalculator;
import rifl2.interfaces.IOrderPriceCalculator;

public class OrderPriceCalculator implements IOrderPriceCalculator{

	private boolean isrunning;
	private BlockingQueue<Order> Queue;
	private boolean exit;
	
	private IDiscountCalculator discount;
	private IDistanceCalculator distance;


	@Override
	public void setRunning(boolean running) {
		isrunning =running;

	}

	@Override
	public void enQueue(Order order) {
		Queue.add(order);

	}
	
	private void calculateOrderPrice(Order order) throws InterruptedException {
		double tempPrice = 0;
		Thread.sleep(500);
		for (Item item : order.getItems()) {
			tempPrice += item.getPrice();
		}

		order.getPriceData().setPrice(tempPrice);
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
							System.out.println("*************Calculating full price ******************");
							System.out.println("Order price before: "+order.getPriceData().getPrice());
							calculateOrderPrice(order);
							System.out.println("Order price before: "+order.getPriceData().getPrice());
							// After the logic has been executed insert the
							// product into the next production units
							// This is done via traversing through the NextPanel
							// attribute of the owning panel
							if(discount != null)
							{
								discount.enQueue(order);
							}
							if(distance != null)
							{
								distance.enQueue(order);
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
	public void setDiscount(IDiscountCalculator calc)
	{
		discount = calc;
	}
	@Override
	public void setDistance(IDistanceCalculator calc)
	{
		distance = calc;
	}
	

	

}
