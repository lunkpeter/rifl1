package com.sample;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.kie.api.KieServices;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import rifl6.base.BaseCalculator;

/**
 * This is a sample class to launch a rule.
 */
public class DroolsManager implements Runnable{

    private KieServices ks;
	private KieContainer kContainer;
	private KieSession kSession;
	private Queue<Event> eventQueue;

	private static DroolsManager eINSTANCE = null;
	private KieRuntimeLogger log;
	
	public static DroolsManager getInstance() {
		if(eINSTANCE==null)
			eINSTANCE = new DroolsManager();
		return eINSTANCE;
	}
	
	private DroolsManager() {
    	try {
            ks = KieServices.Factory.get();
    	    kContainer = ks.getKieClasspathContainer();
        	kSession = kContainer.newKieSession("ksession-rules");
        	DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH");
        	Date date = new Date();
        	log = ks.getLoggers().newFileLogger(kSession, "logs/rifl_"+dateFormat.format(date));
        	eventQueue = new ArrayBlockingQueue<Event>(1000);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
	
	public void addEvent(Event e) {
		eventQueue.add(e);
	}
	
	public void dispose() {
		log.close();
		kSession.dispose();
	}

	@Override
	public void run() {
		while (BaseCalculator.isrunning) {
			if(eventQueue.size()>0) {
				while (eventQueue.size()>0) {
					Event e = eventQueue.poll();
					kSession.insert(e);
					kSession.fireAllRules();
				}
			} else {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

    public static class Event {
    	public static final int MORE_DELIVERY = 3;
    	public static final int TIMEOUT = 600;
    	public static final int TOO_LONG_AVG = 5;
    	public static final int QUEUE_LENGTH = 2;
    	
    	public int timestamp;
		private Integer calculatorID;
    	private Long orderID;
    	private Type type;
    	private ProcessType processType;
    	private EventDeliveryMethod deliveryMethod;
    	private int processTime;
    	private Date date = new Date();
    	
    	public static enum Type {Start, End };
    	
    	public static enum ProcessType { ORDER, NET, FULL, DISTANCE, DISCOUNT, DELIVERY };
    	public static enum EventDeliveryMethod {
    		TakeAway,
    		PrivateDelivery,
    		PostalDelivery;
    	}
    	
    	public Integer getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(int timestamp) {
			this.timestamp = timestamp;
		}

		public Integer getCalculatorID() {
			return calculatorID;
		}

		public void setCalculatorID(int calculatorID) {
			this.calculatorID = calculatorID;
		}

		public Long getOrderID() {
			return orderID;
		}

		public void setOrderID(long orderID) {
			this.orderID = orderID;
		}

		public Type getType() {
			return type;
		}

		public void setType(Type type) {
			this.type = type;
		}

		public ProcessType getProcessType() {
			return processType;
		}

		public void setProcessType(ProcessType processType) {
			this.processType = processType;
		}

		public EventDeliveryMethod getDeliveryMethod() {
			return deliveryMethod;
		}

		public void setDeliveryMethod(EventDeliveryMethod deliveryMethod) {
			this.deliveryMethod=deliveryMethod;
		}

		public int getProcessTime() {
			return processTime;
		}

		public void setProcessTime(int processTime) {
			this.processTime = processTime;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}
    }

}
