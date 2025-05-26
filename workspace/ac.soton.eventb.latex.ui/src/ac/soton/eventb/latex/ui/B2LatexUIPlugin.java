/*******************************************************************************
 * Copyright (c) 2012 Systerel and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Systerel - Initial API and implementation
 *******************************************************************************/
package ac.soton.eventb.latex.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * Activator class of the B2Latex plugin.
 * 
 * @author Thomas Muller
 */
public class B2LatexUIPlugin extends AbstractUIPlugin {
	
	// The shared instance
	private static B2LatexUIPlugin plugin;
	
	// Boolean constant indicating DEBUG mode;
	public static boolean DEBUG;

	// The plug-in ID
	public static final String PLUGIN_ID = "fr.systerel.editor";
		
	/**
	 * The constructor
	 */
	public B2LatexUIPlugin() {
		DEBUG = isDebugging();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		if (isDebugging())
			configureDebugOptions();
	}

	private void configureDebugOptions() {
		// NOTHING FOR NOW.
	}

//	private static boolean parseOption(String key) {
//		final String option = Platform.getDebugOption(key);
//		return "true".equalsIgnoreCase(option); //$NON-NLS-1$
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
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
	public static B2LatexUIPlugin getDefault() {
		return plugin;
	}

}
