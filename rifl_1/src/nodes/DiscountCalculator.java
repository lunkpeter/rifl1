package nodes;

import datamodel.PriceData;

public class DiscountCalculator {
	private static double priceThreshold1 = 100000;
	private static double priceThreshold2 = 200000;
	
	public void caclulateDiscount(PriceData data){
		double tempPrice = data.getPrice();
		if(data.getPrice() >= priceThreshold1){
			if(data.getPrice() >= priceThreshold2){
				data.setPrice(tempPrice*0.8);
			}else {
				data.setPrice(tempPrice*0.9);
			}
		}
		
	}
}
