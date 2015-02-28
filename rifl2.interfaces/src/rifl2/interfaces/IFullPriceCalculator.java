package rifl2.interfaces;

import rifl2.datamodel.Order;


/**
 * Interface for calculating full price based on delivery price and discount price.
 *
 */
public interface IFullPriceCalculator extends ICalculator {
	
	/**
	 * Connect the parallel flow with enqueue the other order calculation result.
	 * @param order
	 */
	public void enQueuePrice(Order order);

}
