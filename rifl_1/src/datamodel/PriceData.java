package datamodel;

public class PriceData {
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
	
	
}
