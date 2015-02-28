package rifl2.core;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;


import org.osgi.util.tracker.ServiceTrackerCustomizer;

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
	
	
	

	public CoreServiceTrackerCustomizer(BundleContext context) {
		this.context = context;
		ordercalc = new ArrayList<IOrderPriceCalculator>();
		delivcalc = new ArrayList<IDeliveryCalculator>();
		discountcalc = new ArrayList<IDiscountCalculator>();
		distancecalc = new ArrayList<IDistanceCalculator>();
		fullpricecalc = new ArrayList<IFullPriceCalculator>();
		netpricecalc = new ArrayList<INetPriceCalculator>();
	}

	@Override
	public Object addingService(ServiceReference reference) {
		ICalculator service = (ICalculator) context.getService(reference);
		if(service instanceof CoreCommand){
			command.add((CoreCommand) service);
		}
		if(service instanceof IOrderPriceCalculator){
			ordercalc.add((IOrderPriceCalculator) service);
		}
		if(service instanceof IDeliveryCalculator){
			delivcalc.add((IDeliveryCalculator) service);
		}
		if(service instanceof IDiscountCalculator){
			discountcalc.add((IDiscountCalculator) service);
		}
		if(service instanceof IDistanceCalculator){
			distancecalc.add((IDistanceCalculator) service);
		}
		if(service instanceof IFullPriceCalculator){
			fullpricecalc.add((IFullPriceCalculator) service);
		}
		if(service instanceof INetPriceCalculator){
			netpricecalc.add((INetPriceCalculator) service);
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
