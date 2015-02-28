package rifl2.interfaces;


/**
 * Interface for calculating the discounted price's net value.
 *
 */
public interface INetPriceCalculator extends ICalculator {
	/**
	 * Set the next process' calculator.
	 * @param calc
	 */
	public void setFullPrice(IFullPriceCalculator calc);
}
