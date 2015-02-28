package rifl2.interfaces;


/**
 * Interface for calculating the order price from ordered items' price.
 *
 */
public interface IOrderPriceCalculator extends ICalculator{
	
	/**
	 * Set the next process' calculator.
	 * @param calc
	 */
	public void setDiscount(IDiscountCalculator calc);

	/**
	 * Set the next process' calculator.
	 * @param calc
	 */
	public void setDistance(IDistanceCalculator calc);

	
}
