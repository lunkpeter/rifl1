package datamodel;

import java.io.Serializable;

public class CustomerData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3891768933949504728L;
	private String name;
	private Region region;
	
	
	public CustomerData(String name, Region region) {
		super();
		this.name = name;
		this.region = region;
	}


	public String getName() {
		return name;
	}


	public synchronized void setName(String name) {
		this.name = name;
	}


	public Region getRegion() {
		return region;
	}


	public synchronized void setRegion(Region region) {
		this.region = region;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "customer: "+name+"\ncustomer region: "+region;
	}
}
