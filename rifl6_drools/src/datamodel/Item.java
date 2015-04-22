package datamodel;

public class Item extends Element{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4771494573828304587L;
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
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name+" - "+String.valueOf(price);
	}
}
