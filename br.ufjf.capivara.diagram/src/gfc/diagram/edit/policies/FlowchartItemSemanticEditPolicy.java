/*
* 
*/
package gfc.diagram.edit.policies;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.commands.core.commands.DuplicateEObjectsCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;

import gfc.diagram.edit.commands.DecisionNodeCreateCommand;
import gfc.diagram.edit.commands.EntryNodeCreateCommand;
import gfc.diagram.edit.commands.ExitNodeCreateCommand;
import gfc.diagram.edit.commands.LoopDecisionNodeCreateCommand;
import gfc.diagram.edit.commands.ProcessingNodeCreateCommand;
import gfc.diagram.providers.GfcElementTypes;

/**
 * @generated
 */
public class FlowchartItemSemanticEditPolicy extends GfcBaseItemSemanticEditPolicy {

	/**
	* @generated
	*/
	public FlowchartItemSemanticEditPolicy() {
		super(GfcElementTypes.Flowchart_1000);
	}

	/**
	* @generated
	*/
	protected Command getCreateCommand(CreateElementRequest req) {
		if (GfcElementTypes.EntryNode_2001 == req.getElementType()) {
			return getGEFWrapper(new EntryNodeCreateCommand(req));
		}
		if (GfcElementTypes.ProcessingNode_2002 == req.getElementType()) {
			return getGEFWrapper(new ProcessingNodeCreateCommand(req));
		}
		if (GfcElementTypes.DecisionNode_2003 == req.getElementType()) {
			return getGEFWrapper(new DecisionNodeCreateCommand(req));
		}
		if (GfcElementTypes.LoopDecisionNode_2004 == req.getElementType()) {
			return getGEFWrapper(new LoopDecisionNodeCreateCommand(req));
		}
		if (GfcElementTypes.ExitNode_2005 == req.getElementType()) {
			return getGEFWrapper(new ExitNodeCreateCommand(req));
		}
		return super.getCreateCommand(req);
	}

	/**
	* @generated
	*/
	protected Command getDuplicateCommand(DuplicateElementsRequest req) {
		TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost()).getEditingDomain();
		return getGEFWrapper(new DuplicateAnythingCommand(editingDomain, req));
	}

	/**
	* @generated
	*/
	private static class DuplicateAnythingCommand extends DuplicateEObjectsCommand {

		/**
		* @generated
		*/
		public DuplicateAnythingCommand(TransactionalEditingDomain editingDomain, DuplicateElementsRequest req) {
			super(editingDomain, req.getLabel(), req.getElementsToBeDuplicated(), req.getAllDuplicatedElementsMap());
		}

	}

}
