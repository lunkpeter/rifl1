package rilf3_orderprice;

import java.io.IOException;

public class OrderPrice {
	public static void main(String[] args) {
		OrderPriceCalculator calc = new OrderPriceCalculator("localhost");
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
