package rifl3_delivery;

import java.io.IOException;


public class Delivery {

	public static void main(String[] args) {
		String brokerIP="localhost";
		if(args.length>0) {
			brokerIP=args[1];
		}
		
		DeliveryCalculator calc = new DeliveryCalculator(brokerIP);
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
