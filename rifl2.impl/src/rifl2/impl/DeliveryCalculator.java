package rifl2.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import rifl2.datamodel.DeliveryData;
import rifl2.datamodel.Order;
import rifl2.datamodel.PriceData;
import rifl2.interfaces.IDeliveryCalculator;
import rifl2.interfaces.IFullPriceCalculator;

public class DeliveryCalculator implements IDeliveryCalculator {

	private boolean isrunning;
	private BlockingQueue<Order> Queue;
	private boolean exit;
	private static double priceThreshold = 50000;
	
	private IFullPriceCalculator next;

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
							System.out.println("*************Calculating delivery price ******************");
							System.out.println("Delivery cost before: "+order.getDeliveryData().getDeliveryCost());
							
							// Execute the business logic separated in a method
							calculateDelivery(order.getDeliveryData(),
									order.getPriceData());
							
							System.out.println("Delivery cost after: "+order.getDeliveryData().getDeliveryCost());
							
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


	private void calculateDelivery(DeliveryData data, PriceData priceData)
			throws InterruptedException {
		Thread.sleep(500);
		double tempPrice = data.getDeliveryCost();
		switch (data.getDeliveryMethod()) {
		case PostalDelivery:
			if (priceData.getPrice() >= priceThreshold) {
				data.setDeliveryCost(0);
			} else {
				data.setDeliveryCost(tempPrice);
			}
			break;
		case PrivateDelivery:
			if (priceData.getPrice() >= priceThreshold) {
				data.setDeliveryCost(0);
			} else {
				data.setDeliveryCost(tempPrice * 3);
			}
			break;
		case TakeAway:
			data.setDeliveryCost(0);
			break;

		default:
			break;
		}
	}
	
	public void setFullPrice(IFullPriceCalculator calc)
	{
		next = calc;
	}

}
