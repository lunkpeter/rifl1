package rifl2.interfaces;


/**
 * Interface for calculating delivery distance.
 *
 */
public interface IDistanceCalculator extends ICalculator{
	/**
	 * Set the next process' calculator.
	 * @param calc
	 */
	public void setDelivery(IDeliveryCalculator calc);
}
