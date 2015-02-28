package rifl2.interfaces;


/**
 * Interface for calculating discounted price.
 *
 */
public interface IDiscountCalculator extends ICalculator {
	/**
	 * Set the next process' calculator.
	 * @param calc
	 */
	public void setNetPrice(INetPriceCalculator calc);

}
