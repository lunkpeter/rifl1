package rifl2.datamodel;

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


	public void setPrice(double price) {
		this.price = price;
	}


	public double getNetPrice() {
		return netPrice;
	}


	public void setNetPrice(double netPrice) {
		this.netPrice = netPrice;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(netPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PriceData other = (PriceData) obj;
		if (Double.doubleToLongBits(netPrice) != Double
				.doubleToLongBits(other.netPrice))
			return false;
		if (Double.doubleToLongBits(price) != Double
				.doubleToLongBits(other.price))
			return false;
		return true;
	}
	
	
}
