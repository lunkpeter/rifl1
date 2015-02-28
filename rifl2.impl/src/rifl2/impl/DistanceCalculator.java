package rifl2.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import rifl2.datamodel.CustomerData;
import rifl2.datamodel.DeliveryData;
import rifl2.datamodel.Order;
import rifl2.interfaces.IDeliveryCalculator;
import rifl2.interfaces.IDistanceCalculator;

public class DistanceCalculator implements IDistanceCalculator {
	
	private boolean isrunning;
	private BlockingQueue<Order> Queue;
	private boolean exit;
	private static double northDeliveryCost = 1500;
	private static double southDeliveryCost = 2500;
	private static double eastDeliveryCost = 3500;
	private static double westDeliveryCost = 4500;
	private static double centralDeliveryCost = 500;
	
	private IDeliveryCalculator next;

	@Override
	public void setRunning(boolean running) {
		isrunning =running;

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
							System.out.println("*************Calculating distance data ******************");
							System.out.println("Delivery cost before: "+order.getDeliveryData().getDeliveryCost());
							calculateDistance(order.getDeliveryData(),
									order.getCustomerData());
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
	
	public void setDelivery(IDeliveryCalculator calc)
	{
		next = calc;
	}

}
