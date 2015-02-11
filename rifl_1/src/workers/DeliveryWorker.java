package workers;

import java.util.List;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import ui.BasePanel;
import datamodel.DeliveryData;
import datamodel.Order;
import datamodel.PriceData;

public class DeliveryWorker extends SwingWorker<Order, String> {

	private Order order;
	private BasePanel panel;
	private static double priceThreshold = 50000;

	public DeliveryWorker(BasePanel panel, Order input) {
		super();
		this.order = input;
		this.panel = panel;
	}

	@Override
	protected Order doInBackground() throws Exception {
		calculateDelivery(order.getDeliveryData(), order.getPriceData());

		return order;
	}

		
	@Override
    public void done() {
        panel.setAfterData(order);
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

}
