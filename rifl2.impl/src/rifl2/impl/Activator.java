package rifl2.impl;

import org.apache.felix.service.command.Descriptor;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import rifl2.datamodel.CustomerData;
import rifl2.datamodel.DeliveryData;
import rifl2.datamodel.DeliveryMethod;
import rifl2.datamodel.Item;
import rifl2.datamodel.Order;
import rifl2.datamodel.PriceData;
import rifl2.datamodel.Region;
import rifl2.interfaces.IDeliveryCalculator;
import rifl2.interfaces.IDiscountCalculator;
import rifl2.interfaces.IDistanceCalculator;
import rifl2.interfaces.IFullPriceCalculator;
import rifl2.interfaces.INetPriceCalculator;
import rifl2.interfaces.IOrderPriceCalculator;

public class Activator implements BundleActivator {
	
	private static IFullPriceCalculator fullPriceService;
	private static IDeliveryCalculator deliveryService;
	private static INetPriceCalculator netpriceService;
	private static IDistanceCalculator distanceService;
	private static IDiscountCalculator discountService;
	private static OrderPriceCalculator orderpriceService;
	

	private ServiceRegistration registration;
	private static int step = 0;

	private static BundleContext context;

	@Descriptor(value = "A simple command that demonstrates OSGi felix console.")
	public void step() {

		switch (step) {
		case 0:
			orderpriceService.init();
			discountService.init();
			distanceService.init();
			netpriceService.init();
			deliveryService.init();
			fullPriceService.init();
			
			CustomerData customerData = new CustomerData("TEST", Region.Central);
			DeliveryData deliveryData = new DeliveryData(
					DeliveryMethod.PrivateDelivery);
			PriceData priceData = new PriceData();
			Order order = new Order(customerData, deliveryData, priceData);
			for (int i = 0; i < 10; i++) {
				order.addItem(new Item(25000, "TEST" + i));
			}

			orderpriceService.enQueue(order);
			break;
		case 1:
			orderpriceService.setRunning(true);
			
			break;
		case 2:
			discountService.setRunning(true);
			
			break;
		case 3:
			distanceService.setRunning(true);
			
			break;

		case 4:
			netpriceService.setRunning(true);
			
			break;
		case 5:
			deliveryService.setRunning(true);
			
			break;
		case 6:
			fullPriceService.setRunning(true);
			break;


		default:
			step = -1;
			break;
		}
		step++;
	}

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
		registration = context.registerService(
				IFullPriceCalculator.class.getName(), fullPriceService, null);
		System.out.println("IFullPriceCalculator service is registered!");

		deliveryService = new DeliveryCalculator();
		deliveryService.setFullPrice(fullPriceService);
		registration = context.registerService(
				IDeliveryCalculator.class.getName(), deliveryService, null);
		System.out.println("IDeliveryCalculator service is registered!");

		netpriceService = new NetPriceCalculator();
		netpriceService.setFullPrice(fullPriceService);
		registration = context.registerService(
				INetPriceCalculator.class.getName(), netpriceService, null);
		System.out.println("INetPriceCalculator service is registered!");

		distanceService = new DistanceCalculator();
		distanceService.setDelivery(deliveryService);
		registration = context.registerService(
				IDistanceCalculator.class.getName(), distanceService, null);
		System.out.println("IDistanceCalculator service is registered!");

		discountService = new DiscountCalculator();
		discountService.setNetPrice(netpriceService);
		registration = context.registerService(
				IDiscountCalculator.class.getName(), discountService, null);
		System.out.println("IDiscountCalculator service is registered!");

		orderpriceService = new OrderPriceCalculator();
		orderpriceService.setDiscount(discountService);
		orderpriceService.setDistance(distanceService);
		registration = context.registerService(
				IOrderPriceCalculator.class.getName(), orderpriceService, null);
		System.out.println("IOrderPriceCalculator service is registered!");

		


		

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
