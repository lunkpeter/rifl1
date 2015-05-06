package rifl6.base;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.opencsv.CSVWriter;

import datamodel.Order;

public class Logger {
	private static double MEAN = 200.0f; 
    private static double VARIANCE = 25.0f;
    private static int LOWERBOUND = 1;
    private static int UPPERBOUND = 100;
	
	private static String logFilePath = "D:\\rifl_log_egyenletes_5000.csv";
	static BlockingQueue<String[]> queue = new ArrayBlockingQueue<String[]>(10000);
	
	
	
	public static double getNextGauss(){
		Random rand = new Random();
		return MEAN + rand.nextGaussian() * VARIANCE;
	}
	
	public static int getNext(){
		Random rand = new Random();
		return LOWERBOUND + rand.nextInt(UPPERBOUND-LOWERBOUND);
	}
	
	public static int getNext(int upper){
		Random rand = new Random();
		return rand.nextInt(upper);
	}
		
	
	public static void logOrderTiming(Order order){
		try {
			queue.put(order.getCalculationData().toArray(new String[0]));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	}
	public static void flush(){
		CSVWriter writer;
		try {
			writer = new CSVWriter(new FileWriter(logFilePath, true), ';');
			for (String[] i : queue) {
				writer.writeNext(i);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	     
	}
}
