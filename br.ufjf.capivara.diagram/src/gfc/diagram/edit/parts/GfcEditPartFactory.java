/*
 * 
 */
package gfc.diagram.edit.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tooling.runtime.directedit.locator.CellEditorLocatorAccess;

import gfc.diagram.part.GfcVisualIDRegistry;

/**
 * @generated
 */
public class GfcEditPartFactory implements EditPartFactory {

	/**
	* @generated
	*/
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof View) {
			View view = (View) model;
			switch (GfcVisualIDRegistry.getVisualID(view)) {

			case FlowchartEditPart.VISUAL_ID:
				return new FlowchartEditPart(view);

			case EntryNodeEditPart.VISUAL_ID:
				return new EntryNodeEditPart(view);

			case EntryNodeIdEditPart.VISUAL_ID:
				return new EntryNodeIdEditPart(view);

			case ProcessingNodeEditPart.VISUAL_ID:
				return new ProcessingNodeEditPart(view);

			case ProcessingNodeIdEditPart.VISUAL_ID:
				return new ProcessingNodeIdEditPart(view);

			case DecisionNodeEditPart.VISUAL_ID:
				return new DecisionNodeEditPart(view);

			case DecisionNodeIdEditPart.VISUAL_ID:
				return new DecisionNodeIdEditPart(view);

			case LoopDecisionNodeEditPart.VISUAL_ID:
				return new LoopDecisionNodeEditPart(view);

			case LoopDecisionNodeIdEditPart.VISUAL_ID:
				return new LoopDecisionNodeIdEditPart(view);

			case ExitNodeEditPart.VISUAL_ID:
				return new ExitNodeEditPart(view);

			case ExitNodeIdEditPart.VISUAL_ID:
				return new ExitNodeIdEditPart(view);

			case SwitchNodeEditPart.VISUAL_ID:
				return new SwitchNodeEditPart(view);

			case SwitchNodeIdEditPart.VISUAL_ID:
				return new SwitchNodeIdEditPart(view);

			case CaseNodeEditPart.VISUAL_ID:
				return new CaseNodeEditPart(view);

			case CaseNodeIdEditPart.VISUAL_ID:
				return new CaseNodeIdEditPart(view);

			case EdgeEditPart.VISUAL_ID:
				return new EdgeEditPart(view);

			case EdgeLabelEditPart.VISUAL_ID:
				return new EdgeLabelEditPart(view);

			}
		}
		return createUnrecognizedEditPart(context, model);
	}

	/**
	* @generated
	*/
	private EditPart createUnrecognizedEditPart(EditPart context, Object model) {
		// Handle creation of unrecognized child node EditParts here
		return null;
	}

	/**
	* @generated
	*/
	public static CellEditorLocator getTextCellEditorLocator(ITextAwareEditPart source) {
		return CellEditorLocatorAccess.INSTANCE.getTextCellEditorLocator(source);
	}

}
