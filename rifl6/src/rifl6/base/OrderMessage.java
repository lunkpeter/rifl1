package rifl6.base;

import java.io.Serializable;

import datamodel.Order;

public class OrderMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2600912620706717223L;

	public enum Sender{
		Order,
		Distance,
		Discount,
		Delivery,
		Net,
		Full
	}
	
	private Order order;
	private Sender sender;
	
	public OrderMessage(Order order, Sender sender) {
		super();
		this.order = order;
		this.sender = sender;
	}

	public Order getOrder() {
		return order;
	}

	public Sender getSender() {
		return sender;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}
	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return order.toString();
	}
	
	

}
