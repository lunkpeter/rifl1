package workers;

import java.util.List;

import ui.BasePanel;
import datamodel.CustomerData;
import datamodel.DeliveryData;
import datamodel.Order;

public class DistanceWorker extends BaseWorker {

	private static double northDeliveryCost = 1500;
	private static double southDeliveryCost = 2500;
	private static double eastDeliveryCost = 3500;
	private static double westDeliveryCost = 4500;
	private static double centralDeliveryCost = 500;

	public DistanceWorker(BasePanel panel) {
		super(panel);
	}

	@Override
	protected Order doInBackground() {
		while (!exit) {
			if (isrunning) {
				Order order;
				try {
					order = Queue.take();
					System.out.println("calculating distance");
					calculateDistance(order.getDeliveryData(),
							order.getCustomerData());
					for (BasePanel panel : panel.NextPanels) {
						BaseWorker worker = panel.worker;
						worker.Queue.put(order);
					}
					publish(order);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
		return null;
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

	@Override
	protected void process(List<Order> chunks) {
		for (Order order : chunks) {
			panel.setAfterData(order);
			for (BasePanel panel : panel.NextPanels) {
				panel.setBeforeData(order);
			}
		}
	}

}
