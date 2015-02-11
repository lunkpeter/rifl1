package workers;

import javax.swing.SwingWorker;

import ui.BasePanel;
import datamodel.Item;
import datamodel.Order;

public class OrderPriceWorker extends SwingWorker<Order, String> {

	private Order order;
	private BasePanel panel;


	public OrderPriceWorker(BasePanel panel, Order input) {
		super();
		this.order = input;
		this.panel = panel;
	}

	@Override
	protected Order doInBackground() throws Exception {
		calculateOrderPrice(order);

		return order;
	}

		
	@Override
    public void done() {
        panel.setAfterData(order);
    }
	
	private void calculateOrderPrice(Order order) throws InterruptedException{
		double tempPrice = 0;
		Thread.sleep(500);
		for (Item item : order.getItems()) {
			tempPrice += item.getPrice();
		}
		
		order.getPriceData().setPrice(tempPrice);
	}

}