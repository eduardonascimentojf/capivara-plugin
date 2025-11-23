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
import gfc.diagram.edit.parts.DecisionNodeIdEditPart;
import gfc.diagram.edit.parts.EntryNodeIdEditPart;
import gfc.diagram.edit.parts.ExitNodeIdEditPart;
import gfc.diagram.edit.parts.LoopDecisionNodeIdEditPart;
import gfc.diagram.edit.parts.ProcessingNodeIdEditPart;
import gfc.diagram.parsers.MessageFormatParser;
import gfc.diagram.part.GfcVisualIDRegistry;

/**
 * @generated
 */
public class GfcParserProvider extends AbstractProvider implements IParserProvider {

	/**
	* @generated
	*/
	private IParser entryNodeId_5001Parser;

	/**
	* @generated
	*/
	private IParser getEntryNodeId_5001Parser() {
		if (entryNodeId_5001Parser == null) {
			EAttribute[] features = new EAttribute[] { GfcPackage.eINSTANCE.getNode_Id() };
			MessageFormatParser parser = new MessageFormatParser(features);
			entryNodeId_5001Parser = parser;
		}
		return entryNodeId_5001Parser;
	}

	/**
	* @generated
	*/
	private IParser processingNodeId_5002Parser;

	/**
	* @generated
	*/
	private IParser getProcessingNodeId_5002Parser() {
		if (processingNodeId_5002Parser == null) {
			EAttribute[] features = new EAttribute[] { GfcPackage.eINSTANCE.getNode_Id() };
			MessageFormatParser parser = new MessageFormatParser(features);
			processingNodeId_5002Parser = parser;
		}
		return processingNodeId_5002Parser;
	}

	/**
	* @generated
	*/
	private IParser decisionNodeId_5003Parser;

	/**
	* @generated
	*/
	private IParser getDecisionNodeId_5003Parser() {
		if (decisionNodeId_5003Parser == null) {
			EAttribute[] features = new EAttribute[] { GfcPackage.eINSTANCE.getNode_Id() };
			MessageFormatParser parser = new MessageFormatParser(features);
			decisionNodeId_5003Parser = parser;
		}
		return decisionNodeId_5003Parser;
	}

	/**
	* @generated
	*/
	private IParser loopDecisionNodeId_5004Parser;

	/**
	* @generated
	*/
	private IParser getLoopDecisionNodeId_5004Parser() {
		if (loopDecisionNodeId_5004Parser == null) {
			EAttribute[] features = new EAttribute[] { GfcPackage.eINSTANCE.getNode_Id() };
			MessageFormatParser parser = new MessageFormatParser(features);
			loopDecisionNodeId_5004Parser = parser;
		}
		return loopDecisionNodeId_5004Parser;
	}

	/**
	* @generated
	*/
	private IParser exitNodeId_5005Parser;

	/**
	* @generated
	*/
	private IParser getExitNodeId_5005Parser() {
		if (exitNodeId_5005Parser == null) {
			EAttribute[] features = new EAttribute[] { GfcPackage.eINSTANCE.getNode_Id() };
			MessageFormatParser parser = new MessageFormatParser(features);
			exitNodeId_5005Parser = parser;
		}
		return exitNodeId_5005Parser;
	}

	/**
	* @generated
	*/
	protected IParser getParser(int visualID) {
		switch (visualID) {
		case EntryNodeIdEditPart.VISUAL_ID:
			return getEntryNodeId_5001Parser();
		case ProcessingNodeIdEditPart.VISUAL_ID:
			return getProcessingNodeId_5002Parser();
		case DecisionNodeIdEditPart.VISUAL_ID:
			return getDecisionNodeId_5003Parser();
		case LoopDecisionNodeIdEditPart.VISUAL_ID:
			return getLoopDecisionNodeId_5004Parser();
		case ExitNodeIdEditPart.VISUAL_ID:
			return getExitNodeId_5005Parser();
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
