package ui;

import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import datamodel.CustomerData;
import datamodel.DeliveryData;
import datamodel.DeliveryMethod;
import datamodel.Item;
import datamodel.Order;
import datamodel.PriceData;
import datamodel.Region;

public class Test {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Test window = new Test();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Test() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @param panel 
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 913);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		
		JPanel scrollablePanel = new JPanel();
		frame.getContentPane().add(scrollablePanel);
		scrollablePanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("450px"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		CreateOrderPanel createOrderPanel = new CreateOrderPanel();
		FullPriceCalculatorPanel fullPriceCalculatorPanel = new FullPriceCalculatorPanel();
		DeliveryCalculatorPanel deliveryCalculatorPanel = new DeliveryCalculatorPanel(fullPriceCalculatorPanel);
		NetPriceCalculatorPanel netPriceCalculatorPanel = new NetPriceCalculatorPanel(fullPriceCalculatorPanel);
		DistancePriceCalculatorPanel distancePriceCalculatorPanel = new DistancePriceCalculatorPanel(deliveryCalculatorPanel);
		DiscountCalculatorPanel discountCalculatorPanel = new DiscountCalculatorPanel(netPriceCalculatorPanel);
		OrderPriceCalculatorPanel orderPriceCalculatorPanel = new OrderPriceCalculatorPanel(discountCalculatorPanel, distancePriceCalculatorPanel);
		
		scrollablePanel.add(createOrderPanel, "1, 1, left, top");
		scrollablePanel.add(orderPriceCalculatorPanel, "1, 2, left, center");
		scrollablePanel.add(discountCalculatorPanel, "1, 3, left, center");
		scrollablePanel.add(distancePriceCalculatorPanel, "1, 4, left, center");
		scrollablePanel.add(netPriceCalculatorPanel, "1, 5, left, center");
		scrollablePanel.add(deliveryCalculatorPanel, "1, 6, left, center");
		scrollablePanel.add(fullPriceCalculatorPanel, "1, 7, left, center");
		
		JScrollPane scrollPane = new JScrollPane(scrollablePanel);
		frame.getContentPane().add(scrollPane);
		
		CustomerData customerData = new CustomerData("TEST", Region.Central);
		DeliveryData deliveryData = new DeliveryData(
				DeliveryMethod.PrivateDelivery);
		PriceData priceData = new PriceData();
		Order order = new Order(customerData, deliveryData, priceData);
		for (int i = 0; i < 10; i++) {
			order.addItem(new Item(25000, "TEST" + i));
		}
		try {
			orderPriceCalculatorPanel.worker.Queue.put(order);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
