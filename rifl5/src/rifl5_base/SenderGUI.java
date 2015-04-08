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

import javax.swing.UIManager;

import rifl5_core.RIFLCore;
import rifl5_core.RIFLCore.Command;

import java.awt.Color;

public class SenderGUI {
	private static int windowOffset = 0;

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
		frmDefault.setBounds(10+windowOffset, 100+windowOffset/10, 350, 450);
		windowOffset+=150;
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
				btnGenerate.setEnabled(false);
				RIFLCore.command = Command.generate;
			}
		});
		panel_2.add(btnGenerate);
		
		btnSend = new JButton("Send");
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnSend.setEnabled(false);
				RIFLCore.command = Command.send;
				txtGenerated.setText("");
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
