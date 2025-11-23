/*
* 
*/
package gfc.diagram.navigator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserOptions;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITreePathLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.ViewerLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;

import gfc.Edge;
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
import gfc.diagram.part.GfcDiagramEditorPlugin;
import gfc.diagram.part.GfcVisualIDRegistry;
import gfc.diagram.providers.GfcElementTypes;
import gfc.diagram.providers.GfcParserProvider;

/**
 * @generated
 */
public class GfcNavigatorLabelProvider extends LabelProvider implements ICommonLabelProvider, ITreePathLabelProvider {

	/**
	* @generated
	*/
	static {
		GfcDiagramEditorPlugin.getInstance().getImageRegistry().put("Navigator?UnknownElement", //$NON-NLS-1$
				ImageDescriptor.getMissingImageDescriptor());
		GfcDiagramEditorPlugin.getInstance().getImageRegistry().put("Navigator?ImageNotFound", //$NON-NLS-1$
				ImageDescriptor.getMissingImageDescriptor());
	}

	/**
	* @generated
	*/
	public void updateLabel(ViewerLabel label, TreePath elementPath) {
		Object element = elementPath.getLastSegment();
		if (element instanceof GfcNavigatorItem && !isOwnView(((GfcNavigatorItem) element).getView())) {
			return;
		}
		label.setText(getText(element));
		label.setImage(getImage(element));
	}

	/**
	* @generated
	*/
	public Image getImage(Object element) {
		if (element instanceof GfcNavigatorGroup) {
			GfcNavigatorGroup group = (GfcNavigatorGroup) element;
			return GfcDiagramEditorPlugin.getInstance().getBundledImage(group.getIcon());
		}

		if (element instanceof GfcNavigatorItem) {
			GfcNavigatorItem navigatorItem = (GfcNavigatorItem) element;
			if (!isOwnView(navigatorItem.getView())) {
				return super.getImage(element);
			}
			return getImage(navigatorItem.getView());
		}

		// Due to plugin.xml content will be called only for "own" views
		if (element instanceof IAdaptable) {
			View view = (View) ((IAdaptable) element).getAdapter(View.class);
			if (view != null && isOwnView(view)) {
				return getImage(view);
			}
		}

		return super.getImage(element);
	}

	/**
	* @generated
	*/
	public Image getImage(View view) {
		switch (GfcVisualIDRegistry.getVisualID(view)) {
		case FlowchartEditPart.VISUAL_ID:
			return getImage("Navigator?Diagram?http://www.ufjf.br/capivara/gfc?Flowchart", //$NON-NLS-1$
					GfcElementTypes.Flowchart_1000);
		case EntryNodeEditPart.VISUAL_ID:
			return getImage("Navigator?TopLevelNode?http://www.ufjf.br/capivara/gfc?EntryNode", //$NON-NLS-1$
					GfcElementTypes.EntryNode_2001);
		case ProcessingNodeEditPart.VISUAL_ID:
			return getImage("Navigator?TopLevelNode?http://www.ufjf.br/capivara/gfc?ProcessingNode", //$NON-NLS-1$
					GfcElementTypes.ProcessingNode_2002);
		case DecisionNodeEditPart.VISUAL_ID:
			return getImage("Navigator?TopLevelNode?http://www.ufjf.br/capivara/gfc?DecisionNode", //$NON-NLS-1$
					GfcElementTypes.DecisionNode_2003);
		case LoopDecisionNodeEditPart.VISUAL_ID:
			return getImage("Navigator?TopLevelNode?http://www.ufjf.br/capivara/gfc?LoopDecisionNode", //$NON-NLS-1$
					GfcElementTypes.LoopDecisionNode_2004);
		case ExitNodeEditPart.VISUAL_ID:
			return getImage("Navigator?TopLevelNode?http://www.ufjf.br/capivara/gfc?ExitNode", //$NON-NLS-1$
					GfcElementTypes.ExitNode_2005);
		case EdgeEditPart.VISUAL_ID:
			return getImage("Navigator?Link?http://www.ufjf.br/capivara/gfc?Edge", GfcElementTypes.Edge_4001); //$NON-NLS-1$
		}
		return getImage("Navigator?UnknownElement", null); //$NON-NLS-1$
	}

	/**
	* @generated
	*/
	private Image getImage(String key, IElementType elementType) {
		ImageRegistry imageRegistry = GfcDiagramEditorPlugin.getInstance().getImageRegistry();
		Image image = imageRegistry.get(key);
		if (image == null && elementType != null && GfcElementTypes.isKnownElementType(elementType)) {
			image = GfcElementTypes.getImage(elementType);
			imageRegistry.put(key, image);
		}

		if (image == null) {
			image = imageRegistry.get("Navigator?ImageNotFound"); //$NON-NLS-1$
			imageRegistry.put(key, image);
		}
		return image;
	}

	/**
	* @generated
	*/
	public String getText(Object element) {
		if (element instanceof GfcNavigatorGroup) {
			GfcNavigatorGroup group = (GfcNavigatorGroup) element;
			return group.getGroupName();
		}

		if (element instanceof GfcNavigatorItem) {
			GfcNavigatorItem navigatorItem = (GfcNavigatorItem) element;
			if (!isOwnView(navigatorItem.getView())) {
				return null;
			}
			return getText(navigatorItem.getView());
		}

		// Due to plugin.xml content will be called only for "own" views
		if (element instanceof IAdaptable) {
			View view = (View) ((IAdaptable) element).getAdapter(View.class);
			if (view != null && isOwnView(view)) {
				return getText(view);
			}
		}

		return super.getText(element);
	}

	/**
	* @generated
	*/
	public String getText(View view) {
		if (view.getElement() != null && view.getElement().eIsProxy()) {
			return getUnresolvedDomainElementProxyText(view);
		}
		switch (GfcVisualIDRegistry.getVisualID(view)) {
		case FlowchartEditPart.VISUAL_ID:
			return getFlowchart_1000Text(view);
		case EntryNodeEditPart.VISUAL_ID:
			return getEntryNode_2001Text(view);
		case ProcessingNodeEditPart.VISUAL_ID:
			return getProcessingNode_2002Text(view);
		case DecisionNodeEditPart.VISUAL_ID:
			return getDecisionNode_2003Text(view);
		case LoopDecisionNodeEditPart.VISUAL_ID:
			return getLoopDecisionNode_2004Text(view);
		case ExitNodeEditPart.VISUAL_ID:
			return getExitNode_2005Text(view);
		case EdgeEditPart.VISUAL_ID:
			return getEdge_4001Text(view);
		}
		return getUnknownElementText(view);
	}

	/**
	* @generated
	*/
	private String getFlowchart_1000Text(View view) {
		return ""; //$NON-NLS-1$
	}

	/**
	* @generated
	*/
	private String getEntryNode_2001Text(View view) {
		IParser parser = GfcParserProvider.getParser(GfcElementTypes.EntryNode_2001,
				view.getElement() != null ? view.getElement() : view,
				GfcVisualIDRegistry.getType(EntryNodeIdEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			GfcDiagramEditorPlugin.getInstance().logError("Parser was not found for label " + 5001); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	* @generated
	*/
	private String getProcessingNode_2002Text(View view) {
		IParser parser = GfcParserProvider.getParser(GfcElementTypes.ProcessingNode_2002,
				view.getElement() != null ? view.getElement() : view,
				GfcVisualIDRegistry.getType(ProcessingNodeIdEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			GfcDiagramEditorPlugin.getInstance().logError("Parser was not found for label " + 5002); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	* @generated
	*/
	private String getDecisionNode_2003Text(View view) {
		IParser parser = GfcParserProvider.getParser(GfcElementTypes.DecisionNode_2003,
				view.getElement() != null ? view.getElement() : view,
				GfcVisualIDRegistry.getType(DecisionNodeIdEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			GfcDiagramEditorPlugin.getInstance().logError("Parser was not found for label " + 5003); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	* @generated
	*/
	private String getLoopDecisionNode_2004Text(View view) {
		IParser parser = GfcParserProvider.getParser(GfcElementTypes.LoopDecisionNode_2004,
				view.getElement() != null ? view.getElement() : view,
				GfcVisualIDRegistry.getType(LoopDecisionNodeIdEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			GfcDiagramEditorPlugin.getInstance().logError("Parser was not found for label " + 5004); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	* @generated
	*/
	private String getExitNode_2005Text(View view) {
		IParser parser = GfcParserProvider.getParser(GfcElementTypes.ExitNode_2005,
				view.getElement() != null ? view.getElement() : view,
				GfcVisualIDRegistry.getType(ExitNodeIdEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			GfcDiagramEditorPlugin.getInstance().logError("Parser was not found for label " + 5005); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	* @generated
	*/
	private String getEdge_4001Text(View view) {
		Edge domainModelElement = (Edge) view.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getLabel();
		} else {
			GfcDiagramEditorPlugin.getInstance().logError("No domain element for view with visualID = " + 4001); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	* @generated
	*/
	private String getUnknownElementText(View view) {
		return "<UnknownElement Visual_ID = " + view.getType() + ">"; //$NON-NLS-1$  //$NON-NLS-2$
	}

	/**
	* @generated
	*/
	private String getUnresolvedDomainElementProxyText(View view) {
		return "<Unresolved domain element Visual_ID = " + view.getType() + ">"; //$NON-NLS-1$  //$NON-NLS-2$
	}

	/**
	* @generated
	*/
	public void init(ICommonContentExtensionSite aConfig) {
	}

	/**
	* @generated
	*/
	public void restoreState(IMemento aMemento) {
	}

	/**
	* @generated
	*/
	public void saveState(IMemento aMemento) {
	}

	/**
	* @generated
	*/
	public String getDescription(Object anElement) {
		return null;
	}

	/**
	* @generated
	*/
	private boolean isOwnView(View view) {
		return FlowchartEditPart.MODEL_ID.equals(GfcVisualIDRegistry.getModelID(view));
	}

}
