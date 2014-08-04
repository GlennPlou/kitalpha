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

package org.polarsys.kitalpha.ad.viewpoint.dsl.cs.text.ui.contentassist;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;
import org.osgi.framework.Bundle;
import org.polarsys.kitalpha.ad.viewpoint.dsl.as.model.vpdesc.AbstractAttributeType;
import org.polarsys.kitalpha.ad.viewpoint.dsl.as.model.vpdesc.AbstractResource;
import org.polarsys.kitalpha.ad.viewpoint.dsl.as.model.vpdesc.Attribute;
import org.polarsys.kitalpha.ad.viewpoint.dsl.as.model.vpdesc.Data;
import org.polarsys.kitalpha.ad.viewpoint.dsl.as.model.vpdesc.ExternalAttributeType;
import org.polarsys.kitalpha.ad.viewpoint.dsl.as.model.vpdesc.FileSystemResource;
import org.polarsys.kitalpha.ad.viewpoint.dsl.as.model.vpdesc.Viewpoint;
import org.polarsys.kitalpha.ad.viewpoint.dsl.as.model.vpdesc.ViewpointResources;
import org.polarsys.kitalpha.ad.viewpoint.dsl.cs.text.resources.ResourceHelper;
import org.polarsys.kitalpha.ad.viewpoint.dsl.cs.text.services.DataGrammarAccess;

import com.google.inject.Inject;

/**
 * 
 * @author Amine Lajmi
 * 
 */
public class DataProposalProvider extends AbstractDataProposalProvider {

	@Inject
	DataGrammarAccess grammar;

	@Override
	public void completeKeyword(Keyword keyword,
			ContentAssistContext contentAssistContext,
			ICompletionProposalAcceptor acceptor) {
		ICompletionProposal proposal = createCompletionProposal(
				keyword.getValue(), getKeywordDisplayString(keyword),
				getImage(keyword), contentAssistContext);
		if (proposal == null)
			return;
		EObject current = contentAssistContext.getCurrentModel();
		if (current != null && NodeModelUtils.getNode(current) != null) {
			// Case for Enumerators: display "values" only for EEnumerators
			ICompositeNode node = NodeModelUtils.getNode(current);
			EObject currentObject = NodeModelUtils
					.findActualSemanticObjectFor(node);
			if (currentObject instanceof Attribute) {
				Attribute currentAtt = (Attribute) currentObject;
				AbstractAttributeType type = currentAtt.getOwned_type();
				// make sure type is already defined
				if (type != null && type.getName() != null) {
					if (type instanceof ExternalAttributeType) {
						if (!type.getName().equals("EEnumerator")
								&& proposal.getDisplayString().matches(
										grammar.getAttributeAccess()
												.getValuesKeyword_6_0()
												.getValue())) {
							return;
						}
					}
				}
			}
			ICompositeNode rootNode = NodeModelUtils.getNode(current)
					.getRootNode();
			EObject root = NodeModelUtils.findActualSemanticObjectFor(rootNode);
			if (root != null && root instanceof Viewpoint) {
				// Check if Data is already defined
				Data vp_Data = ((Viewpoint) root).getVP_Data();
				if (vp_Data != null
						&& proposal.getDisplayString().matches(
								grammar.getDataAccess().getDataKeyword_1()
										.getValue())) {
					return;
				}
			}
		}
		getPriorityHelper().adjustKeywordPriority(proposal,
				contentAssistContext.getPrefix());
		acceptor.accept(proposal);
	}

	@Override
	public void completeClass_Icon(EObject model, Assignment assignment,
			ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		XtextResource resource = (XtextResource) model.eResource();
		String platformString = resource.getURI().toPlatformString(true);
		IFile myFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new Path(platformString));
		IProject currentProject = myFile.getProject();
		IFolder iconFolder = currentProject.getFolder("icons");
		IPath iconRelativePath = iconFolder.getProjectRelativePath();
		List<IResource> collectICons = collectICons(iconFolder,
				new ArrayList<IResource>());
		
		//To decomment later
//		List<IResource> importedResources = getImportedResources(model);
//		
//		for (IResource iResource : importedResources) {
//			collectICons = collectICons(iResource, collectICons);
//		}
		
		for (IResource candidate : collectICons) {
			if (candidate instanceof IFile) {
				IFile xfile = (IFile) candidate;
				try {
					Image image = getImage(xfile.getLocation());
					IPath projectRelativePath = xfile.getProjectRelativePath();
					IPath makeRelativeTo = projectRelativePath
							.makeRelativeTo(iconRelativePath);
					acceptor.accept(createCompletionProposal(
							"\"" + makeRelativeTo.toString() + "\"",
							makeRelativeTo.toString(), image, context));
				} catch (SWTException e){
					//we don't others file except images.
				}
			}
		}
	}

	public void completeImportURI_ImportURI(EObject model,
			Assignment assignment, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {

		final String EMF_PLUGIN_ID = "org.eclipse.emf.ecore.edit";
		final String SCHEMAT_PATH = "icons/full/obj16/EPackage.gif";

		final Bundle bundle = Platform.getBundle(EMF_PLUGIN_ID);
		final URL url = FileLocator.find(bundle, new Path(SCHEMAT_PATH),
				Collections.EMPTY_MAP);

		Image image = ImageDescriptor.createFromURL(url).createImage();

//		Set<String> nsUris = NsUriFinder.getNsUriFromEPackageRegistry();
		Set<String> nsUris = NsUriFinder.getViewpointEPackagesNSURI(model);

		for (String uri : nsUris) {
			StyledString styledUri = new StyledString();
			styledUri.append(uri);
			if (uri.startsWith("\"") && uri.contains(context.getPrefix().substring(1)))
				acceptor.accept(createCompletionProposal(createProposal(uri),
					styledUri, image, context));
			else
				acceptor.accept(createCompletionProposal(createProposal(uri),
						styledUri, image, context));
		}

	}

	private String createProposal(String uri) {
		StringBuffer tmp = new StringBuffer();
		tmp.append("\"").append(uri).append("\"");
		return tmp.toString();
	}
	
	
	
	private List<IResource> getImportedResources(EObject model){
		Resource standalone = ResourceHelper.loadStandaloneResource(model.eResource().getURI().segment(1));
		ViewpointResources vr = getViewpointResource(standalone);
		
		List<IResource> importedResources = new ArrayList<IResource>();
		
		if (vr != null){
			EList<AbstractResource> ar = vr.getUseResource();
			
			for (AbstractResource abstractResource : ar) {
				if (abstractResource instanceof FileSystemResource){
					FileSystemResource fsr = (FileSystemResource)abstractResource;
					
					if (fsr.isWorkspace()){
						String path_str = fsr.getPath();
						IPath path = new Path(path_str);
						IResource r = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
						
						if (r != null){
							importedResources.add(r);
						}
					}
				}
			}
		}
		
		return importedResources;
		
	}

	private ViewpointResources getViewpointResource(Resource standalone) {
		
		if (standalone == null)
			return null;
		
		TreeIterator<EObject> it = standalone.getAllContents();
		
		while (it.hasNext()){
			EObject next = it.next();
			
			if (next instanceof ViewpointResources)
				return (ViewpointResources)next;
		}
		
		return null;
	}
}
