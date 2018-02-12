/*******************************************************************************
 * Copyright (c) 2016-2018 Thales Global Services S.A.S.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *   Thales Global Services S.A.S - initial API and implementation
 ******************************************************************************/
package org.polarsys.kitalpha.doc.gen.business.core.extension.intf;

import org.polarsys.kitalpha.doc.gen.business.core.extension.imp.DocGenExtensionEngine;
import org.polarsys.kitalpha.doc.gen.business.core.extension.imp.DocGenExtensionManager;

/**
 * Factory to instantiate infrastructures to access/execute contributions
 * @author Faycal Abka
 *
 */
public class DocGenExtensionFactory {
	
	/**
	 * Hidden constructor
	 */
	private DocGenExtensionFactory() {
	}
	
	/**
	 * @return singleton of Doc Gen extension manager
	 */
	public static IDocGenExtension getDocGenExtensionManager(){
		return DocGenExtensionManager.getInstance();
	}
	
	/**
	 * @return a new Doc Gen Extension engine to execute patterns
	 */
	public static IDocGenExtensionEngine newDocGenExtensionEngine(){
		return new DocGenExtensionEngine();
	}

}
