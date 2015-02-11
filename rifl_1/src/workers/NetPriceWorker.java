package workers;

import javax.swing.SwingWorker;

import ui.BasePanel;
import datamodel.Order;
import datamodel.PriceData;

public class NetPriceWorker extends SwingWorker<Order, String> {

	private Order order;
	private BasePanel panel;
	private static double netModifier = 1.27;


	public NetPriceWorker(BasePanel panel, Order input) {
		super();
		this.order = input;
		this.panel = panel;
	}

	@Override
	protected Order doInBackground() throws Exception {
		calculateNetPrice(order.getPriceData());

		return order;
	}

		
	@Override
    public void done() {
        panel.setAfterData(order);
    }
	
	private void calculateNetPrice(PriceData data) throws InterruptedException{
		Thread.sleep(500);
		data.setNetPrice(data.getPrice()*netModifier);
	}

}