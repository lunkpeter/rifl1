package datamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Order extends Element {
	/**
	 * 
	 */
	private static final long serialVersionUID = -759919317925318536L;
	private CustomerData customerData;
	private DeliveryData deliveryData;
	private PriceData priceData;
	private List<Item> items;
	
	public Order(CustomerData customerData, DeliveryData deliveryData,
			PriceData priceData) {
		super();
		this.customerData = customerData;
		this.deliveryData = deliveryData;
		this.priceData = priceData;
		this.items = new ArrayList<Item>();
	}

	public CustomerData getCustomerData() {
		return customerData;
	}

	public synchronized void setCustomerData(CustomerData customerData) {
		this.customerData = customerData;
	}

	public DeliveryData getDeliveryData() {
		return deliveryData;
	}

	public synchronized void setDeliveryData(DeliveryData deliveryData) {
		this.deliveryData = deliveryData;
	}

	public PriceData getPriceData() {
		return priceData;
	}

	public synchronized void setPriceData(PriceData priceData) {
		this.priceData = priceData;
	}

	public List<Item> getItems() {
		return items;
	}

	public synchronized void setItems(List<Item> items) {
		this.items = items;
	}
	
	public synchronized void addItem(Item item){
		if(!items.contains(item)){
			items.add(item);
		}
	}
	
	public synchronized void removeItem(Item item){
		if(items.contains(item)){
			items.remove(item);
		}
	}
	
	public Item getItem(long id){
		for (Item item : items) {
			if(item.id == id){
				return item;
			}
		}
		throw new NoSuchElementException();
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder("**************Order**************\n");
		sb.append("id: "+this.id+"\n");
		sb.append(priceData+"\n");
		sb.append(customerData+"\n");
		sb.append(deliveryData+"\n");
		sb.append("Items:\n");
		for (Item item : items) {
			sb.append("\t"+item+"\n");
		}
		sb.append("*********************************\n");
		return sb.toString();
	}
	
}
