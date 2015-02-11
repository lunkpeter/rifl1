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
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

@SuppressWarnings("serial")
public class OrderPriceCalculatorPanel extends BasePanel {

	private JPanel buttonPanel;
	private JPanel beforeAfterPanel;
	private JPanel beforeDataPanel;
	private JPanel afterDataPanel;
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
	public OrderPriceCalculatorPanel() {
		setBackground(Color.WHITE);
		setBorder(new TitledBorder(null, "Order price calculation", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new BorderLayout(0, 0));
		
		setButtonPanel();
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
		beforePricePanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("27px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("175px"),},
			new RowSpec[] {
				RowSpec.decode("20px"),}));
		
		beforePriceLabel = new JLabel("Price:");
		beforePricePanel.add(beforePriceLabel, "1, 1, left, fill");
		
		beforePriceField = new JTextField();
		beforePriceField.setEditable(false);
		beforePricePanel.add(beforePriceField, "3, 1, fill, top");
		beforePriceField.setColumns(10);
		
		afterDataPanel = new JPanel();
		afterDataPanel.setBackground(Color.WHITE);
		beforeAfterPanel.add(afterDataPanel);
		afterDataPanel.setBorder(new TitledBorder(null, "After modification", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		afterDataPanel.setLayout(new BorderLayout(0, 0));
		
		afterPricePanel = new JPanel();
		afterPricePanel.setBackground(Color.WHITE);
		afterDataPanel.add(afterPricePanel, BorderLayout.NORTH);
		afterPricePanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("27px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("175px"),},
			new RowSpec[] {
				RowSpec.decode("20px"),}));
		
		afterPriceLabel = new JLabel("Price:");
		afterPricePanel.add(afterPriceLabel, "1, 1, left, fill");
		
		afterPriceField = new JTextField();
		afterPriceField.setEditable(false);
		afterPricePanel.add(afterPriceField, "3, 1, fill, top");
		afterPriceField.setColumns(10);
	}
}
