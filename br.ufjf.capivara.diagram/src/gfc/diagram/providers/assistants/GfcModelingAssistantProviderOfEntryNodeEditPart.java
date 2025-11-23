/*
 * 
 */
package gfc.diagram.providers.assistants;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;

import gfc.diagram.edit.parts.DecisionNodeEditPart;
import gfc.diagram.edit.parts.EntryNodeEditPart;
import gfc.diagram.edit.parts.ExitNodeEditPart;
import gfc.diagram.edit.parts.LoopDecisionNodeEditPart;
import gfc.diagram.edit.parts.ProcessingNodeEditPart;
import gfc.diagram.providers.GfcElementTypes;
import gfc.diagram.providers.GfcModelingAssistantProvider;

/**
 * @generated
 */
public class GfcModelingAssistantProviderOfEntryNodeEditPart extends GfcModelingAssistantProvider {

	/**
	* @generated
	*/
	@Override

	public List<IElementType> getRelTypesOnSource(IAdaptable source) {
		IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source.getAdapter(IGraphicalEditPart.class);
		return doGetRelTypesOnSource((EntryNodeEditPart) sourceEditPart);
	}

	/**
	* @generated
	*/
	public List<IElementType> doGetRelTypesOnSource(EntryNodeEditPart source) {
		List<IElementType> types = new ArrayList<IElementType>(1);
		types.add(GfcElementTypes.Edge_4001);
		return types;
	}

	/**
	* @generated
	*/
	@Override

	public List<IElementType> getRelTypesOnSourceAndTarget(IAdaptable source, IAdaptable target) {
		IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source.getAdapter(IGraphicalEditPart.class);
		IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target.getAdapter(IGraphicalEditPart.class);
		return doGetRelTypesOnSourceAndTarget((EntryNodeEditPart) sourceEditPart, targetEditPart);
	}

	/**
	* @generated
	*/
	public List<IElementType> doGetRelTypesOnSourceAndTarget(EntryNodeEditPart source,
			IGraphicalEditPart targetEditPart) {
		List<IElementType> types = new LinkedList<IElementType>();
		if (targetEditPart instanceof EntryNodeEditPart) {
			types.add(GfcElementTypes.Edge_4001);
		}
		if (targetEditPart instanceof ProcessingNodeEditPart) {
			types.add(GfcElementTypes.Edge_4001);
		}
		if (targetEditPart instanceof DecisionNodeEditPart) {
			types.add(GfcElementTypes.Edge_4001);
		}
		if (targetEditPart instanceof LoopDecisionNodeEditPart) {
			types.add(GfcElementTypes.Edge_4001);
		}
		if (targetEditPart instanceof ExitNodeEditPart) {
			types.add(GfcElementTypes.Edge_4001);
		}
		return types;
	}

	/**
	* @generated
	*/
	@Override

	public List<IElementType> getTypesForTarget(IAdaptable source, IElementType relationshipType) {
		IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source.getAdapter(IGraphicalEditPart.class);
		return doGetTypesForTarget((EntryNodeEditPart) sourceEditPart, relationshipType);
	}

	/**
	* @generated
	*/
	public List<IElementType> doGetTypesForTarget(EntryNodeEditPart source, IElementType relationshipType) {
		List<IElementType> types = new ArrayList<IElementType>();
		if (relationshipType == GfcElementTypes.Edge_4001) {
			types.add(GfcElementTypes.EntryNode_2001);
			types.add(GfcElementTypes.ProcessingNode_2002);
			types.add(GfcElementTypes.DecisionNode_2003);
			types.add(GfcElementTypes.LoopDecisionNode_2004);
			types.add(GfcElementTypes.ExitNode_2005);
		}
		return types;
	}

	/**
	* @generated
	*/
	@Override

	public List<IElementType> getRelTypesOnTarget(IAdaptable target) {
		IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target.getAdapter(IGraphicalEditPart.class);
		return doGetRelTypesOnTarget((EntryNodeEditPart) targetEditPart);
	}

	/**
	* @generated
	*/
	public List<IElementType> doGetRelTypesOnTarget(EntryNodeEditPart target) {
		List<IElementType> types = new ArrayList<IElementType>(1);
		types.add(GfcElementTypes.Edge_4001);
		return types;
	}

	/**
	* @generated
	*/
	@Override

	public List<IElementType> getTypesForSource(IAdaptable target, IElementType relationshipType) {
		IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target.getAdapter(IGraphicalEditPart.class);
		return doGetTypesForSource((EntryNodeEditPart) targetEditPart, relationshipType);
	}

	/**
	* @generated
	*/
	public List<IElementType> doGetTypesForSource(EntryNodeEditPart target, IElementType relationshipType) {
		List<IElementType> types = new ArrayList<IElementType>();
		if (relationshipType == GfcElementTypes.Edge_4001) {
			types.add(GfcElementTypes.EntryNode_2001);
			types.add(GfcElementTypes.ProcessingNode_2002);
			types.add(GfcElementTypes.DecisionNode_2003);
			types.add(GfcElementTypes.LoopDecisionNode_2004);
			types.add(GfcElementTypes.ExitNode_2005);
		}
		return types;
	}

}
