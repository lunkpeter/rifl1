package datamodel;

import java.io.Serializable;

public class Element  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected long id;

	public Element() {
		id = System.nanoTime();
	}
	
	public long getId() {
		return id;
	}
}
