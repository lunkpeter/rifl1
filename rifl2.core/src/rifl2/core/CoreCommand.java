package rifl2.core;

import org.apache.felix.service.command.Descriptor;

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

/**
 * GoGo command class. The user can use our order calculator through this class.
 *
 */
public class CoreCommand {
	
	
	private IOrderPriceCalculator orderpriceService;
	private IDeliveryCalculator deliveryService;
	private IDiscountCalculator discountService;
	private IDistanceCalculator distanceService;
	private IFullPriceCalculator fullPriceService;
	private INetPriceCalculator netpriceService;
	
	private int step = 0;

	/**
	 * The only command for the user. The user can step our work flow.
	 */
	@Descriptor(value = "RIFL command. You can step the workflow with this command.")
	public void step() {
		try {
			switch (step) {
			// Initial step
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
				System.out.println("Initialized");
				break;
			// Calculate the base price
			case 1:
				orderpriceService.setRunning(true);
				
				break;
			// Calculate the discount price
			case 2:
				discountService.setRunning(true);
				
				break;
			// Calculate the delivery distance
			case 3:
				distanceService.setRunning(true);
				
				break;
			// Calculate the net price
			case 4:
				netpriceService.setRunning(true);
				
				break;
			// Calculate the delivery price
			case 5:
				deliveryService.setRunning(true);
				
				break;
			// Calculate the full price
			case 6:
				fullPriceService.setRunning(true);
				break;


			default:
				step = -1;
				break;
			}
			step++;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	/*************** Getters and setters for private fields. ***************/
	
	public IOrderPriceCalculator getOrderpriceService() {
		return orderpriceService;
	}

	public void setOrderpriceService(IOrderPriceCalculator orderpriceService) {
		this.orderpriceService = orderpriceService;
	}

	public IDeliveryCalculator getDeliveryService() {
		return deliveryService;
	}

	public void setDeliveryService(IDeliveryCalculator deliveryService) {
		this.deliveryService = deliveryService;
	}

	public IDiscountCalculator getDiscountService() {
		return discountService;
	}

	public void setDiscountService(IDiscountCalculator discountService) {
		this.discountService = discountService;
	}

	public IDistanceCalculator getDistanceService() {
		return distanceService;
	}

	public void setDistanceService(IDistanceCalculator distanceService) {
		this.distanceService = distanceService;
	}

	public IFullPriceCalculator getFullPriceService() {
		return fullPriceService;
	}

	public void setFullPriceService(IFullPriceCalculator fullPriceService) {
		this.fullPriceService = fullPriceService;
	}

	public INetPriceCalculator getNetpriceService() {
		return netpriceService;
	}

	public void setNetpriceService(INetPriceCalculator netpriceService) {
		this.netpriceService = netpriceService;
	}


	

}
