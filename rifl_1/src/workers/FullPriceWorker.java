package workers;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import ui.BasePanel;
import datamodel.DeliveryData;
import datamodel.Order;
import datamodel.PriceData;

public class FullPriceWorker extends BaseWorker {

	public BlockingQueue<Order> PriceQueue;


	public FullPriceWorker(BasePanel panel, Order input, Order priceorder) {
		super(panel);
		PriceQueue = new ArrayBlockingQueue<Order>(1, true);
	}

	@Override
	protected Order doInBackground(){
		while (!exit) {
			Order delivorder;
			Order priceorder;
			try {
				delivorder = Queue.take();
				priceorder = PriceQueue.take();
				System.out.println("calculating full price");
				calculateFullPrice(delivorder.getDeliveryData(), priceorder.getPriceData());
				publish(priceorder);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

		
	
	private void calculateFullPrice(DeliveryData deliveryData, PriceData priceData) throws InterruptedException{
		Thread.sleep(500);
		double tempPrice = priceData.getPrice();
		double tempNetPrice = priceData.getNetPrice();
		
		priceData.setPrice(tempPrice+deliveryData.getDeliveryCost());
		priceData.setNetPrice(tempNetPrice+deliveryData.getDeliveryCost());
	}

	@Override
	protected void process(List<Order> chunks) {
		for (Order order : chunks) {
			 panel.setAfterData(order);
		}
	}

}

