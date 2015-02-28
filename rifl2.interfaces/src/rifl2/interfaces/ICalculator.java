package rifl2.interfaces;

import rifl2.datamodel.Order;


public interface ICalculator {
	public void setRunning(boolean running);
	public void enQueue(Order order);
	public void init();
}
