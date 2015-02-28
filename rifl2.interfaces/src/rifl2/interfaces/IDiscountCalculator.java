package rifl2.interfaces;


public interface IDiscountCalculator extends ICalculator {
	public void setNetPrice(INetPriceCalculator calc);

}
