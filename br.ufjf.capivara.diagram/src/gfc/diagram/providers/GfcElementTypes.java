/*
 * 
 */
package gfc.diagram.providers;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.tooling.runtime.providers.DiagramElementTypeImages;
import org.eclipse.gmf.tooling.runtime.providers.DiagramElementTypes;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import gfc.GfcPackage;
import gfc.diagram.edit.parts.DecisionNodeEditPart;
import gfc.diagram.edit.parts.EdgeEditPart;
import gfc.diagram.edit.parts.EntryNodeEditPart;
import gfc.diagram.edit.parts.ExitNodeEditPart;
import gfc.diagram.edit.parts.FlowchartEditPart;
import gfc.diagram.edit.parts.LoopDecisionNodeEditPart;
import gfc.diagram.edit.parts.ProcessingNodeEditPart;
import gfc.diagram.part.GfcDiagramEditorPlugin;

/**
 * @generated
 */
public class GfcElementTypes {

	/**
	* @generated
	*/
	private GfcElementTypes() {
	}

	/**
	* @generated
	*/
	private static Map<IElementType, ENamedElement> elements;

	/**
	* @generated
	*/
	private static DiagramElementTypeImages elementTypeImages = new DiagramElementTypeImages(
			GfcDiagramEditorPlugin.getInstance().getItemProvidersAdapterFactory());

	/**
	* @generated
	*/
	private static Set<IElementType> KNOWN_ELEMENT_TYPES;

	/**
	* @generated
	*/
	public static final IElementType Flowchart_1000 = getElementType("br.ufjf.capivara.diagram.Flowchart_1000"); //$NON-NLS-1$
	/**
	* @generated
	*/
	public static final IElementType EntryNode_2001 = getElementType("br.ufjf.capivara.diagram.EntryNode_2001"); //$NON-NLS-1$
	/**
	* @generated
	*/
	public static final IElementType ProcessingNode_2002 = getElementType(
			"br.ufjf.capivara.diagram.ProcessingNode_2002"); //$NON-NLS-1$
	/**
	* @generated
	*/
	public static final IElementType DecisionNode_2003 = getElementType("br.ufjf.capivara.diagram.DecisionNode_2003"); //$NON-NLS-1$
	/**
	* @generated
	*/
	public static final IElementType LoopDecisionNode_2004 = getElementType(
			"br.ufjf.capivara.diagram.LoopDecisionNode_2004"); //$NON-NLS-1$
	/**
	* @generated
	*/
	public static final IElementType ExitNode_2005 = getElementType("br.ufjf.capivara.diagram.ExitNode_2005"); //$NON-NLS-1$
	/**
	* @generated
	*/
	public static final IElementType Edge_4001 = getElementType("br.ufjf.capivara.diagram.Edge_4001"); //$NON-NLS-1$

	/**
	* @generated
	*/
	public static ImageDescriptor getImageDescriptor(ENamedElement element) {
		return elementTypeImages.getImageDescriptor(element);
	}

	/**
	* @generated
	*/
	public static Image getImage(ENamedElement element) {
		return elementTypeImages.getImage(element);
	}

	/**
	* @generated
	*/
	public static ImageDescriptor getImageDescriptor(IAdaptable hint) {
		return getImageDescriptor(getElement(hint));
	}

	/**
	* @generated
	*/
	public static Image getImage(IAdaptable hint) {
		return getImage(getElement(hint));
	}

	/**
	* Returns 'type' of the ecore object associated with the hint.
	* 
	* @generated
	*/
	public static ENamedElement getElement(IAdaptable hint) {
		Object type = hint.getAdapter(IElementType.class);
		if (elements == null) {
			elements = new IdentityHashMap<IElementType, ENamedElement>();

			elements.put(Flowchart_1000, GfcPackage.eINSTANCE.getFlowchart());

			elements.put(EntryNode_2001, GfcPackage.eINSTANCE.getEntryNode());

			elements.put(ProcessingNode_2002, GfcPackage.eINSTANCE.getProcessingNode());

			elements.put(DecisionNode_2003, GfcPackage.eINSTANCE.getDecisionNode());

			elements.put(LoopDecisionNode_2004, GfcPackage.eINSTANCE.getLoopDecisionNode());

			elements.put(ExitNode_2005, GfcPackage.eINSTANCE.getExitNode());

			elements.put(Edge_4001, GfcPackage.eINSTANCE.getEdge());
		}
		return (ENamedElement) elements.get(type);
	}

	/**
	* @generated
	*/
	private static IElementType getElementType(String id) {
		return ElementTypeRegistry.getInstance().getType(id);
	}

	/**
	* @generated
	*/
	public static boolean isKnownElementType(IElementType elementType) {
		if (KNOWN_ELEMENT_TYPES == null) {
			KNOWN_ELEMENT_TYPES = new HashSet<IElementType>();
			KNOWN_ELEMENT_TYPES.add(Flowchart_1000);
			KNOWN_ELEMENT_TYPES.add(EntryNode_2001);
			KNOWN_ELEMENT_TYPES.add(ProcessingNode_2002);
			KNOWN_ELEMENT_TYPES.add(DecisionNode_2003);
			KNOWN_ELEMENT_TYPES.add(LoopDecisionNode_2004);
			KNOWN_ELEMENT_TYPES.add(ExitNode_2005);
			KNOWN_ELEMENT_TYPES.add(Edge_4001);
		}
		return KNOWN_ELEMENT_TYPES.contains(elementType);
	}

	/**
	* @generated
	*/
	public static IElementType getElementType(int visualID) {
		switch (visualID) {
		case FlowchartEditPart.VISUAL_ID:
			return Flowchart_1000;
		case EntryNodeEditPart.VISUAL_ID:
			return EntryNode_2001;
		case ProcessingNodeEditPart.VISUAL_ID:
			return ProcessingNode_2002;
		case DecisionNodeEditPart.VISUAL_ID:
			return DecisionNode_2003;
		case LoopDecisionNodeEditPart.VISUAL_ID:
			return LoopDecisionNode_2004;
		case ExitNodeEditPart.VISUAL_ID:
			return ExitNode_2005;
		case EdgeEditPart.VISUAL_ID:
			return Edge_4001;
		}
		return null;
	}

	/**
	* @generated
	*/
	public static final DiagramElementTypes TYPED_INSTANCE = new DiagramElementTypes(elementTypeImages) {

		/**
		* @generated
		*/
		@Override

		public boolean isKnownElementType(IElementType elementType) {
			return gfc.diagram.providers.GfcElementTypes.isKnownElementType(elementType);
		}

		/**
		* @generated
		*/
		@Override

		public IElementType getElementTypeForVisualId(int visualID) {
			return gfc.diagram.providers.GfcElementTypes.getElementType(visualID);
		}

		/**
		* @generated
		*/
		@Override

		public ENamedElement getDefiningNamedElement(IAdaptable elementTypeAdapter) {
			return gfc.diagram.providers.GfcElementTypes.getElement(elementTypeAdapter);
		}
	};

}
