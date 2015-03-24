package datamodel;

import java.io.Serializable;

public class PriceData  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6593041044301599420L;
	private double price;
	private double netPrice;
	
	
	public PriceData() {
		super();
		this.price = 0;
		this.netPrice = 0;
	}


	public double getPrice() {
		return price;
	}


	public synchronized void setPrice(double price) {
		this.price = price;
	}


	public double getNetPrice() {
		return netPrice;
	}


	public synchronized void setNetPrice(double netPrice) {
		this.netPrice = netPrice;
	}
	
	@Override
	public String toString() {
		return "price: "+price+"\nnet price: "+netPrice;
	}
}
