package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import datamodel.DeliveryMethod;
import datamodel.Item;
import datamodel.Region;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFormattedTextField;

@SuppressWarnings("serial")
public class CreateOrderPanel extends JPanel {
	private JTextField nameField;
	private JFormattedTextField priceField;
	private JTextField custNameField;
	
	private List<Item> items;
	private JComboBox<Region> custRegionCombo;
	private JComboBox<DeliveryMethod> delMethodCombo;
	private JList<Item> itemsList;

	/**
	 * Create the panel.
	 */
	public CreateOrderPanel() {
		items = new ArrayList<Item>();
		
		setBackground(Color.WHITE);
		setBorder(new TitledBorder(null, "Create order", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new BorderLayout(0, 0));
		
		JPanel buttonPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		buttonPanel.setBackground(Color.WHITE);
		add(buttonPanel, BorderLayout.SOUTH);
		
		JButton btnCreateOrder = new JButton("Create order");
		buttonPanel.add(btnCreateOrder);
		
		JPanel orderPropPanel = new JPanel();
		orderPropPanel.setBackground(Color.WHITE);
		add(orderPropPanel, BorderLayout.NORTH);
		orderPropPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("86px:grow"),},
			new RowSpec[] {
				RowSpec.decode("20px"),
				RowSpec.decode("20px"),
				RowSpec.decode("20px"),}));
		
		JLabel lblCostumerName = new JLabel("Costumer name:");
		orderPropPanel.add(lblCostumerName, "2, 1, right, center");
		
		custNameField = new JTextField();
		orderPropPanel.add(custNameField, "4, 1, fill, top");
		custNameField.setColumns(10);
		
		JLabel lblCustomerRegion = new JLabel("Customer region:");
		orderPropPanel.add(lblCustomerRegion, "2, 2, right, center");
		
		custRegionCombo = new JComboBox<Region>();
		custRegionCombo.setModel(new DefaultComboBoxModel<Region>(Region.values()));
		orderPropPanel.add(custRegionCombo, "4, 2, fill, default");
		
		JLabel lblDeliveryMethod = new JLabel("Delivery method:");
		orderPropPanel.add(lblDeliveryMethod, "2, 3, right, center");
		
		delMethodCombo = new JComboBox<DeliveryMethod>();
		delMethodCombo.setModel(new DefaultComboBoxModel<DeliveryMethod>(DeliveryMethod.values()));
		orderPropPanel.add(delMethodCombo, "4, 3, fill, default");
		
		JPanel itemsPanel = new JPanel();
		itemsPanel.setBackground(Color.WHITE);
		add(itemsPanel, BorderLayout.WEST);
		itemsPanel.setLayout(new BorderLayout(0, 0));
		
		itemsList = new JList<Item>();
		itemsPanel.add(itemsList, BorderLayout.WEST);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 0, 0, 0));
		panel.setBackground(Color.WHITE);
		itemsPanel.add(panel, BorderLayout.EAST);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {71};
		gbl_panel.rowHeights = new int[]{23, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JButton buttonAdd = new JButton("<< Add");
		buttonAdd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					Item newItem = new Item(Integer.parseInt(priceField.getText()), nameField.getText());
					boolean alreadyExists = false;
					for (Item item : items) {
						if(item.getName().equals(newItem.getName())) {
							alreadyExists = true;
						}
					}
					if(!alreadyExists) {
						items.add(newItem);
						updateItemsList();
						priceField.setText("0");
						nameField.setText("");
					} else {
						JOptionPane.showMessageDialog(arg0.getComponent(), 
								"This item already exists. It will not be added to the list.", 
								"Data problem", 
								JOptionPane.WARNING_MESSAGE);
					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(arg0.getComponent(), 
							"Incorrect cost.", 
							"Data error", 
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		GridBagConstraints gbc_buttonAdd = new GridBagConstraints();
		gbc_buttonAdd.fill = GridBagConstraints.HORIZONTAL;
		gbc_buttonAdd.anchor = GridBagConstraints.NORTH;
		gbc_buttonAdd.insets = new Insets(5, 5, 5, 5);
		gbc_buttonAdd.gridx = 0;
		gbc_buttonAdd.gridy = 0;
		panel.add(buttonAdd, gbc_buttonAdd);
		
		JButton btnDeleteAll = new JButton("Delete all");
		btnDeleteAll.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				items.clear();
				updateItemsList();
			}
		});
		GridBagConstraints gbc_btnDeleteAll = new GridBagConstraints();
		gbc_btnDeleteAll.insets = new Insets(0, 5, 0, 5);
		gbc_btnDeleteAll.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnDeleteAll.gridx = 0;
		gbc_btnDeleteAll.gridy = 1;
		panel.add(btnDeleteAll, gbc_btnDeleteAll);
		
		JPanel newItemPanel = new JPanel();
		newItemPanel.setBackground(Color.WHITE);
		newItemPanel.setBorder(new TitledBorder(null, "New item", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(newItemPanel, BorderLayout.CENTER);
		newItemPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("31px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("150px:grow"),},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblName = new JLabel("Name:");
		newItemPanel.add(lblName, "2, 2, left, center");
		
		nameField = new JTextField();
		newItemPanel.add(nameField, "4, 2, fill, top");
		nameField.setColumns(10);
		
		JLabel lblPrice = new JLabel("Price:");
		newItemPanel.add(lblPrice, "2, 3, right, default");
		
		priceField = new JFormattedTextField(NumberFormat.getNumberInstance());
		newItemPanel.add(priceField, "4, 3, fill, default");
		priceField.setColumns(10);

	}
	
	private void updateItemsList() {
		itemsList.setCellRenderer(new DefaultListCellRenderer() {

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
		for (Item item : items) {
			model.addElement(item);
		}
		itemsList.setModel(model);
	}

}
