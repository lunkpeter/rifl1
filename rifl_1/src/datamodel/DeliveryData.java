package datamodel;

public class DeliveryData {
	private double deliveryCost;
	private DeliveryMethod deliveryMethod;
	
	public DeliveryData(DeliveryMethod deliveryMethod) {
		super();
		this.deliveryCost = 0;
		this.deliveryMethod = deliveryMethod;
	}

	public double getDeliveryCost() {
		return deliveryCost;
	}

	public synchronized void setDeliveryCost(double deliveryCost) {
		this.deliveryCost = deliveryCost;
	}

	public DeliveryMethod getDeliveryMethod() {
		return deliveryMethod;
	}

	public synchronized void setDeliveryMethod(DeliveryMethod deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}
	
	
}
