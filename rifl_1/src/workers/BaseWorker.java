package workers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.swing.SwingWorker;

import ui.BasePanel;
import nodes.Node;
import datamodel.Order;

public abstract class BaseWorker extends SwingWorker<Order, Order>{
	public BlockingQueue<Order> Queue;
	private BasePanel panel;
	
	public BaseWorker(BasePanel panel) {
		super();
		this.panel = panel;
	}
	
	
	


	@Override
	protected abstract Order doInBackground();
	@Override
	protected abstract void process(final List<Order> chunks);
	

}
