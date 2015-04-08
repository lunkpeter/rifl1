package rifl5_base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import akka.actor.UntypedActor;
import datamodel.Order;

public abstract class BaseCalculator extends UntypedActor {

	protected abstract void calculate(Order order) throws InterruptedException;
	protected OrderGUI gui;

	@Override
	public void onReceive(Object msg) {
		if (msg instanceof byte[]) {
			OrderMessage orderMessage;
			try {
				orderMessage = deserializeOrder((byte[]) msg);
				Order order = orderMessage.getOrder();
				if(gui==null) {
					System.out.println(this.getClass().getName()+" BEFORE CALC" + orderMessage);
				} else { 
					gui.setOrder(order);
					while(!gui.canCalculate)
						Thread.sleep(100);
					gui.canCalculate = false;
				}
				calculate(order);
				if(gui==null) {
					System.out.println(this.getClass().getName()+" AFTER CALC" + orderMessage);
				} else {
					gui.setAfter(order);
					while(!gui.canSend)
						Thread.sleep(100);
					gui.canSend = false;
				}
				send(orderMessage);
			} catch (ClassNotFoundException e) {
				unhandled(msg);
				e.printStackTrace();
			} catch (IOException e) {
				unhandled(msg);
				e.printStackTrace();
			} catch (InterruptedException e) {
				unhandled(msg);
				e.printStackTrace();
			}
		} else {
			unhandled(msg);
		}

		
		//getSender().tell(Msg.DONE, getSelf());
	
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
	
	protected abstract void send(OrderMessage msg);

}
