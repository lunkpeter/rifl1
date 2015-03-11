package rifl3_netprice;

import java.io.IOException;

public class Netprice {
	public static void main(String[] args) {
		NetPriceCalculator calc = new NetPriceCalculator("localhost");
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
