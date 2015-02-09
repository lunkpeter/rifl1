package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import datamodel.Item;
import datamodel.Order;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class OrderPriceCalculationPanel extends JPanel {

	private JPanel buttonPanel;
	private JPanel editDataPanel;
	private JPanel beforeAfterPanel;
	private JPanel beforeDataPanel;
	private JPanel afterDataPanel;
	private JButton calculateButton;
	private JList<Item> beforeItemsList;
	private JPanel afterPricePanel;
	private JLabel afterPriceLabel;
	private JTextField afterPriceField;
	private JButton nextButton;
	private JPanel beforePricePanel;
	private JLabel beforePriceLabel;
	private JTextField beforePriceField;

	/**
	 * Create the panel.
	 */
	public OrderPriceCalculationPanel() {
		setLayout(new BorderLayout(0, 0));
		
		setButtonPanel();
		setEditDataPanel();
		setBeforeAfterPanel();
	}

	public void setBeforeData(Order o) {
		beforePriceField.setText(String.valueOf(o.getPriceData().getPrice()));
		
		beforeItemsList.setCellRenderer(new DefaultListCellRenderer() {

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				
				if(value instanceof Item) {
					Item item = (Item) value;
					setText(item.getName()+" ("+item.getPrice()+")");
				}
				
				return this;
			}
		});
		
		DefaultListModel<Item> model = new DefaultListModel<Item>();
		for (Item item : o.getItems()) {
			model.addElement(item);
		}
		beforeItemsList.setModel(model);
	}
	
	public void setAfterData(Order o) {
		afterPriceField.setText(String.valueOf(o.getPriceData().getPrice()));
		
		nextButton.setEnabled(true);
	}

	private void setButtonPanel() {
		buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.WHITE);
		FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		add(buttonPanel, BorderLayout.SOUTH);
		
		calculateButton = new JButton("Calculate");
		calculateButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				calculateButton.setEnabled(false);
			}
		});
		buttonPanel.add(calculateButton);
		
		nextButton = new JButton("Next");
		nextButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				nextButton.setEnabled(false);
			}
		});
		nextButton.setEnabled(false);
		buttonPanel.add(nextButton);
	}
	
	private void setEditDataPanel() {
		editDataPanel = new JPanel();
		editDataPanel.setBackground(Color.WHITE);
		add(editDataPanel, BorderLayout.NORTH);
	}
	
	private void setBeforeAfterPanel() {
		beforeAfterPanel = new JPanel();
		beforeAfterPanel.setBackground(Color.WHITE);
		add(beforeAfterPanel, BorderLayout.CENTER);
		beforeAfterPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		beforeDataPanel = new JPanel();
		beforeDataPanel.setBackground(Color.WHITE);
		beforeAfterPanel.add(beforeDataPanel);
		beforeDataPanel.setBorder(new TitledBorder(null, "Before modification", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		beforeDataPanel.setLayout(new BorderLayout(0, 5));
		
		beforeItemsList = new JList<Item>();
		beforeDataPanel.add(beforeItemsList, BorderLayout.CENTER);
		
		beforePricePanel = new JPanel();
		beforePricePanel.setBackground(Color.WHITE);
		beforeDataPanel.add(beforePricePanel, BorderLayout.NORTH);
		beforePricePanel.setLayout(new BorderLayout(5, 0));
		
		beforePriceLabel = new JLabel("Price:");
		beforePricePanel.add(beforePriceLabel, BorderLayout.WEST);
		
		beforePriceField = new JTextField();
		beforePriceField.setEditable(false);
		beforePricePanel.add(beforePriceField);
		beforePriceField.setColumns(10);
		
		afterDataPanel = new JPanel();
		afterDataPanel.setBackground(Color.WHITE);
		beforeAfterPanel.add(afterDataPanel);
		afterDataPanel.setBorder(new TitledBorder(null, "After modification", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		afterDataPanel.setLayout(new BorderLayout(0, 0));
		
		afterPricePanel = new JPanel();
		afterPricePanel.setBackground(Color.WHITE);
		afterDataPanel.add(afterPricePanel, BorderLayout.NORTH);
		afterPricePanel.setLayout(new BorderLayout(5, 0));
		
		afterPriceLabel = new JLabel("Price:");
		afterPricePanel.add(afterPriceLabel, BorderLayout.WEST);
		
		afterPriceField = new JTextField();
		afterPriceField.setEditable(false);
		afterPricePanel.add(afterPriceField);
		afterPriceField.setColumns(10);
	}
}
