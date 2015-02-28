package rifl2.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import rifl2.core.CoreCommand;
import rifl2.interfaces.IDeliveryCalculator;
import rifl2.interfaces.IDiscountCalculator;
import rifl2.interfaces.IDistanceCalculator;
import rifl2.interfaces.IFullPriceCalculator;
import rifl2.interfaces.INetPriceCalculator;
import rifl2.interfaces.IOrderPriceCalculator;

public class Activator implements BundleActivator {

	private IFullPriceCalculator fullPriceService;
	private IDeliveryCalculator deliveryService;
	private INetPriceCalculator netpriceService;
	private IDistanceCalculator distanceService;
	private IDiscountCalculator discountService;
	private OrderPriceCalculator orderpriceService;

	ServiceTracker serviceTracker;

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		fullPriceService = new FullPriceCalculator();
		context.registerService(
				IFullPriceCalculator.class.getName(), fullPriceService, null);
		System.out.println("IFullPriceCalculator service is registered!");

		deliveryService = new DeliveryCalculator();
		deliveryService.setFullPrice(fullPriceService);
		context.registerService(
				IDeliveryCalculator.class.getName(), deliveryService, null);
		System.out.println("IDeliveryCalculator service is registered!");

		netpriceService = new NetPriceCalculator();
		netpriceService.setFullPrice(fullPriceService);
		context.registerService(
				INetPriceCalculator.class.getName(), netpriceService, null);
		System.out.println("INetPriceCalculator service is registered!");

		distanceService = new DistanceCalculator();
		distanceService.setDelivery(deliveryService);
		context.registerService(
				IDistanceCalculator.class.getName(), distanceService, null);
		System.out.println("IDistanceCalculator service is registered!");

		discountService = new DiscountCalculator();
		discountService.setNetPrice(netpriceService);
		context.registerService(
				IDiscountCalculator.class.getName(), discountService, null);
		System.out.println("IDiscountCalculator service is registered!");

		orderpriceService = new OrderPriceCalculator();
		orderpriceService.setDiscount(discountService);
		orderpriceService.setDistance(distanceService);
		context.registerService(
				IOrderPriceCalculator.class.getName(), orderpriceService, null);
		System.out.println("IOrderPriceCalculator service is registered!");

		CoreServiceTrackerCustomizer customizer = new CoreServiceTrackerCustomizer(
				context);
		customizer.getOrdercalc().add(orderpriceService);
		customizer.getDelivcalc().add(deliveryService);
		customizer.getDistancecalc().add(distanceService);
		customizer.getDiscountcalc().add(discountService);
		customizer.getFullpricecalc().add(fullPriceService);
		customizer.getNetpricecalc().add(netpriceService);

		serviceTracker = new ServiceTracker(context,
				CoreCommand.class.getName(), customizer);
		serviceTracker.open();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		serviceTracker.close();
	}

}
