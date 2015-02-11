package datamodel;

public class Item extends Element{
	private double price;
	private String name;
	
	public Item(double price, String name) {
		super();
		this.price = price;
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public synchronized void setPrice(double price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public synchronized void setName(String name) {
		this.name = name;
	}
	
	
}
