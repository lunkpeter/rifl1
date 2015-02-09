package nodes;

import datamodel.DeliveryData;
import datamodel.PriceData;

public class FullPriceCalculator {
	public void calculateFullPrice(DeliveryData deliveryData, PriceData priceData){
		double tempPrice = priceData.getPrice();
		double tempNetPrice = priceData.getNetPrice();
		
		priceData.setPrice(tempPrice+deliveryData.getDeliveryCost());
		priceData.setNetPrice(tempNetPrice+deliveryData.getDeliveryCost());
	}
}
