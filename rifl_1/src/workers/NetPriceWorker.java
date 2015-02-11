package workers;

import java.util.List;

import ui.BasePanel;
import datamodel.Order;
import datamodel.PriceData;

public class NetPriceWorker extends BaseWorker {

	private static double netModifier = 1.27;

	public NetPriceWorker(BasePanel panel) {
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
					calculateNetPrice(order.getPriceData());
					for (BasePanel panel : panel.NextPanels) {
						BaseWorker worker = panel.worker;
						if (worker instanceof FullPriceWorker) {
							((FullPriceWorker) worker).PriceQueue.put(order);
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

	private void calculateNetPrice(PriceData data) throws InterruptedException {
		Thread.sleep(500);
		data.setNetPrice(data.getPrice() * netModifier);
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