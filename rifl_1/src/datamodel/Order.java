package datamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Order extends Element {
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

	public void setCustomerData(CustomerData customerData) {
		this.customerData = customerData;
	}

	public DeliveryData getDeliveryData() {
		return deliveryData;
	}

	public void setDeliveryData(DeliveryData deliveryData) {
		this.deliveryData = deliveryData;
	}

	public PriceData getPriceData() {
		return priceData;
	}

	public void setPriceData(PriceData priceData) {
		this.priceData = priceData;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	public void addItem(Item item){
		if(!items.contains(item)){
			items.add(item);
		}
	}
	
	public void removeItem(Item item){
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
	
}
