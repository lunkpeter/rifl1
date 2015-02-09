package nodes;

import datamodel.PriceData;

public class NetPriceCalculator {
	private static double netModifier = 1.27;
	
	public void calculateNetPrice(PriceData data){
		data.setNetPrice(data.getPrice()*netModifier);
	}
}
