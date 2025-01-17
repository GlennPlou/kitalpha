/*******************************************************************************
 * Copyright (c) 2021 THALES GLOBAL SERVICES.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.kitalpha.sirius.rotativeimage.figures;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.sirius.diagram.WorkspaceImage;
import org.eclipse.sirius.diagram.ui.tools.api.figure.SVGWorkspaceImageFigure;
import org.polarsys.kitalpha.sirius.rotativeimage.internal.helpers.RotativeWorkspaceImageHelper;

/**
 * A rotative figure
 * 
 * @author <a href="mailto:arnaud.dieumegard@obeo.fr">Arnaud Dieumegard</a>
 */
public class Rotative4ImagesSVGWorkspaceImageFigure extends SVGWorkspaceImageFigure {

	/**
	 * One of {@link PositionConstants} among NORTH, SOUTH, EAST, WEST
	 */
	private int orientation;

	private String basepath;

	/**
	 * Creates a rotative image. Set private to force the use of static initialization
	 * {@linkplain RotativeSVGWorkspaceImageFigure.createImageFigure}.
	 */
	protected Rotative4ImagesSVGWorkspaceImageFigure() {
		super();
	}

	public static Rotative4ImagesSVGWorkspaceImageFigure createImageFigure(WorkspaceImage image, String basepath) {
		Rotative4ImagesSVGWorkspaceImageFigure figure = new Rotative4ImagesSVGWorkspaceImageFigure();
		// Set default orientation (will be changed with calls to setOrientation)
		figure.orientation = PositionConstants.NORTH;
		figure.basepath = basepath;
		figure.refreshFigure(image);
		return figure;
	}

	/**
	 * refresh the figure.
	 *
	 * @param workspaceImage
	 *            the image associated to the figure
	 */
	@Override
	public void refreshFigure(final WorkspaceImage workspaceImage) {
	    String documentKey = this.getDocumentKey();
	    if (!documentKey.equals(this.getURI())) {
	        this.setURI(this.getDocumentKey(), false);
	        this.contentChanged();
	    }
	}

	/**
	 * @param orientation
	 *            one of PositionConstants.WEST, PositionConstants.EAST, PositionConstants.SOUTH,
	 *            PositionConstants.NORTH
	 */
	public void setOrientation(int orientation) {
	    if (this.orientation != orientation) {
    		this.orientation = orientation;
    		refreshFigure(null);
	    }
	}

	@Override
	public String getDocumentKey() {
		return RotativeWorkspaceImageHelper.get4ImagesDocumentKey(basepath, orientation);
	}

}
