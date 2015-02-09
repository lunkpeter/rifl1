package datamodel;

public class CustomerData {
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


	public void setName(String name) {
		this.name = name;
	}


	public Region getRegion() {
		return region;
	}


	public void setRegion(Region region) {
		this.region = region;
	}
	
	
}
