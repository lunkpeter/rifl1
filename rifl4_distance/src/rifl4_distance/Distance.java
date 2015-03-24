package rifl4_distance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.JMSException;

public class Distance {
	
	public static void main(String[] args) {
		boolean exit = false;
		String brokerUrl="tcp://localhost:61616";
		if(args.length>0) {
			brokerUrl=args[1];
		}
		
		DistanceCalculator calc = null;
		try {
			calc = new DistanceCalculator(brokerUrl);
			Thread mythread = new Thread(calc);
			mythread.start();
			
			System.out.println("Connection established at: "+brokerUrl);
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
		}catch (JMSException ex) {
			
		} finally {
			calc.exit = true;
		}
	}

}
