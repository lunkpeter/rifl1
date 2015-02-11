package ui;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.swing.JPanel;

import workers.BaseWorker;
import datamodel.Order;

public abstract class BasePanel extends JPanel{
	public List<BasePanel> NextPanels;
	public BaseWorker worker;
	
	
	
	
	public abstract void setBeforeData(Order o);
	public abstract void setAfterData(Order o);
}
