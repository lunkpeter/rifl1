package rifl2.interfaces;

import rifl2.datamodel.Order;


public interface IFullPriceCalculator extends ICalculator {
	
	public void enQueuePrice(Order order);

}
