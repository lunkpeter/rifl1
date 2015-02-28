package rifl2.interfaces;


/**
 * Interface for calculating delivery price.
 *
 */
public interface IDeliveryCalculator extends ICalculator {
	
	/**
	 * Set the next process' calculator.
	 * @param calc
	 */
	public void setFullPrice(IFullPriceCalculator calc);

}
