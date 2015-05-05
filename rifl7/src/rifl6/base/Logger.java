package rifl6.base;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import com.opencsv.CSVWriter;

import datamodel.Order;

public class Logger {
	private static double MEAN = 200.0f; 
    private static double VARIANCE = 25.0f;
    private static int LOWERBOUND = 1;
    private static int UPPERBOUND = 300;
	
	private static String logFilePath = "D:\\rifl_log.csv";
	
	
	
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
		CSVWriter writer;
		try {
			writer = new CSVWriter(new FileWriter(logFilePath, true), ';');
			String[] entries = order.getCalculationData().toArray(new String[order.getCalculationData().size()]);
		    writer.writeNext(entries);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	     
	}
}
