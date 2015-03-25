package rifl4_fullprice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.JMSException;
import javax.naming.NamingException;

public class FullPrice {
	public static void main(String[] args) {
		boolean exit = false;
		
		FullPriceCalculator calc = null;
		try {
			calc = new FullPriceCalculator();
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
			
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if(calc!=null)
				calc.exit = true;
		}
	}
}
