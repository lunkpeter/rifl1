package rifl5_base;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import datamodel.Order;

public class OrderGUI {
	private static final String LBL_CALCULATE="Calculate";
	private static final String LBL_SEND="Send";
	private static int windowOffset = 0;
	
	public boolean canCalculate;
	public boolean canSend;

	private JFrame frmDefault;
	private JTextArea txtAfter;
	private JTextArea txtBefore;
	private JButton btnCalculate;

	/**
	 * Create the application.
	 */
	public OrderGUI(String title) {
		initialize(title);
		frmDefault.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String title) {
		frmDefault = new JFrame();
		frmDefault.setTitle(title);
		frmDefault.setBounds(10+windowOffset, 100, 600, 300);
		windowOffset+=150;
		frmDefault.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDefault.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Before", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmDefault.getContentPane().add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));
		panel.setMinimumSize(new Dimension(250, 100));
		
		txtBefore = new JTextArea();
		txtBefore.setEditable(false);
		txtBefore.setColumns(20);
		panel.add(txtBefore, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "After", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmDefault.getContentPane().add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		txtAfter = new JTextArea();
		txtAfter.setColumns(20);
		txtAfter.setEditable(false);
		panel_1.add(txtAfter, BorderLayout.CENTER);
		
		JPanel panel_2 = new JPanel();
		frmDefault.getContentPane().add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnCalculate = new JButton("Calculate");
		btnCalculate.setEnabled(false);
		btnCalculate.setMinimumSize(new Dimension(175, 0));
		btnCalculate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnCalculate.setEnabled(false);
				if(LBL_CALCULATE.equals(btnCalculate.getText())) {
					canCalculate = true;
					btnCalculate.setText(LBL_SEND);
				} else if(LBL_SEND.equals(btnCalculate.getText())) {
					canSend = true;
					btnCalculate.setText(LBL_CALCULATE);
					txtAfter.setText("");
					txtBefore.setText("");
				}
			}
		});
		panel_2.add(btnCalculate);
	}
	
	public void setOrder(Order order) {
		txtBefore.setText(order.toString());
		btnCalculate.setEnabled(true);
	}
	
	public void setAfter(Order order) {
		txtAfter.setText(order.toString());
		btnCalculate.setEnabled(true);
	}
	
	public void appendAfter(Order order) {
		txtAfter.append(order.toString());
		btnCalculate.setEnabled(true);
	}

}