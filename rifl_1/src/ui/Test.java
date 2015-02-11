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
		scrollablePanel.add(createOrderPanel, "1, 1, left, top");
		OrderPriceCalculatorPanel orderPriceCalculatorPanel = new OrderPriceCalculatorPanel();
		scrollablePanel.add(orderPriceCalculatorPanel, "1, 2, left, center");
		DiscountCalculatorPanel discountCalculatorPanel = new DiscountCalculatorPanel();
		scrollablePanel.add(discountCalculatorPanel, "1, 3, left, center");
		DistancePriceCalculatorPanel distancePriceCalculatorPanel = new DistancePriceCalculatorPanel();
		scrollablePanel.add(distancePriceCalculatorPanel, "1, 4, left, center");
		NetPriceCalculatorPanel netPriceCalculatorPanel = new NetPriceCalculatorPanel();
		scrollablePanel.add(netPriceCalculatorPanel, "1, 5, left, center");
		DeliveryCalculatorPanel deliveryCalculatorPanel = new DeliveryCalculatorPanel();
		scrollablePanel.add(deliveryCalculatorPanel, "1, 6, left, center");
		FullPriceCalculatorPanel fullPriceCalculatorPanel = new FullPriceCalculatorPanel();
		scrollablePanel.add(fullPriceCalculatorPanel, "1, 7, left, center");
		
		JScrollPane scrollPane = new JScrollPane(scrollablePanel);
		frame.getContentPane().add(scrollPane);
	}

}
