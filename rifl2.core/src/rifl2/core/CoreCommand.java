package rifl2.core;

import org.apache.felix.service.command.Descriptor;

import rifl2.interfaces.IDeliveryCalculator;
import rifl2.interfaces.IDiscountCalculator;
import rifl2.interfaces.IDistanceCalculator;
import rifl2.interfaces.IFullPriceCalculator;
import rifl2.interfaces.INetPriceCalculator;
import rifl2.interfaces.IOrderPriceCalculator;

public class CoreCommand {
	
	
	private IOrderPriceCalculator ordercalc;
	private IDeliveryCalculator delivcalc;
	private IDiscountCalculator discountcalc;
	private IDistanceCalculator distancecalc;
	private IFullPriceCalculator fullpricecalc;
	private INetPriceCalculator netpricecalc;

	@Descriptor(value = "RIFL command")
	public void coreCommand(
			@Descriptor(value = "An integer parameter to be processed.") Integer value) {
		
		System.out.println(value + 3);
	}

	public IOrderPriceCalculator getOrdercalc() {
		return ordercalc;
	}

	public void setOrdercalc(IOrderPriceCalculator ordercalc) {
		this.ordercalc = ordercalc;
	}

	public IDeliveryCalculator getDelivcalc() {
		return delivcalc;
	}

	public void setDelivcalc(IDeliveryCalculator delivcalc) {
		this.delivcalc = delivcalc;
	}

	public IDiscountCalculator getDiscountcalc() {
		return discountcalc;
	}

	public void setDiscountcalc(IDiscountCalculator discountcalc) {
		this.discountcalc = discountcalc;
	}

	public IDistanceCalculator getDistancecalc() {
		return distancecalc;
	}

	public void setDistancecalc(IDistanceCalculator distancecalc) {
		this.distancecalc = distancecalc;
	}

	public IFullPriceCalculator getFullpricecalc() {
		return fullpricecalc;
	}

	public void setFullpricecalc(IFullPriceCalculator fullpricecalc) {
		this.fullpricecalc = fullpricecalc;
	}

	public INetPriceCalculator getNetpricecalc() {
		return netpricecalc;
	}

	public void setNetpricecalc(INetPriceCalculator netpricecalc) {
		this.netpricecalc = netpricecalc;
	}
	
	
	

}
