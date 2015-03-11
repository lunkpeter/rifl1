package rilf3_orderprice;

import java.io.IOException;

public class OrderPrice {
	public static void main(String[] args) {
		String brokerIP="localhost";
		if(args.length>0) {
			brokerIP=args[1];
		}
		
		OrderPriceCalculator calc = new OrderPriceCalculator(brokerIP);
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
