package rifl3_distance;

import java.io.IOException;

public class Distance {
	
	public static void main(String[] args) {
		String brokerIP="localhost";
		if(args.length>0) {
			brokerIP=args[1];
		}
		
		DistanceCalculator calc = new DistanceCalculator(brokerIP);
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
