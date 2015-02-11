package nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import datamodel.Order;

public abstract class Node implements Runnable{
	public boolean exit;
	public BlockingQueue<Order> Queue;
	public List<Node> NextNodes;
	
	
	public Node() {
		super();
		NextNodes = new ArrayList<Node>();
		Queue = new ArrayBlockingQueue<Order>(1, true);
		exit = false; 
	}
	
	
	public Node(Node nextNode) {
		super();
		NextNodes = new ArrayList<Node>();
		NextNodes.add(nextNode);
		Queue = new ArrayBlockingQueue<Order>(1, true);
		exit = false; 
	}
	
	public Node(List<Node> nextNode) {
		super();
		NextNodes = new ArrayList<Node>();
		NextNodes.addAll(nextNode);
		Queue = new ArrayBlockingQueue<Order>(1, true);
		exit = false; 
	}



	@Override
	public abstract void run();

}
