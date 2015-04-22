package rifl6.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.sample.DroolsManager;
import com.sample.DroolsManager.Event;
import com.sample.DroolsManager.Event.EventDeliveryMethod;
import com.sample.DroolsManager.Event.ProcessType;
import com.sample.DroolsManager.Event.Type;

import datamodel.Order;

public abstract class BaseCalculator implements Runnable {
	
	public final int id = UUID.randomUUID().hashCode();

	protected abstract void calculate(Order order) throws InterruptedException;
	protected OrderGUI gui;
	protected List<OrderMessage> orders = new ArrayList<OrderMessage>();
	public boolean isrunning;
	public static final boolean AUTOMATIC = true;
	protected static boolean FULL_CONSOLE_LOG = false;
	protected ProcessType type;
	
	public void run() {
		isrunning = true;
		while(isrunning) {
			if(orders.size()>0) {
				OrderMessage orderMessage = orders.get(0);
				Order order = orderMessage.getOrder();
				int startTime = (int) System.currentTimeMillis();
				{
					Event e = new Event();
					e.setCalculatorID(id);
					e.setOrderID(order.getId());
					e.setType(Type.Start);
					e.setProcessType(type);
					e.setTimestamp(startTime);
					switch (order.getDeliveryData().getDeliveryMethod()) {
					case PostalDelivery:
						e.setDeliveryMethod(EventDeliveryMethod.PostalDelivery);
						break;

					case PrivateDelivery:
						e.setDeliveryMethod(EventDeliveryMethod.PrivateDelivery);
						break;

					case TakeAway:
						e.setDeliveryMethod(EventDeliveryMethod.TakeAway);
						break;
					default:
						break;
					}
					
					DroolsManager.getInstance().addEvent(e);
				}
				try {
					if(AUTOMATIC || gui==null) {
						calculate(order);
						//System.out.println(this.getClass().getName()+" AFTER CALC" + orderMessage);
//						Thread.sleep((new Random()).nextInt(100)*100);
					} else {
						gui.setOrder(order);
						while(!gui.canCalculate)
							Thread.sleep(100);
						gui.canCalculate = false;
						calculate(order);
						
						gui.setAfter(order);
						while(!gui.canSend)
							Thread.sleep(100);
						gui.canSend = false;
					}
					send(orderMessage);

					int stopTime = (int) System.currentTimeMillis();
					{
						Event e = new Event();
						e.setCalculatorID(id);
						e.setOrderID(order.getId());
						e.setType(Type.End);
						e.setProcessType(type);
						e.setTimestamp(stopTime);
						e.setProcessTime(stopTime-startTime);
						switch (order.getDeliveryData().getDeliveryMethod()) {
						case PostalDelivery:
							e.setDeliveryMethod(EventDeliveryMethod.PostalDelivery);
							break;

						case PrivateDelivery:
							e.setDeliveryMethod(EventDeliveryMethod.PrivateDelivery);
							break;

						case TakeAway:
							e.setDeliveryMethod(EventDeliveryMethod.TakeAway);
							break;
						default:
							break;
						}
						
						DroolsManager.getInstance().addEvent(e);
					}
					
					orders.remove(orderMessage);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
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
