/*
* 
*/
package gfc.diagram.part;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tooling.runtime.update.DiagramUpdater;

import gfc.DecisionNode;
import gfc.Edge;
import gfc.EntryNode;
import gfc.ExitNode;
import gfc.Flowchart;
import gfc.GfcPackage;
import gfc.LoopDecisionNode;
import gfc.Node;
import gfc.ProcessingNode;
import gfc.diagram.edit.parts.DecisionNodeEditPart;
import gfc.diagram.edit.parts.EdgeEditPart;
import gfc.diagram.edit.parts.EntryNodeEditPart;
import gfc.diagram.edit.parts.ExitNodeEditPart;
import gfc.diagram.edit.parts.FlowchartEditPart;
import gfc.diagram.edit.parts.LoopDecisionNodeEditPart;
import gfc.diagram.edit.parts.ProcessingNodeEditPart;
import gfc.diagram.providers.GfcElementTypes;

/**
 * @generated
 */
public class GfcDiagramUpdater {

	/**
	* @generated
	*/
	public static boolean isShortcutOrphaned(View view) {
		return !view.isSetElement() || view.getElement() == null || view.getElement().eIsProxy();
	}

	/**
	* @generated
	*/
	public static List<GfcNodeDescriptor> getSemanticChildren(View view) {
		switch (GfcVisualIDRegistry.getVisualID(view)) {
		case FlowchartEditPart.VISUAL_ID:
			return getFlowchart_1000SemanticChildren(view);
		}
		return Collections.emptyList();
	}

	/**
	* @generated
	*/
	public static List<GfcNodeDescriptor> getFlowchart_1000SemanticChildren(View view) {
		if (!view.isSetElement()) {
			return Collections.emptyList();
		}
		Flowchart modelElement = (Flowchart) view.getElement();
		LinkedList<GfcNodeDescriptor> result = new LinkedList<GfcNodeDescriptor>();
		for (Iterator<?> it = modelElement.getNodes().iterator(); it.hasNext();) {
			Node childElement = (Node) it.next();
			int visualID = GfcVisualIDRegistry.getNodeVisualID(view, childElement);
			if (visualID == EntryNodeEditPart.VISUAL_ID) {
				result.add(new GfcNodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == ProcessingNodeEditPart.VISUAL_ID) {
				result.add(new GfcNodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == DecisionNodeEditPart.VISUAL_ID) {
				result.add(new GfcNodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == LoopDecisionNodeEditPart.VISUAL_ID) {
				result.add(new GfcNodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == ExitNodeEditPart.VISUAL_ID) {
				result.add(new GfcNodeDescriptor(childElement, visualID));
				continue;
			}
		}
		return result;
	}

	/**
	* @generated
	*/
	public static List<GfcLinkDescriptor> getContainedLinks(View view) {
		switch (GfcVisualIDRegistry.getVisualID(view)) {
		case FlowchartEditPart.VISUAL_ID:
			return getFlowchart_1000ContainedLinks(view);
		case EntryNodeEditPart.VISUAL_ID:
			return getEntryNode_2001ContainedLinks(view);
		case ProcessingNodeEditPart.VISUAL_ID:
			return getProcessingNode_2002ContainedLinks(view);
		case DecisionNodeEditPart.VISUAL_ID:
			return getDecisionNode_2003ContainedLinks(view);
		case LoopDecisionNodeEditPart.VISUAL_ID:
			return getLoopDecisionNode_2004ContainedLinks(view);
		case ExitNodeEditPart.VISUAL_ID:
			return getExitNode_2005ContainedLinks(view);
		case EdgeEditPart.VISUAL_ID:
			return getEdge_4001ContainedLinks(view);
		}
		return Collections.emptyList();
	}

	/**
	* @generated
	*/
	public static List<GfcLinkDescriptor> getIncomingLinks(View view) {
		switch (GfcVisualIDRegistry.getVisualID(view)) {
		case EntryNodeEditPart.VISUAL_ID:
			return getEntryNode_2001IncomingLinks(view);
		case ProcessingNodeEditPart.VISUAL_ID:
			return getProcessingNode_2002IncomingLinks(view);
		case DecisionNodeEditPart.VISUAL_ID:
			return getDecisionNode_2003IncomingLinks(view);
		case LoopDecisionNodeEditPart.VISUAL_ID:
			return getLoopDecisionNode_2004IncomingLinks(view);
		case ExitNodeEditPart.VISUAL_ID:
			return getExitNode_2005IncomingLinks(view);
		case EdgeEditPart.VISUAL_ID:
			return getEdge_4001IncomingLinks(view);
		}
		return Collections.emptyList();
	}

	/**
	* @generated
	*/
	public static List<GfcLinkDescriptor> getOutgoingLinks(View view) {
		switch (GfcVisualIDRegistry.getVisualID(view)) {
		case EntryNodeEditPart.VISUAL_ID:
			return getEntryNode_2001OutgoingLinks(view);
		case ProcessingNodeEditPart.VISUAL_ID:
			return getProcessingNode_2002OutgoingLinks(view);
		case DecisionNodeEditPart.VISUAL_ID:
			return getDecisionNode_2003OutgoingLinks(view);
		case LoopDecisionNodeEditPart.VISUAL_ID:
			return getLoopDecisionNode_2004OutgoingLinks(view);
		case ExitNodeEditPart.VISUAL_ID:
			return getExitNode_2005OutgoingLinks(view);
		case EdgeEditPart.VISUAL_ID:
			return getEdge_4001OutgoingLinks(view);
		}
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<GfcLinkDescriptor> getFlowchart_1000ContainedLinks(View view) {
		Flowchart modelElement = (Flowchart) view.getElement();
		LinkedList<GfcLinkDescriptor> result = new LinkedList<GfcLinkDescriptor>();
		result.addAll(getContainedTypeModelFacetLinks_Edge_4001(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<GfcLinkDescriptor> getEntryNode_2001ContainedLinks(View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<GfcLinkDescriptor> getProcessingNode_2002ContainedLinks(View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<GfcLinkDescriptor> getDecisionNode_2003ContainedLinks(View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<GfcLinkDescriptor> getLoopDecisionNode_2004ContainedLinks(View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<GfcLinkDescriptor> getExitNode_2005ContainedLinks(View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<GfcLinkDescriptor> getEdge_4001ContainedLinks(View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<GfcLinkDescriptor> getEntryNode_2001IncomingLinks(View view) {
		EntryNode modelElement = (EntryNode) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<GfcLinkDescriptor> result = new LinkedList<GfcLinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_Edge_4001(modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<GfcLinkDescriptor> getProcessingNode_2002IncomingLinks(View view) {
		ProcessingNode modelElement = (ProcessingNode) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<GfcLinkDescriptor> result = new LinkedList<GfcLinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_Edge_4001(modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<GfcLinkDescriptor> getDecisionNode_2003IncomingLinks(View view) {
		DecisionNode modelElement = (DecisionNode) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<GfcLinkDescriptor> result = new LinkedList<GfcLinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_Edge_4001(modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<GfcLinkDescriptor> getLoopDecisionNode_2004IncomingLinks(View view) {
		LoopDecisionNode modelElement = (LoopDecisionNode) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<GfcLinkDescriptor> result = new LinkedList<GfcLinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_Edge_4001(modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<GfcLinkDescriptor> getExitNode_2005IncomingLinks(View view) {
		ExitNode modelElement = (ExitNode) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<GfcLinkDescriptor> result = new LinkedList<GfcLinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_Edge_4001(modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<GfcLinkDescriptor> getEdge_4001IncomingLinks(View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<GfcLinkDescriptor> getEntryNode_2001OutgoingLinks(View view) {
		EntryNode modelElement = (EntryNode) view.getElement();
		LinkedList<GfcLinkDescriptor> result = new LinkedList<GfcLinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_Edge_4001(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<GfcLinkDescriptor> getProcessingNode_2002OutgoingLinks(View view) {
		ProcessingNode modelElement = (ProcessingNode) view.getElement();
		LinkedList<GfcLinkDescriptor> result = new LinkedList<GfcLinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_Edge_4001(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<GfcLinkDescriptor> getDecisionNode_2003OutgoingLinks(View view) {
		DecisionNode modelElement = (DecisionNode) view.getElement();
		LinkedList<GfcLinkDescriptor> result = new LinkedList<GfcLinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_Edge_4001(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<GfcLinkDescriptor> getLoopDecisionNode_2004OutgoingLinks(View view) {
		LoopDecisionNode modelElement = (LoopDecisionNode) view.getElement();
		LinkedList<GfcLinkDescriptor> result = new LinkedList<GfcLinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_Edge_4001(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<GfcLinkDescriptor> getExitNode_2005OutgoingLinks(View view) {
		ExitNode modelElement = (ExitNode) view.getElement();
		LinkedList<GfcLinkDescriptor> result = new LinkedList<GfcLinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_Edge_4001(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<GfcLinkDescriptor> getEdge_4001OutgoingLinks(View view) {
		return Collections.emptyList();
	}

	/**
	* @generated
	*/
	private static Collection<GfcLinkDescriptor> getContainedTypeModelFacetLinks_Edge_4001(Flowchart container) {
		LinkedList<GfcLinkDescriptor> result = new LinkedList<GfcLinkDescriptor>();
		for (Iterator<?> links = container.getEdges().iterator(); links.hasNext();) {
			EObject linkObject = (EObject) links.next();
			if (false == linkObject instanceof Edge) {
				continue;
			}
			Edge link = (Edge) linkObject;
			if (EdgeEditPart.VISUAL_ID != GfcVisualIDRegistry.getLinkWithClassVisualID(link)) {
				continue;
			}
			Node dst = link.getTarget();
			Node src = link.getSource();
			result.add(new GfcLinkDescriptor(src, dst, link, GfcElementTypes.Edge_4001, EdgeEditPart.VISUAL_ID));
		}
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection<GfcLinkDescriptor> getIncomingTypeModelFacetLinks_Edge_4001(Node target,
			Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences) {
		LinkedList<GfcLinkDescriptor> result = new LinkedList<GfcLinkDescriptor>();
		Collection<EStructuralFeature.Setting> settings = crossReferences.get(target);
		for (EStructuralFeature.Setting setting : settings) {
			if (setting.getEStructuralFeature() != GfcPackage.eINSTANCE.getEdge_Target()
					|| false == setting.getEObject() instanceof Edge) {
				continue;
			}
			Edge link = (Edge) setting.getEObject();
			if (EdgeEditPart.VISUAL_ID != GfcVisualIDRegistry.getLinkWithClassVisualID(link)) {
				continue;
			}
			Node src = link.getSource();
			result.add(new GfcLinkDescriptor(src, target, link, GfcElementTypes.Edge_4001, EdgeEditPart.VISUAL_ID));
		}
		return result;
	}

	/**
	* @generated
	*/
	private static Collection<GfcLinkDescriptor> getOutgoingTypeModelFacetLinks_Edge_4001(Node source) {
		Flowchart container = null;
		// Find container element for the link.
		// Climb up by containment hierarchy starting from the source
		// and return the first element that is instance of the container class.
		for (EObject element = source; element != null && container == null; element = element.eContainer()) {
			if (element instanceof Flowchart) {
				container = (Flowchart) element;
			}
		}
		if (container == null) {
			return Collections.emptyList();
		}
		LinkedList<GfcLinkDescriptor> result = new LinkedList<GfcLinkDescriptor>();
		for (Iterator<?> links = container.getEdges().iterator(); links.hasNext();) {
			EObject linkObject = (EObject) links.next();
			if (false == linkObject instanceof Edge) {
				continue;
			}
			Edge link = (Edge) linkObject;
			if (EdgeEditPart.VISUAL_ID != GfcVisualIDRegistry.getLinkWithClassVisualID(link)) {
				continue;
			}
			Node dst = link.getTarget();
			Node src = link.getSource();
			if (src != source) {
				continue;
			}
			result.add(new GfcLinkDescriptor(src, dst, link, GfcElementTypes.Edge_4001, EdgeEditPart.VISUAL_ID));
		}
		return result;
	}

	/**
	* @generated
	*/
	public static final DiagramUpdater TYPED_INSTANCE = new DiagramUpdater() {
		/**
		* @generated
		*/
		@Override

		public List<GfcNodeDescriptor> getSemanticChildren(View view) {
			return GfcDiagramUpdater.getSemanticChildren(view);
		}

		/**
		* @generated
		*/
		@Override

		public List<GfcLinkDescriptor> getContainedLinks(View view) {
			return GfcDiagramUpdater.getContainedLinks(view);
		}

		/**
		* @generated
		*/
		@Override

		public List<GfcLinkDescriptor> getIncomingLinks(View view) {
			return GfcDiagramUpdater.getIncomingLinks(view);
		}

		/**
		* @generated
		*/
		@Override

		public List<GfcLinkDescriptor> getOutgoingLinks(View view) {
			return GfcDiagramUpdater.getOutgoingLinks(view);
		}
	};

}
