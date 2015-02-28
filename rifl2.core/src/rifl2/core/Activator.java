package rifl2.core;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class Activator implements BundleActivator {

	
	ServiceTracker serviceTracker;


	public void start(BundleContext context) throws Exception {
		System.out.println("ICoreService service is registered!");
		
		ServiceTrackerCustomizer customizer =
				 new CoreServiceTrackerCustomizer(context);
				 serviceTracker = new ServiceTracker(context,
						 CoreServiceTrackerCustomizer.class.getName(), customizer);
				 serviceTracker.open();
	}

	public void stop(BundleContext context) throws Exception {
		serviceTracker.close();

		System.out.println("ICoreService service is unregistered!");
	}

}
