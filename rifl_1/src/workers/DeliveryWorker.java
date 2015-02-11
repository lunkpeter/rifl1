package workers;

import java.util.List;

import ui.BasePanel;
import datamodel.DeliveryData;
import datamodel.Order;
import datamodel.PriceData;

public class DeliveryWorker extends BaseWorker {

	private static double priceThreshold = 50000;

	public DeliveryWorker(BasePanel panel) {
		super(panel);

	}

	@Override
	protected Order doInBackground() {
		while (!exit) {
			if (isrunning) {
				Order order;
				try {
					order = Queue.take();
					System.out.println("calculating delivery");
					calculateDelivery(order.getDeliveryData(),
							order.getPriceData());
					for (BasePanel panel : panel.NextPanels) {
						BaseWorker worker = panel.worker;
						if (worker instanceof FullPriceWorker) {
							((FullPriceWorker) worker).Queue.put(order);
						} else {
							worker.Queue.put(order);
						}
					}

					publish(order);
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
		return null;
	}

	@Override
	protected void process(List<Order> chunks) {
		for (Order order : chunks) {
			panel.setAfterData(order);
			for (BasePanel panel : panel.NextPanels) {
				panel.setBeforeData(order);
			}
		}
		isrunning = false;

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
