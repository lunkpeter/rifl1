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
		//repeat in a cycle until the exit flag is set
		while (!exit) {
			if (isrunning) {
				Order order;
				try {
					//Fetch the next order from the input queue
					order = Queue.take();
					//Execute the business logic separated in a method
					calculateDelivery(order.getDeliveryData(),
							order.getPriceData());
					//After the logic has been executed insert the product into the next production units
					//This is done via traversing through the NextPanel attribute of the owning panel
					for (BasePanel panel : panel.NextPanels) {
						BaseWorker worker = panel.worker;
						//If the target worker is a FullPriceWorker instance use the alternate queue instead
						if (worker instanceof FullPriceWorker) {
							((FullPriceWorker) worker).Queue.put(order);
						} else {
							worker.Queue.put(order);
						}
					}
					// after the calculation is complete set the isrunning flag accordingly
					isrunning = false;
					//Update the UI
					publish(order);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else {
				// if the running flag is not set, suspend data processing and wait instead
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
			//Update the owing ui's set "After Data" field via this method
			panel.setAfterData(order);
			for (BasePanel panel : panel.NextPanels) {
				//Update the nex panels "Before data" field
				panel.setBeforeData(order);
			}
		}
		

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
