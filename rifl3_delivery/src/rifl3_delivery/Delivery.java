package rifl3_delivery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Delivery {

	public static void main(String[] args) {
		boolean exit = false;
		String brokerIP="localhost";
		if(args.length>0) {
			brokerIP=args[1];
		}
		
		DeliveryCalculator calc = new DeliveryCalculator(brokerIP);
		Thread mythread = new Thread(calc);
		mythread.start();
		
		System.out.println("AMQP connection established at: "+brokerIP);
		System.out.println("Type \"step\" to step the workflow");
		System.out.println("Type \"quit\" to exit");
		while(!exit)
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try {
				String input = reader.readLine();
				switch (input) {
				case "step":
					calc.isrunning = true;
					break;
				case "quit":
					exit = true;
					break;

				default:
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		calc.exit = true;
		
		
	}
	
	
}
