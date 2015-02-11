package workers;

import java.util.List;

import ui.BasePanel;
import datamodel.Item;
import datamodel.Order;

public class OrderPriceWorker extends BaseWorker {

	public OrderPriceWorker(BasePanel panel) {
		super(panel);
	}

	@Override
	protected Order doInBackground() {
		while (!exit) {
			if (isrunning) {
				Order order;
				try {
					order = Queue.take();
					System.out.println("calculating order price");
					calculateOrderPrice(order);
					for (BasePanel panel : panel.NextPanels) {
						BaseWorker worker = panel.worker;
						worker.Queue.put(order);
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

	private void calculateOrderPrice(Order order) throws InterruptedException {
		double tempPrice = 0;
		Thread.sleep(500);
		for (Item item : order.getItems()) {
			tempPrice += item.getPrice();
		}

		order.getPriceData().setPrice(tempPrice);
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