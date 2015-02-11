package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import datamodel.Order;

@SuppressWarnings("serial")
public class FullPriceCalculatorPanel extends BasePanel {

	private JPanel buttonPanel;
	private JPanel beforeAfterPanel;
	private JPanel beforeDataPanel;
	private JPanel afterDataPanel;
	private JPanel afterPricePanel;
	private JLabel afterPriceLabel;
	private JTextField afterPriceField;
	private JButton nextButton;
	private JPanel beforePricePanel;
	private JLabel beforePriceLabel;
	private JTextField beforePriceField;
	private JLabel beforeDeliveryPriceLabel;
	private JTextField beforeDeliveryPriceField;

	/**
	 * Create the panel.
	 */
	public FullPriceCalculatorPanel() {
		setBackground(Color.WHITE);
		setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Full price calculation", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		setLayout(new BorderLayout(0, 0));
		
		setButtonPanel();
		setBeforeAfterPanel();
	}

	public void setBeforeData(Order o) {
		beforePriceField.setText(String.valueOf(o.getPriceData().getPrice()));
		
		beforeDeliveryPriceField.setText(String.valueOf(o.getDeliveryData().getDeliveryCost()));
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
		
		beforePricePanel = new JPanel();
		beforePricePanel.setBackground(Color.WHITE);
		beforeDataPanel.add(beforePricePanel, BorderLayout.NORTH);
		beforePricePanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				RowSpec.decode("20px"),
				RowSpec.decode("20px"),}));
		
		beforePriceLabel = new JLabel("Price:");
		beforePricePanel.add(beforePriceLabel, "1, 1, right, fill");
		
		beforePriceField = new JTextField();
		beforePriceField.setEditable(false);
		beforePricePanel.add(beforePriceField, "3, 1, fill, top");
		beforePriceField.setColumns(10);
		
		beforeDeliveryPriceLabel = new JLabel("Delivery price:");
		beforePricePanel.add(beforeDeliveryPriceLabel, "1, 2, right, default");
		
		beforeDeliveryPriceField = new JTextField();
		beforeDeliveryPriceField.setEditable(false);
		beforePricePanel.add(beforeDeliveryPriceField, "3, 2, fill, default");
		beforeDeliveryPriceField.setColumns(10);
		
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
