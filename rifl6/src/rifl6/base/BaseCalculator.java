package rifl6.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import datamodel.Order;

public abstract class BaseCalculator implements Runnable {
	
	public final int id = UUID.randomUUID().hashCode();

	protected abstract void calculate(Order order) throws InterruptedException;
	protected OrderGUI gui;
	protected List<OrderMessage> orders = new ArrayList<OrderMessage>();
	protected boolean isrunning;
	public static final boolean AUTOMATIC = true;
	
	public void run() {
		isrunning = true;
		while(isrunning) {
			if(orders.size()>0) {
				OrderMessage orderMessage = orders.get(0);
				Order order = orderMessage.getOrder();
				try {
					calculate(order);
					if(AUTOMATIC || gui==null) {
						System.out.println(this.getClass().getName()+" AFTER CALC" + orderMessage);
					} else {
						gui.setAfter(order);
						while(!gui.canSend)
							Thread.sleep(100);
						gui.canSend = false;
					}
					send(orderMessage);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	
	}

	protected byte[] serializeOrder(OrderMessage order) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(order);
		return b.toByteArray();
	}

	protected OrderMessage deserializeOrder(byte[] message) throws IOException,
			ClassNotFoundException {
		OrderMessage ret = null;
		ByteArrayInputStream b = new ByteArrayInputStream(message);
		ObjectInputStream o = new ObjectInputStream(b);
		ret = (OrderMessage) o.readObject();
		return ret;
	}
	
	public void addOrder(byte[] msg) {
		if (msg instanceof byte[]) {
			try {
				OrderMessage orderMessage = deserializeOrder((byte[]) msg);
				orders.add(orderMessage);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected abstract void send(OrderMessage msg);

}
