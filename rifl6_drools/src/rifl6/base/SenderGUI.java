package rifl6.base;

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

import javax.swing.UIManager;

import rifl6.RIFLCore;
import rifl6.RIFLCore.Command;

import java.awt.Color;

public class SenderGUI {

	public static final int HEIGHT = 450;
	public static final int WIDTH = 350;
	private JFrame frmDefault;
	private JTextArea txtGenerated;
	private JButton btnGenerate;
	private JButton btnSend;

	/**
	 * Create the application.
	 */
	public SenderGUI(String title) {
		initialize(title);
		frmDefault.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String title) {
		frmDefault = new JFrame();
		frmDefault.setTitle(title);
		frmDefault.setBounds(0, 0, WIDTH, HEIGHT);
		frmDefault.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDefault.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Generated Order", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		frmDefault.getContentPane().add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));
		panel.setMinimumSize(new Dimension(250, 100));
		
		txtGenerated = new JTextArea();
		txtGenerated.setEditable(false);
		txtGenerated.setColumns(20);
		panel.add(txtGenerated, BorderLayout.CENTER);
		
		JPanel panel_2 = new JPanel();
		frmDefault.getContentPane().add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnGenerate = new JButton("Generate");
		btnGenerate.setMinimumSize(new Dimension(175, 0));
		btnGenerate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(btnGenerate.isEnabled()) {
					btnGenerate.setEnabled(false);
					RIFLCore.command = Command.generate;
				}
			}
		});
		panel_2.add(btnGenerate);
		
		btnSend = new JButton("Send");
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(btnSend.isEnabled()) {
					btnSend.setEnabled(false);
					RIFLCore.command = Command.send;
					txtGenerated.setText("");
				}
			}
		});
		btnSend.setEnabled(false);
		panel_2.add(btnSend);
	}
	
	public void setOrder(Order order) {
		txtGenerated.setText(order.toString());
		btnGenerate.setEnabled(true);
		btnSend.setEnabled(true);
	}

}
