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

import gfc.diagram.edit.parts.CaseNodeEditPart;
import gfc.diagram.edit.parts.DecisionNodeEditPart;
import gfc.diagram.edit.parts.EntryNodeEditPart;
import gfc.diagram.edit.parts.ExitNodeEditPart;
import gfc.diagram.edit.parts.LoopDecisionNodeEditPart;
import gfc.diagram.edit.parts.ProcessingNodeEditPart;
import gfc.diagram.edit.parts.SwitchNodeEditPart;
import gfc.diagram.providers.GfcElementTypes;
import gfc.diagram.providers.GfcModelingAssistantProvider;

/**
 * @generated
 */
public class GfcModelingAssistantProviderOfDecisionNodeEditPart extends GfcModelingAssistantProvider {

	/**
	* @generated
	*/
	@Override

	public List<IElementType> getRelTypesOnSource(IAdaptable source) {
		IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source.getAdapter(IGraphicalEditPart.class);
		return doGetRelTypesOnSource((DecisionNodeEditPart) sourceEditPart);
	}

	/**
	* @generated
	*/
	public List<IElementType> doGetRelTypesOnSource(DecisionNodeEditPart source) {
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
		return doGetRelTypesOnSourceAndTarget((DecisionNodeEditPart) sourceEditPart, targetEditPart);
	}

	/**
	* @generated
	*/
	public List<IElementType> doGetRelTypesOnSourceAndTarget(DecisionNodeEditPart source,
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
		if (targetEditPart instanceof SwitchNodeEditPart) {
			types.add(GfcElementTypes.Edge_4001);
		}
		if (targetEditPart instanceof CaseNodeEditPart) {
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
		return doGetTypesForTarget((DecisionNodeEditPart) sourceEditPart, relationshipType);
	}

	/**
	* @generated
	*/
	public List<IElementType> doGetTypesForTarget(DecisionNodeEditPart source, IElementType relationshipType) {
		List<IElementType> types = new ArrayList<IElementType>();
		if (relationshipType == GfcElementTypes.Edge_4001) {
			types.add(GfcElementTypes.EntryNode_2001);
			types.add(GfcElementTypes.ProcessingNode_2002);
			types.add(GfcElementTypes.DecisionNode_2003);
			types.add(GfcElementTypes.LoopDecisionNode_2004);
			types.add(GfcElementTypes.ExitNode_2005);
			types.add(GfcElementTypes.SwitchNode_2006);
			types.add(GfcElementTypes.CaseNode_2007);
		}
		return types;
	}

	/**
	* @generated
	*/
	@Override

	public List<IElementType> getRelTypesOnTarget(IAdaptable target) {
		IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target.getAdapter(IGraphicalEditPart.class);
		return doGetRelTypesOnTarget((DecisionNodeEditPart) targetEditPart);
	}

	/**
	* @generated
	*/
	public List<IElementType> doGetRelTypesOnTarget(DecisionNodeEditPart target) {
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
		return doGetTypesForSource((DecisionNodeEditPart) targetEditPart, relationshipType);
	}

	/**
	* @generated
	*/
	public List<IElementType> doGetTypesForSource(DecisionNodeEditPart target, IElementType relationshipType) {
		List<IElementType> types = new ArrayList<IElementType>();
		if (relationshipType == GfcElementTypes.Edge_4001) {
			types.add(GfcElementTypes.EntryNode_2001);
			types.add(GfcElementTypes.ProcessingNode_2002);
			types.add(GfcElementTypes.DecisionNode_2003);
			types.add(GfcElementTypes.LoopDecisionNode_2004);
			types.add(GfcElementTypes.ExitNode_2005);
			types.add(GfcElementTypes.SwitchNode_2006);
			types.add(GfcElementTypes.CaseNode_2007);
		}
		return types;
	}

}
