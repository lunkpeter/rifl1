package rifl3_fullprice;

import java.io.IOException;

public class FullPrice {
	public static void main(String[] args) {
		FullPriceCalculator calc = new FullPriceCalculator("localhost");
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
