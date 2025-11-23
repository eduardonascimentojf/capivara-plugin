/*
* 
*/
package gfc.diagram.part;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tooling.runtime.structure.DiagramStructure;

import gfc.Flowchart;
import gfc.GfcPackage;
import gfc.diagram.edit.parts.DecisionNodeEditPart;
import gfc.diagram.edit.parts.DecisionNodeIdEditPart;
import gfc.diagram.edit.parts.EdgeEditPart;
import gfc.diagram.edit.parts.EntryNodeEditPart;
import gfc.diagram.edit.parts.EntryNodeIdEditPart;
import gfc.diagram.edit.parts.ExitNodeEditPart;
import gfc.diagram.edit.parts.ExitNodeIdEditPart;
import gfc.diagram.edit.parts.FlowchartEditPart;
import gfc.diagram.edit.parts.LoopDecisionNodeEditPart;
import gfc.diagram.edit.parts.LoopDecisionNodeIdEditPart;
import gfc.diagram.edit.parts.ProcessingNodeEditPart;
import gfc.diagram.edit.parts.ProcessingNodeIdEditPart;

/**
 * This registry is used to determine which type of visual object should be
 * created for the corresponding Diagram, Node, ChildNode or Link represented
 * by a domain model object.
 * 
 * @generated
 */
public class GfcVisualIDRegistry {

	/**
	* @generated
	*/
	private static final String DEBUG_KEY = "br.ufjf.capivara.diagram/debug/visualID"; //$NON-NLS-1$

	/**
	* @generated
	*/
	public static int getVisualID(View view) {
		if (view instanceof Diagram) {
			if (FlowchartEditPart.MODEL_ID.equals(view.getType())) {
				return FlowchartEditPart.VISUAL_ID;
			} else {
				return -1;
			}
		}
		return gfc.diagram.part.GfcVisualIDRegistry.getVisualID(view.getType());
	}

	/**
	* @generated
	*/
	public static String getModelID(View view) {
		View diagram = view.getDiagram();
		while (view != diagram) {
			EAnnotation annotation = view.getEAnnotation("Shortcut"); //$NON-NLS-1$
			if (annotation != null) {
				return (String) annotation.getDetails().get("modelID"); //$NON-NLS-1$
			}
			view = (View) view.eContainer();
		}
		return diagram != null ? diagram.getType() : null;
	}

	/**
	* @generated
	*/
	public static int getVisualID(String type) {
		try {
			return Integer.parseInt(type);
		} catch (NumberFormatException e) {
			if (Boolean.TRUE.toString().equalsIgnoreCase(Platform.getDebugOption(DEBUG_KEY))) {
				GfcDiagramEditorPlugin.getInstance()
						.logError("Unable to parse view type as a visualID number: " + type);
			}
		}
		return -1;
	}

	/**
	* @generated
	*/
	public static String getType(int visualID) {
		return Integer.toString(visualID);
	}

	/**
	* @generated
	*/
	public static int getDiagramVisualID(EObject domainElement) {
		if (domainElement == null) {
			return -1;
		}
		if (GfcPackage.eINSTANCE.getFlowchart().isSuperTypeOf(domainElement.eClass())
				&& isDiagram((Flowchart) domainElement)) {
			return FlowchartEditPart.VISUAL_ID;
		}
		return -1;
	}

	/**
	* @generated
	*/
	public static int getNodeVisualID(View containerView, EObject domainElement) {
		if (domainElement == null) {
			return -1;
		}
		String containerModelID = gfc.diagram.part.GfcVisualIDRegistry.getModelID(containerView);
		if (!FlowchartEditPart.MODEL_ID.equals(containerModelID) && !"gfc".equals(containerModelID)) { //$NON-NLS-1$
			return -1;
		}
		int containerVisualID;
		if (FlowchartEditPart.MODEL_ID.equals(containerModelID)) {
			containerVisualID = gfc.diagram.part.GfcVisualIDRegistry.getVisualID(containerView);
		} else {
			if (containerView instanceof Diagram) {
				containerVisualID = FlowchartEditPart.VISUAL_ID;
			} else {
				return -1;
			}
		}
		switch (containerVisualID) {
		case FlowchartEditPart.VISUAL_ID:
			if (GfcPackage.eINSTANCE.getEntryNode().isSuperTypeOf(domainElement.eClass())) {
				return EntryNodeEditPart.VISUAL_ID;
			}
			if (GfcPackage.eINSTANCE.getProcessingNode().isSuperTypeOf(domainElement.eClass())) {
				return ProcessingNodeEditPart.VISUAL_ID;
			}
			if (GfcPackage.eINSTANCE.getDecisionNode().isSuperTypeOf(domainElement.eClass())) {
				return DecisionNodeEditPart.VISUAL_ID;
			}
			if (GfcPackage.eINSTANCE.getLoopDecisionNode().isSuperTypeOf(domainElement.eClass())) {
				return LoopDecisionNodeEditPart.VISUAL_ID;
			}
			if (GfcPackage.eINSTANCE.getExitNode().isSuperTypeOf(domainElement.eClass())) {
				return ExitNodeEditPart.VISUAL_ID;
			}
			break;
		}
		return -1;
	}

	/**
	* @generated
	*/
	public static boolean canCreateNode(View containerView, int nodeVisualID) {
		String containerModelID = gfc.diagram.part.GfcVisualIDRegistry.getModelID(containerView);
		if (!FlowchartEditPart.MODEL_ID.equals(containerModelID) && !"gfc".equals(containerModelID)) { //$NON-NLS-1$
			return false;
		}
		int containerVisualID;
		if (FlowchartEditPart.MODEL_ID.equals(containerModelID)) {
			containerVisualID = gfc.diagram.part.GfcVisualIDRegistry.getVisualID(containerView);
		} else {
			if (containerView instanceof Diagram) {
				containerVisualID = FlowchartEditPart.VISUAL_ID;
			} else {
				return false;
			}
		}
		switch (containerVisualID) {
		case FlowchartEditPart.VISUAL_ID:
			if (EntryNodeEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (ProcessingNodeEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (DecisionNodeEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (LoopDecisionNodeEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (ExitNodeEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case EntryNodeEditPart.VISUAL_ID:
			if (EntryNodeIdEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case ProcessingNodeEditPart.VISUAL_ID:
			if (ProcessingNodeIdEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case DecisionNodeEditPart.VISUAL_ID:
			if (DecisionNodeIdEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case LoopDecisionNodeEditPart.VISUAL_ID:
			if (LoopDecisionNodeIdEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case ExitNodeEditPart.VISUAL_ID:
			if (ExitNodeIdEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		}
		return false;
	}

	/**
	* @generated
	*/
	public static int getLinkWithClassVisualID(EObject domainElement) {
		if (domainElement == null) {
			return -1;
		}
		if (GfcPackage.eINSTANCE.getEdge().isSuperTypeOf(domainElement.eClass())) {
			return EdgeEditPart.VISUAL_ID;
		}
		return -1;
	}

	/**
	* User can change implementation of this method to handle some specific
	* situations not covered by default logic.
	* 
	* @generated
	*/
	private static boolean isDiagram(Flowchart element) {
		return true;
	}

	/**
	* @generated
	*/
	public static boolean checkNodeVisualID(View containerView, EObject domainElement, int candidate) {
		if (candidate == -1) {
			//unrecognized id is always bad
			return false;
		}
		int basic = getNodeVisualID(containerView, domainElement);
		return basic == candidate;
	}

	/**
	* @generated
	*/
	public static boolean isCompartmentVisualID(int visualID) {
		return false;
	}

	/**
	* @generated
	*/
	public static boolean isSemanticLeafVisualID(int visualID) {
		switch (visualID) {
		case FlowchartEditPart.VISUAL_ID:
			return false;
		case EntryNodeEditPart.VISUAL_ID:
		case ProcessingNodeEditPart.VISUAL_ID:
		case DecisionNodeEditPart.VISUAL_ID:
		case LoopDecisionNodeEditPart.VISUAL_ID:
		case ExitNodeEditPart.VISUAL_ID:
			return true;
		default:
			break;
		}
		return false;
	}

	/**
	* @generated
	*/
	public static final DiagramStructure TYPED_INSTANCE = new DiagramStructure() {
		/**
		* @generated
		*/
		@Override

		public int getVisualID(View view) {
			return gfc.diagram.part.GfcVisualIDRegistry.getVisualID(view);
		}

		/**
		* @generated
		*/
		@Override

		public String getModelID(View view) {
			return gfc.diagram.part.GfcVisualIDRegistry.getModelID(view);
		}

		/**
		* @generated
		*/
		@Override

		public int getNodeVisualID(View containerView, EObject domainElement) {
			return gfc.diagram.part.GfcVisualIDRegistry.getNodeVisualID(containerView, domainElement);
		}

		/**
		* @generated
		*/
		@Override

		public boolean checkNodeVisualID(View containerView, EObject domainElement, int candidate) {
			return gfc.diagram.part.GfcVisualIDRegistry.checkNodeVisualID(containerView, domainElement, candidate);
		}

		/**
		* @generated
		*/
		@Override

		public boolean isCompartmentVisualID(int visualID) {
			return gfc.diagram.part.GfcVisualIDRegistry.isCompartmentVisualID(visualID);
		}

		/**
		* @generated
		*/
		@Override

		public boolean isSemanticLeafVisualID(int visualID) {
			return gfc.diagram.part.GfcVisualIDRegistry.isSemanticLeafVisualID(visualID);
		}
	};

}
