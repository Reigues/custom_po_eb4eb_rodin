/*******************************************************************************
 * Copyright (c) 2010 Systerel and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Systerel - initial API and implementation
 *******************************************************************************/
package fr.systerel.rodinextension.sample;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class QualProbPlugin extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "fr.systerel.rodinextension.sample"; //$NON-NLS-1$

	// The shared instance
	private static QualProbPlugin plugin;
	
	/**
	 * The constructor
	 */
	public QualProbPlugin() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		if (isDebugging())
			configureDebugOptions();
	}

	private void configureDebugOptions() {
		// do nothing for the moment
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
	public static QualProbPlugin getDefault() {
		return plugin;
	}

}
