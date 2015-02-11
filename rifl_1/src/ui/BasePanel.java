package ui;

import javax.swing.JPanel;

import datamodel.Order;

public abstract class BasePanel extends JPanel{
	public abstract void setBeforeData(Order o);
	public abstract void setAfterData(Order o);
}
