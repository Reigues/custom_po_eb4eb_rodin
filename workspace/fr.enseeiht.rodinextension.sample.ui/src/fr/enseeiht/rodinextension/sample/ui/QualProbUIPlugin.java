package fr.enseeiht.rodinextension.sample.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class QualProbUIPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "fr.enseeiht.rodinextension.sample.ui"; //$NON-NLS-1$

	// The shared instance
	private static QualProbUIPlugin plugin;
	
	/**
	 * The constructor
	 */
	public QualProbUIPlugin() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static QualProbUIPlugin getDefault() {
		return plugin;
	}

}
