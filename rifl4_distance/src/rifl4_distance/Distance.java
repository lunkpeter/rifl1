package rifl4_distance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.JMSException;

public class Distance {
	
	public static void main(String[] args) {
		boolean exit = false;
		
		DistanceCalculator calc = null;
		try {
			calc = new DistanceCalculator();
			Thread mythread = new Thread(calc);
			mythread.start();
			
			System.out.println("Connection established!");
			System.out.println("Type \"step\" to step the workflow");
			System.out.println("Type \"quit\" to exit");
			while(!exit)
			{
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				try {
					String input = reader.readLine();
					if(input.equals("step")){
						calc.isrunning = true;
					}else if (input.equals("quit")) {
						exit = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}catch (JMSException ex) {
			
		} finally {
			if(calc!=null)
				calc.exit = true;
		}
	}

}
