/*
 * 
 */
package gfc.diagram.providers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.GetParserOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserProvider;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserService;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.ui.services.parser.ParserHintAdapter;
import org.eclipse.gmf.runtime.notation.View;

import gfc.GfcPackage;
import gfc.diagram.edit.parts.CaseNodeIdEditPart;
import gfc.diagram.edit.parts.DecisionNodeIdEditPart;
import gfc.diagram.edit.parts.EdgeLabelEditPart;
import gfc.diagram.edit.parts.EntryNodeIdEditPart;
import gfc.diagram.edit.parts.ExitNodeIdEditPart;
import gfc.diagram.edit.parts.LoopDecisionNodeIdEditPart;
import gfc.diagram.edit.parts.ProcessingNodeIdEditPart;
import gfc.diagram.edit.parts.SwitchNodeIdEditPart;
import gfc.diagram.parsers.MessageFormatParser;
import gfc.diagram.part.GfcVisualIDRegistry;

/**
 * @generated
 */
public class GfcParserProvider extends AbstractProvider implements IParserProvider {

	/**
	* @generated
	*/
	private IParser entryNodeLabel_5001Parser;

	/**
	* @generated
	*/
	private IParser getEntryNodeLabel_5001Parser() {
		if (entryNodeLabel_5001Parser == null) {
			EAttribute[] features = new EAttribute[] { GfcPackage.eINSTANCE.getNode_Label() };
			MessageFormatParser parser = new MessageFormatParser(features);
			entryNodeLabel_5001Parser = parser;
		}
		return entryNodeLabel_5001Parser;
	}

	/**
	* @generated
	*/
	private IParser processingNodeLabel_5002Parser;

	/**
	* @generated
	*/
	private IParser getProcessingNodeLabel_5002Parser() {
		if (processingNodeLabel_5002Parser == null) {
			EAttribute[] features = new EAttribute[] { GfcPackage.eINSTANCE.getNode_Label() };
			MessageFormatParser parser = new MessageFormatParser(features);
			processingNodeLabel_5002Parser = parser;
		}
		return processingNodeLabel_5002Parser;
	}

	/**
	* @generated
	*/
	private IParser decisionNodeLabel_5003Parser;

	/**
	* @generated
	*/
	private IParser getDecisionNodeLabel_5003Parser() {
		if (decisionNodeLabel_5003Parser == null) {
			EAttribute[] features = new EAttribute[] { GfcPackage.eINSTANCE.getNode_Label() };
			MessageFormatParser parser = new MessageFormatParser(features);
			decisionNodeLabel_5003Parser = parser;
		}
		return decisionNodeLabel_5003Parser;
	}

	/**
	* @generated
	*/
	private IParser loopDecisionNodeLabel_5004Parser;

	/**
	* @generated
	*/
	private IParser getLoopDecisionNodeLabel_5004Parser() {
		if (loopDecisionNodeLabel_5004Parser == null) {
			EAttribute[] features = new EAttribute[] { GfcPackage.eINSTANCE.getNode_Label() };
			MessageFormatParser parser = new MessageFormatParser(features);
			loopDecisionNodeLabel_5004Parser = parser;
		}
		return loopDecisionNodeLabel_5004Parser;
	}

	/**
	* @generated
	*/
	private IParser exitNodeLabel_5005Parser;

	/**
	* @generated
	*/
	private IParser getExitNodeLabel_5005Parser() {
		if (exitNodeLabel_5005Parser == null) {
			EAttribute[] features = new EAttribute[] { GfcPackage.eINSTANCE.getNode_Label() };
			MessageFormatParser parser = new MessageFormatParser(features);
			exitNodeLabel_5005Parser = parser;
		}
		return exitNodeLabel_5005Parser;
	}

	/**
	* @generated
	*/
	private IParser switchNodeLabel_5006Parser;

	/**
	* @generated
	*/
	private IParser getSwitchNodeLabel_5006Parser() {
		if (switchNodeLabel_5006Parser == null) {
			EAttribute[] features = new EAttribute[] { GfcPackage.eINSTANCE.getNode_Label() };
			MessageFormatParser parser = new MessageFormatParser(features);
			switchNodeLabel_5006Parser = parser;
		}
		return switchNodeLabel_5006Parser;
	}

	/**
	* @generated
	*/
	private IParser caseNodeLabel_5007Parser;

	/**
	* @generated
	*/
	private IParser getCaseNodeLabel_5007Parser() {
		if (caseNodeLabel_5007Parser == null) {
			EAttribute[] features = new EAttribute[] { GfcPackage.eINSTANCE.getNode_Label() };
			MessageFormatParser parser = new MessageFormatParser(features);
			caseNodeLabel_5007Parser = parser;
		}
		return caseNodeLabel_5007Parser;
	}

	/**
	* @generated
	*/
	private IParser edgeLabel_6001Parser;

	/**
	* @generated
	*/
	private IParser getEdgeLabel_6001Parser() {
		if (edgeLabel_6001Parser == null) {
			EAttribute[] features = new EAttribute[] { GfcPackage.eINSTANCE.getEdge_Label() };
			MessageFormatParser parser = new MessageFormatParser(features);
			edgeLabel_6001Parser = parser;
		}
		return edgeLabel_6001Parser;
	}

	/**
	* @generated
	*/
	protected IParser getParser(int visualID) {
		switch (visualID) {
		case EntryNodeIdEditPart.VISUAL_ID:
			return getEntryNodeLabel_5001Parser();
		case ProcessingNodeIdEditPart.VISUAL_ID:
			return getProcessingNodeLabel_5002Parser();
		case DecisionNodeIdEditPart.VISUAL_ID:
			return getDecisionNodeLabel_5003Parser();
		case LoopDecisionNodeIdEditPart.VISUAL_ID:
			return getLoopDecisionNodeLabel_5004Parser();
		case ExitNodeIdEditPart.VISUAL_ID:
			return getExitNodeLabel_5005Parser();
		case SwitchNodeIdEditPart.VISUAL_ID:
			return getSwitchNodeLabel_5006Parser();
		case CaseNodeIdEditPart.VISUAL_ID:
			return getCaseNodeLabel_5007Parser();
		case EdgeLabelEditPart.VISUAL_ID:
			return getEdgeLabel_6001Parser();
		}
		return null;
	}

	/**
	* Utility method that consults ParserService
	* @generated
	*/
	public static IParser getParser(IElementType type, EObject object, String parserHint) {
		return ParserService.getInstance().getParser(new HintAdapter(type, object, parserHint));
	}

	/**
	* @generated
	*/
	public IParser getParser(IAdaptable hint) {
		String vid = (String) hint.getAdapter(String.class);
		if (vid != null) {
			return getParser(GfcVisualIDRegistry.getVisualID(vid));
		}
		View view = (View) hint.getAdapter(View.class);
		if (view != null) {
			return getParser(GfcVisualIDRegistry.getVisualID(view));
		}
		return null;
	}

	/**
	* @generated
	*/
	public boolean provides(IOperation operation) {
		if (operation instanceof GetParserOperation) {
			IAdaptable hint = ((GetParserOperation) operation).getHint();
			if (GfcElementTypes.getElement(hint) == null) {
				return false;
			}
			return getParser(hint) != null;
		}
		return false;
	}

	/**
	* @generated
	*/
	private static class HintAdapter extends ParserHintAdapter {

		/**
		* @generated
		*/
		private final IElementType elementType;

		/**
		* @generated
		*/
		public HintAdapter(IElementType type, EObject object, String parserHint) {
			super(object, parserHint);
			assert type != null;
			elementType = type;
		}

		/**
		* @generated
		*/
		public Object getAdapter(Class adapter) {
			if (IElementType.class.equals(adapter)) {
				return elementType;
			}
			return super.getAdapter(adapter);
		}
	}

}
