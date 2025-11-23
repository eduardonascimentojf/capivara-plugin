/*
 * 
 */
package gfc.diagram.providers.assistants;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;

import gfc.diagram.providers.GfcElementTypes;
import gfc.diagram.providers.GfcModelingAssistantProvider;

/**
 * @generated
 */
public class GfcModelingAssistantProviderOfFlowchartEditPart extends GfcModelingAssistantProvider {

	/**
	* @generated
	*/
	@Override

	public List<IElementType> getTypesForPopupBar(IAdaptable host) {
		List<IElementType> types = new ArrayList<IElementType>(5);
		types.add(GfcElementTypes.EntryNode_2001);
		types.add(GfcElementTypes.ProcessingNode_2002);
		types.add(GfcElementTypes.DecisionNode_2003);
		types.add(GfcElementTypes.LoopDecisionNode_2004);
		types.add(GfcElementTypes.ExitNode_2005);
		return types;
	}

}
