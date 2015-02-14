package workers;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.swing.SwingWorker;

import ui.BasePanel;
import datamodel.Order;

public abstract class BaseWorker extends SwingWorker<Order, Order>{
	public boolean exit;
	public boolean isrunning;
	public BlockingQueue<Order> Queue;
	public BasePanel panel;
	
	public BaseWorker(BasePanel panel) {
		super();
		this.panel = panel;
		Queue = new ArrayBlockingQueue<Order>(10, true);
		exit = false; 
		isrunning = false;
	}

	@Override
	protected abstract Order doInBackground();
	@Override
	protected abstract void process(final List<Order> chunks);
	

}
