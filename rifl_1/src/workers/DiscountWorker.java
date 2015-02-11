package workers;

import javax.swing.SwingWorker;

import ui.BasePanel;
import datamodel.Order;
import datamodel.PriceData;

public class DiscountWorker extends SwingWorker<Order, String>{
	private Order order;
	private BasePanel panel;
	private static double priceThreshold1 = 100000;
	private static double priceThreshold2 = 200000;

	public DiscountWorker(BasePanel panel, Order input) {
		super();
		this.order = input;
		this.panel = panel;
	}

	@Override
	protected Order doInBackground() throws Exception {
		caclulateDiscount(order.getPriceData());

		return order;
	}

		
	@Override
    public void done() {
        panel.setAfterData(order);
    }
	
	private void caclulateDiscount(PriceData data) throws InterruptedException{
		double tempPrice = data.getPrice();
		Thread.sleep(500);
		if(data.getPrice() >= priceThreshold1){
			if(data.getPrice() >= priceThreshold2){
				data.setPrice(tempPrice*0.8);
			}else {
				data.setPrice(tempPrice*0.9);
			}
		}
		
	}
}
