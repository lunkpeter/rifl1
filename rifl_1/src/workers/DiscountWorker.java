package workers;

import java.util.List;

import ui.BasePanel;
import datamodel.Order;
import datamodel.PriceData;

public class DiscountWorker extends BaseWorker {
	private static double priceThreshold1 = 100000;
	private static double priceThreshold2 = 200000;

	public DiscountWorker(BasePanel panel) {
		super(panel);

	}

	@Override
	protected Order doInBackground() {
		while (!exit) {
			if (isrunning) {
				Order order;
				try {
					order = Queue.take();
					System.out.println("calculating discount");
					caclulateDiscount(order.getPriceData());
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
