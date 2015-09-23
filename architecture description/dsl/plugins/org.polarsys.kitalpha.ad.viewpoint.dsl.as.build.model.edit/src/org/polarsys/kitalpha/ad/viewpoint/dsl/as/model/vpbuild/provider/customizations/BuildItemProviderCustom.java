/*******************************************************************************
 * Copyright (c) 2014 Thales Global Services S.A.S.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *  Thales Global Services S.A.S - initial API and implementation
 ******************************************************************************/

package org.polarsys.kitalpha.ad.viewpoint.dsl.as.model.vpbuild.provider.customizations;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.polarsys.kitalpha.ad.viewpoint.dsl.as.model.vpbuild.provider.BuildItemProvider;

/**
 * 
 * @author Faycal Abka
 *
 */
public class BuildItemProviderCustom extends BuildItemProvider {

	public BuildItemProviderCustom(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}
	
	@Override
	public String getText(Object object) {
		return super.getText(object);
	}
	
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/Build.png")); //$NON-NLS-1$
	}

}
