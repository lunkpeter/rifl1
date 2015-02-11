package workers;

import javax.swing.SwingWorker;

import ui.BasePanel;
import datamodel.DeliveryData;
import datamodel.Order;
import datamodel.PriceData;

public class FullPriceWorker extends SwingWorker<Order, String> {

	private Order order;
	private Order priceorder;
	private BasePanel panel;


	public FullPriceWorker(BasePanel panel, Order input, Order priceorder) {
		super();
		this.order = input;
		this.priceorder = priceorder;
		this.panel = panel;
	}

	@Override
	protected Order doInBackground() throws Exception {
		calculateFullPrice(order.getDeliveryData(), priceorder.getPriceData());

		return order;
	}

		
	@Override
    public void done() {
        panel.setAfterData(order);
    }
	
	private void calculateFullPrice(DeliveryData deliveryData, PriceData priceData) throws InterruptedException{
		Thread.sleep(500);
		double tempPrice = priceData.getPrice();
		double tempNetPrice = priceData.getNetPrice();
		
		priceData.setPrice(tempPrice+deliveryData.getDeliveryCost());
		priceData.setNetPrice(tempNetPrice+deliveryData.getDeliveryCost());
	}

}

