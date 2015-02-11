package ui;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.swing.JPanel;

import datamodel.Order;

public abstract class BasePanel extends JPanel{
	public BlockingQueue<Order> Queue;
	public List<BasePanel> NextPanels;
	
	
	
	
	
	public abstract void setBeforeData(Order o);
	public abstract void setAfterData(Order o);
}
