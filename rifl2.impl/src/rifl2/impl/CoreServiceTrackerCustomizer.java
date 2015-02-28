package rifl2.impl;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;


import org.osgi.util.tracker.ServiceTrackerCustomizer;

import rifl2.core.CoreCommand;
import rifl2.interfaces.ICalculator;
import rifl2.interfaces.IDeliveryCalculator;
import rifl2.interfaces.IDiscountCalculator;
import rifl2.interfaces.IDistanceCalculator;
import rifl2.interfaces.IFullPriceCalculator;
import rifl2.interfaces.INetPriceCalculator;
import rifl2.interfaces.IOrderPriceCalculator;

public class CoreServiceTrackerCustomizer implements ServiceTrackerCustomizer {
	private final BundleContext context;
	private List<IOrderPriceCalculator> ordercalc;
	private List<IDeliveryCalculator> delivcalc;
	private List<IDiscountCalculator> discountcalc;
	private List<IDistanceCalculator> distancecalc;
	private List<IFullPriceCalculator> fullpricecalc;
	private List<INetPriceCalculator> netpricecalc;
	private List<CoreCommand> command;
	
	
	

	public List<IOrderPriceCalculator> getOrdercalc() {
		return ordercalc;
	}

	public List<IDeliveryCalculator> getDelivcalc() {
		return delivcalc;
	}

	public List<IDiscountCalculator> getDiscountcalc() {
		return discountcalc;
	}

	public List<IDistanceCalculator> getDistancecalc() {
		return distancecalc;
	}

	public List<IFullPriceCalculator> getFullpricecalc() {
		return fullpricecalc;
	}

	public List<INetPriceCalculator> getNetpricecalc() {
		return netpricecalc;
	}


	public CoreServiceTrackerCustomizer(BundleContext context) {
		this.context = context;
		ordercalc = new ArrayList<IOrderPriceCalculator>();
		delivcalc = new ArrayList<IDeliveryCalculator>();
		discountcalc = new ArrayList<IDiscountCalculator>();
		distancecalc = new ArrayList<IDistanceCalculator>();
		fullpricecalc = new ArrayList<IFullPriceCalculator>();
		netpricecalc = new ArrayList<INetPriceCalculator>();
		command = new ArrayList<CoreCommand>();
	}

	@Override
	public Object addingService(ServiceReference reference) {
		
		Object service = context.getService(reference);
		if(service instanceof CoreCommand){
			command.add((CoreCommand) service);
			try {
				command.get(0).setDeliveryService(delivcalc.get(0));
				command.get(0).setOrderpriceService(ordercalc.get(0));
				command.get(0).setDiscountService(discountcalc.get(0));
				command.get(0).setDistanceService(distancecalc.get(0));
				command.get(0).setFullPriceService(fullpricecalc.get(0));
				command.get(0).setNetpriceService(netpricecalc.get(0));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return service;
	}

	@Override
	public void modifiedService(ServiceReference reference, Object service) {
	}

	@Override
	public void removedService(ServiceReference reference, Object service) {
		if(service instanceof IOrderPriceCalculator){
			ordercalc.remove((IOrderPriceCalculator) service);
		}
		if(service instanceof IDeliveryCalculator){
			delivcalc.remove((IDeliveryCalculator) service);
		}
		if(service instanceof IDiscountCalculator){
			discountcalc.remove((IDiscountCalculator) service);
		}
		if(service instanceof IDistanceCalculator){
			distancecalc.remove((IDistanceCalculator) service);
		}
		if(service instanceof IFullPriceCalculator){
			fullpricecalc.remove((IFullPriceCalculator) service);
		}
		if(service instanceof INetPriceCalculator){
			netpricecalc.remove((INetPriceCalculator) service);
		}
	}
}
