/*
* 
*/
package gfc.diagram.navigator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gmf.runtime.emf.core.GMFEditingDomainFactory;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonContentProvider;

import gfc.diagram.edit.parts.CaseNodeEditPart;
import gfc.diagram.edit.parts.DecisionNodeEditPart;
import gfc.diagram.edit.parts.EdgeEditPart;
import gfc.diagram.edit.parts.EntryNodeEditPart;
import gfc.diagram.edit.parts.ExitNodeEditPart;
import gfc.diagram.edit.parts.FlowchartEditPart;
import gfc.diagram.edit.parts.LoopDecisionNodeEditPart;
import gfc.diagram.edit.parts.ProcessingNodeEditPart;
import gfc.diagram.edit.parts.SwitchNodeEditPart;
import gfc.diagram.part.GfcVisualIDRegistry;
import gfc.diagram.part.Messages;

/**
 * @generated
 */
public class GfcNavigatorContentProvider implements ICommonContentProvider {

	/**
	* @generated
	*/
	private static final Object[] EMPTY_ARRAY = new Object[0];

	/**
	* @generated
	*/
	private Viewer myViewer;

	/**
	* @generated
	*/
	private AdapterFactoryEditingDomain myEditingDomain;

	/**
	* @generated
	*/
	private WorkspaceSynchronizer myWorkspaceSynchronizer;

	/**
	* @generated
	*/
	private Runnable myViewerRefreshRunnable;

	/**
	* @generated
	*/
	@SuppressWarnings({ "unchecked", "serial", "rawtypes" })
	public GfcNavigatorContentProvider() {
		TransactionalEditingDomain editingDomain = GMFEditingDomainFactory.INSTANCE.createEditingDomain();
		myEditingDomain = (AdapterFactoryEditingDomain) editingDomain;
		myEditingDomain.setResourceToReadOnlyMap(new HashMap() {
			public Object get(Object key) {
				if (!containsKey(key)) {
					put(key, Boolean.TRUE);
				}
				return super.get(key);
			}
		});
		myViewerRefreshRunnable = new Runnable() {
			public void run() {
				if (myViewer != null) {
					myViewer.refresh();
				}
			}
		};
		myWorkspaceSynchronizer = new WorkspaceSynchronizer(editingDomain, new WorkspaceSynchronizer.Delegate() {
			public void dispose() {
			}

			public boolean handleResourceChanged(final Resource resource) {
				unloadAllResources();
				asyncRefresh();
				return true;
			}

			public boolean handleResourceDeleted(Resource resource) {
				unloadAllResources();
				asyncRefresh();
				return true;
			}

			public boolean handleResourceMoved(Resource resource, final URI newURI) {
				unloadAllResources();
				asyncRefresh();
				return true;
			}
		});
	}

	/**
	* @generated
	*/
	public void dispose() {
		myWorkspaceSynchronizer.dispose();
		myWorkspaceSynchronizer = null;
		myViewerRefreshRunnable = null;
		myViewer = null;
		unloadAllResources();
		((TransactionalEditingDomain) myEditingDomain).dispose();
		myEditingDomain = null;
	}

	/**
	* @generated
	*/
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		myViewer = viewer;
	}

	/**
	* @generated
	*/
	void unloadAllResources() {
		for (Resource nextResource : myEditingDomain.getResourceSet().getResources()) {
			nextResource.unload();
		}
	}

	/**
	* @generated
	*/
	void asyncRefresh() {
		if (myViewer != null && !myViewer.getControl().isDisposed()) {
			myViewer.getControl().getDisplay().asyncExec(myViewerRefreshRunnable);
		}
	}

	/**
	* @generated
	*/
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	/**
	* @generated
	*/
	public void restoreState(IMemento aMemento) {
	}

	/**
	 * @generated
	 */
	public void saveState(IMemento aMemento) {
	}

	/**
	* @generated
	*/
	public void init(ICommonContentExtensionSite aConfig) {
	}

	/**
	* @generated
	*/
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IFile) {
			IFile file = (IFile) parentElement;
			URI fileURI = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
			Resource resource = myEditingDomain.getResourceSet().getResource(fileURI, true);
			ArrayList<GfcNavigatorItem> result = new ArrayList<GfcNavigatorItem>();
			ArrayList<View> topViews = new ArrayList<View>(resource.getContents().size());
			for (EObject o : resource.getContents()) {
				if (o instanceof View) {
					topViews.add((View) o);
				}
			}
			result.addAll(createNavigatorItems(selectViewsByType(topViews, FlowchartEditPart.MODEL_ID), file, false));
			return result.toArray();
		}

		if (parentElement instanceof GfcNavigatorGroup) {
			GfcNavigatorGroup group = (GfcNavigatorGroup) parentElement;
			return group.getChildren();
		}

		if (parentElement instanceof GfcNavigatorItem) {
			GfcNavigatorItem navigatorItem = (GfcNavigatorItem) parentElement;
			if (navigatorItem.isLeaf() || !isOwnView(navigatorItem.getView())) {
				return EMPTY_ARRAY;
			}
			return getViewChildren(navigatorItem.getView(), parentElement);
		}

		/*
		* Due to plugin.xml restrictions this code will be called only for views representing
		* shortcuts to this diagram elements created on other diagrams. 
		*/
		if (parentElement instanceof IAdaptable) {
			View view = (View) ((IAdaptable) parentElement).getAdapter(View.class);
			if (view != null) {
				return getViewChildren(view, parentElement);
			}
		}

		return EMPTY_ARRAY;
	}

	/**
	* @generated
	*/
	private Object[] getViewChildren(View view, Object parentElement) {
		switch (GfcVisualIDRegistry.getVisualID(view)) {

		case FlowchartEditPart.VISUAL_ID: {
			LinkedList<GfcAbstractNavigatorItem> result = new LinkedList<GfcAbstractNavigatorItem>();
			result.addAll(getForeignShortcuts((Diagram) view, parentElement));
			Diagram sv = (Diagram) view;
			GfcNavigatorGroup links = new GfcNavigatorGroup(Messages.NavigatorGroupName_Flowchart_1000_links,
					"icons/linksNavigatorGroup.gif", parentElement); 
			Collection<View> connectedViews;
			connectedViews = getChildrenByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(EntryNodeEditPart.VISUAL_ID));
			result.addAll(createNavigatorItems(connectedViews, parentElement, false));
			connectedViews = getChildrenByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(ProcessingNodeEditPart.VISUAL_ID));
			result.addAll(createNavigatorItems(connectedViews, parentElement, false));
			connectedViews = getChildrenByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(DecisionNodeEditPart.VISUAL_ID));
			result.addAll(createNavigatorItems(connectedViews, parentElement, false));
			connectedViews = getChildrenByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(LoopDecisionNodeEditPart.VISUAL_ID));
			result.addAll(createNavigatorItems(connectedViews, parentElement, false));
			connectedViews = getChildrenByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(ExitNodeEditPart.VISUAL_ID));
			result.addAll(createNavigatorItems(connectedViews, parentElement, false));
			connectedViews = getChildrenByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(SwitchNodeEditPart.VISUAL_ID));
			result.addAll(createNavigatorItems(connectedViews, parentElement, false));
			connectedViews = getChildrenByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(CaseNodeEditPart.VISUAL_ID));
			result.addAll(createNavigatorItems(connectedViews, parentElement, false));
			connectedViews = getDiagramLinksByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(EdgeEditPart.VISUAL_ID));
			links.addChildren(createNavigatorItems(connectedViews, links, false));
			if (!links.isEmpty()) {
				result.add(links);
			}
			return result.toArray();
		}

		case EntryNodeEditPart.VISUAL_ID: {
			LinkedList<GfcAbstractNavigatorItem> result = new LinkedList<GfcAbstractNavigatorItem>();
			Node sv = (Node) view;
			GfcNavigatorGroup incominglinks = new GfcNavigatorGroup(
					Messages.NavigatorGroupName_EntryNode_2001_incominglinks, "icons/incomingLinksNavigatorGroup.gif", 
					parentElement);
			GfcNavigatorGroup outgoinglinks = new GfcNavigatorGroup(
					Messages.NavigatorGroupName_EntryNode_2001_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", 
					parentElement);
			Collection<View> connectedViews;
			connectedViews = getIncomingLinksByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(EdgeEditPart.VISUAL_ID));
			incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
			connectedViews = getOutgoingLinksByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(EdgeEditPart.VISUAL_ID));
			outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
			if (!incominglinks.isEmpty()) {
				result.add(incominglinks);
			}
			if (!outgoinglinks.isEmpty()) {
				result.add(outgoinglinks);
			}
			return result.toArray();
		}

		case ProcessingNodeEditPart.VISUAL_ID: {
			LinkedList<GfcAbstractNavigatorItem> result = new LinkedList<GfcAbstractNavigatorItem>();
			Node sv = (Node) view;
			GfcNavigatorGroup incominglinks = new GfcNavigatorGroup(
					Messages.NavigatorGroupName_ProcessingNode_2002_incominglinks,
					"icons/incomingLinksNavigatorGroup.gif", parentElement); 
			GfcNavigatorGroup outgoinglinks = new GfcNavigatorGroup(
					Messages.NavigatorGroupName_ProcessingNode_2002_outgoinglinks,
					"icons/outgoingLinksNavigatorGroup.gif", parentElement); 
			Collection<View> connectedViews;
			connectedViews = getIncomingLinksByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(EdgeEditPart.VISUAL_ID));
			incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
			connectedViews = getOutgoingLinksByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(EdgeEditPart.VISUAL_ID));
			outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
			if (!incominglinks.isEmpty()) {
				result.add(incominglinks);
			}
			if (!outgoinglinks.isEmpty()) {
				result.add(outgoinglinks);
			}
			return result.toArray();
		}

		case DecisionNodeEditPart.VISUAL_ID: {
			LinkedList<GfcAbstractNavigatorItem> result = new LinkedList<GfcAbstractNavigatorItem>();
			Node sv = (Node) view;
			GfcNavigatorGroup incominglinks = new GfcNavigatorGroup(
					Messages.NavigatorGroupName_DecisionNode_2003_incominglinks,
					"icons/incomingLinksNavigatorGroup.gif", parentElement); 
			GfcNavigatorGroup outgoinglinks = new GfcNavigatorGroup(
					Messages.NavigatorGroupName_DecisionNode_2003_outgoinglinks,
					"icons/outgoingLinksNavigatorGroup.gif", parentElement); 
			Collection<View> connectedViews;
			connectedViews = getIncomingLinksByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(EdgeEditPart.VISUAL_ID));
			incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
			connectedViews = getOutgoingLinksByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(EdgeEditPart.VISUAL_ID));
			outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
			if (!incominglinks.isEmpty()) {
				result.add(incominglinks);
			}
			if (!outgoinglinks.isEmpty()) {
				result.add(outgoinglinks);
			}
			return result.toArray();
		}

		case LoopDecisionNodeEditPart.VISUAL_ID: {
			LinkedList<GfcAbstractNavigatorItem> result = new LinkedList<GfcAbstractNavigatorItem>();
			Node sv = (Node) view;
			GfcNavigatorGroup incominglinks = new GfcNavigatorGroup(
					Messages.NavigatorGroupName_LoopDecisionNode_2004_incominglinks,
					"icons/incomingLinksNavigatorGroup.gif", parentElement); 
			GfcNavigatorGroup outgoinglinks = new GfcNavigatorGroup(
					Messages.NavigatorGroupName_LoopDecisionNode_2004_outgoinglinks,
					"icons/outgoingLinksNavigatorGroup.gif", parentElement); 
			Collection<View> connectedViews;
			connectedViews = getIncomingLinksByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(EdgeEditPart.VISUAL_ID));
			incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
			connectedViews = getOutgoingLinksByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(EdgeEditPart.VISUAL_ID));
			outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
			if (!incominglinks.isEmpty()) {
				result.add(incominglinks);
			}
			if (!outgoinglinks.isEmpty()) {
				result.add(outgoinglinks);
			}
			return result.toArray();
		}

		case ExitNodeEditPart.VISUAL_ID: {
			LinkedList<GfcAbstractNavigatorItem> result = new LinkedList<GfcAbstractNavigatorItem>();
			Node sv = (Node) view;
			GfcNavigatorGroup incominglinks = new GfcNavigatorGroup(
					Messages.NavigatorGroupName_ExitNode_2005_incominglinks, "icons/incomingLinksNavigatorGroup.gif", 
					parentElement);
			GfcNavigatorGroup outgoinglinks = new GfcNavigatorGroup(
					Messages.NavigatorGroupName_ExitNode_2005_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", 
					parentElement);
			Collection<View> connectedViews;
			connectedViews = getIncomingLinksByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(EdgeEditPart.VISUAL_ID));
			incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
			connectedViews = getOutgoingLinksByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(EdgeEditPart.VISUAL_ID));
			outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
			if (!incominglinks.isEmpty()) {
				result.add(incominglinks);
			}
			if (!outgoinglinks.isEmpty()) {
				result.add(outgoinglinks);
			}
			return result.toArray();
		}

		case SwitchNodeEditPart.VISUAL_ID: {
			LinkedList<GfcAbstractNavigatorItem> result = new LinkedList<GfcAbstractNavigatorItem>();
			Node sv = (Node) view;
			GfcNavigatorGroup incominglinks = new GfcNavigatorGroup(
					Messages.NavigatorGroupName_SwitchNode_2006_incominglinks, "icons/incomingLinksNavigatorGroup.gif", 
					parentElement);
			GfcNavigatorGroup outgoinglinks = new GfcNavigatorGroup(
					Messages.NavigatorGroupName_SwitchNode_2006_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", 
					parentElement);
			Collection<View> connectedViews;
			connectedViews = getIncomingLinksByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(EdgeEditPart.VISUAL_ID));
			incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
			connectedViews = getOutgoingLinksByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(EdgeEditPart.VISUAL_ID));
			outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
			if (!incominglinks.isEmpty()) {
				result.add(incominglinks);
			}
			if (!outgoinglinks.isEmpty()) {
				result.add(outgoinglinks);
			}
			return result.toArray();
		}

		case CaseNodeEditPart.VISUAL_ID: {
			LinkedList<GfcAbstractNavigatorItem> result = new LinkedList<GfcAbstractNavigatorItem>();
			Node sv = (Node) view;
			GfcNavigatorGroup incominglinks = new GfcNavigatorGroup(
					Messages.NavigatorGroupName_CaseNode_2007_incominglinks, "icons/incomingLinksNavigatorGroup.gif", 
					parentElement);
			GfcNavigatorGroup outgoinglinks = new GfcNavigatorGroup(
					Messages.NavigatorGroupName_CaseNode_2007_outgoinglinks, "icons/outgoingLinksNavigatorGroup.gif", 
					parentElement);
			Collection<View> connectedViews;
			connectedViews = getIncomingLinksByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(EdgeEditPart.VISUAL_ID));
			incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
			connectedViews = getOutgoingLinksByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(EdgeEditPart.VISUAL_ID));
			outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
			if (!incominglinks.isEmpty()) {
				result.add(incominglinks);
			}
			if (!outgoinglinks.isEmpty()) {
				result.add(outgoinglinks);
			}
			return result.toArray();
		}

		case EdgeEditPart.VISUAL_ID: {
			LinkedList<GfcAbstractNavigatorItem> result = new LinkedList<GfcAbstractNavigatorItem>();
			Edge sv = (Edge) view;
			GfcNavigatorGroup target = new GfcNavigatorGroup(Messages.NavigatorGroupName_Edge_4001_target,
					"icons/linkTargetNavigatorGroup.gif", parentElement); 
			GfcNavigatorGroup source = new GfcNavigatorGroup(Messages.NavigatorGroupName_Edge_4001_source,
					"icons/linkSourceNavigatorGroup.gif", parentElement); 
			Collection<View> connectedViews;
			connectedViews = getLinksTargetByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(EntryNodeEditPart.VISUAL_ID));
			target.addChildren(createNavigatorItems(connectedViews, target, true));
			connectedViews = getLinksTargetByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(ProcessingNodeEditPart.VISUAL_ID));
			target.addChildren(createNavigatorItems(connectedViews, target, true));
			connectedViews = getLinksTargetByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(DecisionNodeEditPart.VISUAL_ID));
			target.addChildren(createNavigatorItems(connectedViews, target, true));
			connectedViews = getLinksTargetByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(LoopDecisionNodeEditPart.VISUAL_ID));
			target.addChildren(createNavigatorItems(connectedViews, target, true));
			connectedViews = getLinksTargetByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(ExitNodeEditPart.VISUAL_ID));
			target.addChildren(createNavigatorItems(connectedViews, target, true));
			connectedViews = getLinksTargetByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(SwitchNodeEditPart.VISUAL_ID));
			target.addChildren(createNavigatorItems(connectedViews, target, true));
			connectedViews = getLinksTargetByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(CaseNodeEditPart.VISUAL_ID));
			target.addChildren(createNavigatorItems(connectedViews, target, true));
			connectedViews = getLinksSourceByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(EntryNodeEditPart.VISUAL_ID));
			source.addChildren(createNavigatorItems(connectedViews, source, true));
			connectedViews = getLinksSourceByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(ProcessingNodeEditPart.VISUAL_ID));
			source.addChildren(createNavigatorItems(connectedViews, source, true));
			connectedViews = getLinksSourceByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(DecisionNodeEditPart.VISUAL_ID));
			source.addChildren(createNavigatorItems(connectedViews, source, true));
			connectedViews = getLinksSourceByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(LoopDecisionNodeEditPart.VISUAL_ID));
			source.addChildren(createNavigatorItems(connectedViews, source, true));
			connectedViews = getLinksSourceByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(ExitNodeEditPart.VISUAL_ID));
			source.addChildren(createNavigatorItems(connectedViews, source, true));
			connectedViews = getLinksSourceByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(SwitchNodeEditPart.VISUAL_ID));
			source.addChildren(createNavigatorItems(connectedViews, source, true));
			connectedViews = getLinksSourceByType(Collections.singleton(sv),
					GfcVisualIDRegistry.getType(CaseNodeEditPart.VISUAL_ID));
			source.addChildren(createNavigatorItems(connectedViews, source, true));
			if (!target.isEmpty()) {
				result.add(target);
			}
			if (!source.isEmpty()) {
				result.add(source);
			}
			return result.toArray();
		}
		}
		return EMPTY_ARRAY;
	}

	/**
	* @generated
	*/
	private Collection<View> getLinksSourceByType(Collection<Edge> edges, String type) {
		LinkedList<View> result = new LinkedList<View>();
		for (Edge nextEdge : edges) {
			View nextEdgeSource = nextEdge.getSource();
			if (type.equals(nextEdgeSource.getType()) && isOwnView(nextEdgeSource)) {
				result.add(nextEdgeSource);
			}
		}
		return result;
	}

	/**
	 * @generated
	 */
	private Collection<View> getLinksTargetByType(Collection<Edge> edges, String type) {
		LinkedList<View> result = new LinkedList<View>();
		for (Edge nextEdge : edges) {
			View nextEdgeTarget = nextEdge.getTarget();
			if (type.equals(nextEdgeTarget.getType()) && isOwnView(nextEdgeTarget)) {
				result.add(nextEdgeTarget);
			}
		}
		return result;
	}

	/**
	 * @generated
	 */
	private Collection<View> getOutgoingLinksByType(Collection<? extends View> nodes, String type) {
		LinkedList<View> result = new LinkedList<View>();
		for (View nextNode : nodes) {
			result.addAll(selectViewsByType(nextNode.getSourceEdges(), type));
		}
		return result;
	}

	/**
	 * @generated
	 */
	private Collection<View> getIncomingLinksByType(Collection<? extends View> nodes, String type) {
		LinkedList<View> result = new LinkedList<View>();
		for (View nextNode : nodes) {
			result.addAll(selectViewsByType(nextNode.getTargetEdges(), type));
		}
		return result;
	}

	/**
	 * @generated
	 */
	private Collection<View> getChildrenByType(Collection<? extends View> nodes, String type) {
		LinkedList<View> result = new LinkedList<View>();
		for (View nextNode : nodes) {
			result.addAll(selectViewsByType(nextNode.getChildren(), type));
		}
		return result;
	}

	/**
	 * @generated
	 */
	private Collection<View> getDiagramLinksByType(Collection<Diagram> diagrams, String type) {
		ArrayList<View> result = new ArrayList<View>();
		for (Diagram nextDiagram : diagrams) {
			result.addAll(selectViewsByType(nextDiagram.getEdges(), type));
		}
		return result;
	}

	// TODO refactor as static method
	/**
	 * @generated
	 */
	private Collection<View> selectViewsByType(Collection<View> views, String type) {
		ArrayList<View> result = new ArrayList<View>();
		for (View nextView : views) {
			if (type.equals(nextView.getType()) && isOwnView(nextView)) {
				result.add(nextView);
			}
		}
		return result;
	}

	/**
	 * @generated
	 */
	private boolean isOwnView(View view) {
		return FlowchartEditPart.MODEL_ID.equals(GfcVisualIDRegistry.getModelID(view));
	}

	/**
	 * @generated
	 */
	private Collection<GfcNavigatorItem> createNavigatorItems(Collection<View> views, Object parent, boolean isLeafs) {
		ArrayList<GfcNavigatorItem> result = new ArrayList<GfcNavigatorItem>(views.size());
		for (View nextView : views) {
			result.add(new GfcNavigatorItem(nextView, parent, isLeafs));
		}
		return result;
	}

	/**
	 * @generated
	 */
	private Collection<GfcNavigatorItem> getForeignShortcuts(Diagram diagram, Object parent) {
		LinkedList<View> result = new LinkedList<View>();
		for (Iterator<View> it = diagram.getChildren().iterator(); it.hasNext();) {
			View nextView = it.next();
			if (!isOwnView(nextView) && nextView.getEAnnotation("Shortcut") != null) { 
				result.add(nextView);
			}
		}
		return createNavigatorItems(result, parent, false);
	}

	/**
	* @generated
	*/
	public Object getParent(Object element) {
		if (element instanceof GfcAbstractNavigatorItem) {
			GfcAbstractNavigatorItem abstractNavigatorItem = (GfcAbstractNavigatorItem) element;
			return abstractNavigatorItem.getParent();
		}
		return null;
	}

	/**
	* @generated
	*/
	public boolean hasChildren(Object element) {
		return element instanceof IFile || getChildren(element).length > 0;
	}

}
