package fr.enseeiht.eb4eb.generator.ui;

import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


/**
 * The activator class controls the plug-in life cycle
 */
public class EB4EBGeneratorUIPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "fr.enseeiht.eb4eb.generator.ui"; //$NON-NLS-1$
	
	// Shared constants
	public static final String THEORY_PATH_NAME = "TheoryPath";
	public static final String THEORY_CORE_PROJECT_NAME = "EB4EBCore2";
	public static final String THEORY_CORE_FILE_NAME = "EvtBTheoryProofRule";

	// The shared instance
	private static EB4EBGeneratorUIPlugin plugin;
	
	/**
	 * The constructor
	 */
	public EB4EBGeneratorUIPlugin() {
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
	 * Getting the current active page from the active workbench window.
	 * <p>
	 * 
	 * @return current active workbench page
	 */
	private IWorkbenchPage internalGetActivePage() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}
	
	/**
	 * Get the active workbench page.
	 * <p>
	 * 
	 * @return current active workbench page
	 */
	public static IWorkbenchPage getActivePage() {
		return getDefault().internalGetActivePage();
	}
	
	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static EB4EBGeneratorUIPlugin getDefault() {
		return plugin;
	}

}
