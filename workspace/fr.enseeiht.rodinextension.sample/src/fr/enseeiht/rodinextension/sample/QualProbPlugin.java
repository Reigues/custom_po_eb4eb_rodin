package fr.enseeiht.rodinextension.sample;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class QualProbPlugin implements BundleActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "fr.enseeiht.rodinextension.sample"; //$NON-NLS-1$
	
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		QualProbPlugin.context = bundleContext;
	}

	public void stop(BundleContext bundleContext) throws Exception {
		QualProbPlugin.context = null;
	}

}
