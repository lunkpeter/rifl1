package rifl5_ordergui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import swing2swt.layout.BorderLayout;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import swing2swt.layout.FlowLayout;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

import datamodel.Order;

public class OrderGUI {
	private static final String LBL_CALCULATE="Calculate";
	private static final String LBL_SEND="Send";
	
	private List<Order> orders = new ArrayList<Order>();
	private Order after;

	protected Shell shell;
	private StyledText txtBefore;
	private StyledText txtAfter;
	private Button btnCalculate;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			OrderGUI window = new OrderGUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		shell.setLayout(new BorderLayout(0, 0));
		
		Group grpBefore = new Group(shell, SWT.NONE);
		grpBefore.setText("Before");
		grpBefore.setLayoutData(BorderLayout.WEST);
		grpBefore.setLayout(new BorderLayout(0, 0));
		
		TextViewer textViewer = new TextViewer(grpBefore, SWT.BORDER);
		txtBefore = textViewer.getTextWidget();
		txtBefore.setLayoutData(BorderLayout.CENTER);
		
		Group grpAfter = new Group(shell, SWT.NONE);
		grpAfter.setText("After");
		grpAfter.setLayoutData(BorderLayout.EAST);
		grpAfter.setLayout(new BorderLayout(0, 0));
		
		TextViewer textViewer_1 = new TextViewer(grpAfter, SWT.BORDER);
		txtAfter = textViewer_1.getTextWidget();
		txtAfter.setLayoutData(BorderLayout.CENTER);
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(BorderLayout.CENTER);
		composite.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnCalculate = new Button(composite, SWT.NONE);
		btnCalculate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				btnCalculate.setEnabled(false);
				if(LBL_CALCULATE.equals(btnCalculate.getText())) {
					//actor.calc
					btnCalculate.setText(LBL_SEND);
				} else if(LBL_SEND.equals(btnCalculate.getText())) {
					//actor.send
					orders.remove(after);
					btnCalculate.setText(LBL_CALCULATE);
					if(orders.size()>0) {
						setBefore(orders.get(0));
					}
				}
			}
		});
		btnCalculate.setText("Calculate");

	}
	
	public void addOrder(Order order) {
		orders.add(order);
		if(orders.size()==1) {
			setBefore(orders.get(0));
		}
	}
	
	private void setBefore(Order order) {
		txtBefore.setText(order.toString());
		btnCalculate.setEnabled(true);
	}
	
	public void setAfter(Order order) {
		txtAfter.setText(order.toString());
		btnCalculate.setEnabled(true);
	}
}
