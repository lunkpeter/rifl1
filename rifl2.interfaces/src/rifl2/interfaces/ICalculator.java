package rifl2.interfaces;

import rifl2.datamodel.Order;


/**
 * Base interface for OSGI services.
 *
 */
public interface ICalculator {
	/**
	 * This method indicates that the calculator process should run.
	 * @param running
	 */
	public void setRunning(boolean running);
	/**
	 * Enqueue a new Order in the process working list.
	 * @param order The new order.
	 */
	public void enQueue(Order order);
	/**
	 * Initialize the calculator.
	 */
	public void init();
}
