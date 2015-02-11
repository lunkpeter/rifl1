package workers;

import javax.swing.SwingWorker;

import ui.BasePanel;
import datamodel.CustomerData;
import datamodel.DeliveryData;
import datamodel.Order;

public class DistanceWorker extends SwingWorker<Order, String> {

	private Order order;
	private BasePanel panel;
	private static double northDeliveryCost = 1500;
	private static double southDeliveryCost = 2500;
	private static double eastDeliveryCost = 3500;
	private static double westDeliveryCost = 4500;
	private static double centralDeliveryCost = 500;

	public DistanceWorker(BasePanel panel, Order input) {
		super();
		this.order = input;
		this.panel = panel;
	}

	@Override
	protected Order doInBackground() throws Exception {
		calculateDistance(order.getDeliveryData(), order.getCustomerData());

		return order;
	}

		
	@Override
    public void done() {
        panel.setAfterData(order);
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

}
