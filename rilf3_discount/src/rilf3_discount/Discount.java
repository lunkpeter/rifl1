package rilf3_discount;

import java.io.IOException;

public class Discount {
	public static void main(String[] args) {
		DiscountCalculator calc = new DiscountCalculator();
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
