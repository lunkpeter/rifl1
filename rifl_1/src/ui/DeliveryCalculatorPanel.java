package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import workers.DeliveryWorker;
import datamodel.Order;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

@SuppressWarnings("serial")
public class DeliveryCalculatorPanel extends BasePanel {

	private JPanel buttonPanel;
	private JPanel beforeAfterPanel;
	private JPanel beforeDataPanel;
	private JPanel afterDataPanel;
	private JPanel afterPricePanel;
	private JLabel afterDPriceLabel;
	private JTextField afterPriceField;
	private JButton nextButton;
	private JPanel beforePricePanel;
	private JLabel beforeDPriceLabel;
	private JTextField beforePriceField;

	/**
	 * Create the panel.
	 * @param fullPriceCalculatorPanel 
	 */
	public DeliveryCalculatorPanel(FullPriceCalculatorPanel fullPriceCalculatorPanel) {
		setBackground(Color.WHITE);
		setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Delivery calculation", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		setLayout(new BorderLayout(0, 0));
		
		setButtonPanel();
		setBeforeAfterPanel();
		
		worker = new DeliveryWorker(this);
		
		NextPanels = new ArrayList<BasePanel>();
		NextPanels.add(fullPriceCalculatorPanel);
	}

	public void setBeforeData(final Order o) {
		beforePriceField.setText(String.valueOf(o.getDeliveryData().getDeliveryCost()));
		
		nextButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				nextButton.setEnabled(false);
				
				for (BasePanel basePanel : NextPanels) {
					basePanel.setBeforeData(o);
				}
			}
		});
		
		afterPriceField.setText("");
	}
	
	public void setAfterData(Order o) {
		afterPriceField.setText(String.valueOf(o.getDeliveryData().getDeliveryCost()));
		
		nextButton.setEnabled(true);
	}

	private void setButtonPanel() {
		buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.WHITE);
		FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		add(buttonPanel, BorderLayout.SOUTH);
		
		nextButton = new JButton("Next");
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
				ColumnSpec.decode("69px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("133px"),},
			new RowSpec[] {
				RowSpec.decode("20px"),}));
		
		beforeDPriceLabel = new JLabel("Delivery price:");
		beforePricePanel.add(beforeDPriceLabel, "1, 1, left, fill");
		
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
				ColumnSpec.decode("69px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("133px"),},
			new RowSpec[] {
				RowSpec.decode("20px"),}));
		
		afterDPriceLabel = new JLabel("Delivery price:");
		afterPricePanel.add(afterDPriceLabel, "1, 1, left, fill");
		
		afterPriceField = new JTextField();
		afterPriceField.setEditable(false);
		afterPricePanel.add(afterPriceField, "3, 1, fill, top");
		afterPriceField.setColumns(10);
	}
}
