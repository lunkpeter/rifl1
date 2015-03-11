package rifl3_delivery;

import java.io.IOException;


public class Delivery {

	public static void main(String[] args) {
		DeliveryCalculator calc = new DeliveryCalculator("localhost");
		Thread mythread = new Thread(calc);
		mythread.start();
		
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			calc.exit = true;
		}
		
	}
	
	
}
