package rifl2.interfaces;


public interface IOrderPriceCalculator extends ICalculator{
	
	public void setDiscount(IDiscountCalculator calc);

	public void setDistance(IDistanceCalculator calc);

	
}
