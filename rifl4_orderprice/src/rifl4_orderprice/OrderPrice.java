package rifl4_orderprice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.JMSException;

public class OrderPrice {
	public static void main(String[] args) {
		boolean exit = false;
		
		OrderPriceCalculator calc = null;
		try {
		calc = new OrderPriceCalculator();
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
