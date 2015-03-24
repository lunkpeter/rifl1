package rifl4_fullprice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;

import rifl4_base.BaseCalculator;
import datamodel.DeliveryData;
import datamodel.Order;
import datamodel.PriceData;

public class FullPriceCalculator extends BaseCalculator {
	public boolean exit;
	public boolean isrunning = false;
	private MessageConsumer consumer2;
	
	private static final String IN_QUEUE_PRICE_NAME = "net";
	private static final String IN_QUEUE_DELIV_NAME = "delivery";
	
	private List<Order> delivOrders = new ArrayList<Order>();
	private List<Order> priceOrders = new ArrayList<Order>();

	public FullPriceCalculator(String brokerUrl) throws JMSException {
		setConnection(brokerUrl, IN_QUEUE_PRICE_NAME, null);

		if(IN_QUEUE_DELIV_NAME!=null) {
            Destination destination2 = session.createQueue(IN_QUEUE_DELIV_NAME);
            consumer2 = session.createConsumer(destination2);
		}
	}

	@Override
	protected void calculate(Order order) throws InterruptedException {
		DeliveryData deliveryData = order.getDeliveryData();
		PriceData priceData = order.getPriceData();
		double tempPrice = priceData.getPrice();
		double tempNetPrice = priceData.getNetPrice();

		priceData.setPrice(tempPrice + deliveryData.getDeliveryCost());
		priceData.setNetPrice(tempNetPrice + deliveryData.getDeliveryCost());
	}

	@Override
	public void run() {
		while (!exit) {
			if (isrunning) {
				Order priceOrder = null;
				Order delivOrder = null;
				try {
					Message inputMessage = consumer.receive(100);
					
					if(inputMessage instanceof BytesMessage) {
						BytesMessage objectMessage = (BytesMessage) inputMessage;
						byte[] data = new byte[(int) objectMessage.getBodyLength()];
						objectMessage.readBytes(data);
						priceOrders.add(deserializeOrder(data));
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					Message inputMessage = consumer2.receive(100);
					
					if(inputMessage instanceof BytesMessage) {
						BytesMessage objectMessage = (BytesMessage) inputMessage;
						byte[] data = new byte[(int) objectMessage.getBodyLength()];
						objectMessage.readBytes(data);
						delivOrders.add(deserializeOrder(data));
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				try {
					for (Order dO : delivOrders) {
						for (Order pO : priceOrders) {
							if(dO.getId()==pO.getId()) {
								delivOrder=dO;
								priceOrder=pO;
							}
						}
					}
					if(delivOrder!=null && priceOrder!=null) {
						delivOrders.remove(delivOrder);
						priceOrders.remove(priceOrder);
						
						priceOrder.setDeliveryData(delivOrder.getDeliveryData());
						
						System.out.println("BEFORE CALC"+priceOrder);
						calculate(priceOrder);
						System.out.println("AFTER CALC"+priceOrder);
						
						isrunning = false;
					}
				} catch (InterruptedException intex) {
					
				}
			} else {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
		closeConnection();
	}
}
