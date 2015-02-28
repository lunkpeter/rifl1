package rifl2.datamodel;


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

	public void setDeliveryCost(double deliveryCost) {
		this.deliveryCost = deliveryCost;
	}

	public DeliveryMethod getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(deliveryCost);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((deliveryMethod == null) ? 0 : deliveryMethod.hashCode());
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
		DeliveryData other = (DeliveryData) obj;
		if (Double.doubleToLongBits(deliveryCost) != Double
				.doubleToLongBits(other.deliveryCost))
			return false;
		if (deliveryMethod != other.deliveryMethod)
			return false;
		return true;
	}
	
	
}
