package rifl2.core;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {


	public void start(BundleContext context) throws Exception {
		System.out.println("ICoreService service is registered!");

		
	}

	public void stop(BundleContext context) throws Exception {


		System.out.println("ICoreService service is unregistered!");
	}

}
