package gfc.diagram.part;

import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramActionBarContributor;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.actions.EnhancedPrintActionHelper;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.actions.RenderedPrintPreviewAction;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;

import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import gfc.diagram.actions.ExportDiagramAsImageAction;

/**
 * Adiciona ações personalizadas à barra de ferramentas do editor GMF.
 */
public class GfcDiagramActionBarContributor extends DiagramActionBarContributor {

	@Override
	protected Class<GfcDiagramEditor> getEditorClass() {
		return GfcDiagramEditor.class;
	}

	@Override
	protected String getEditorId() {
		return GfcDiagramEditor.ID;
	}

	/**
	* @generated
	*/
	public void init(IActionBars bars, IWorkbenchPage page) {
		super.init(bars, page);
		// print preview
		IMenuManager fileMenu = bars.getMenuManager().findMenuUsingPath(IWorkbenchActionConstants.M_FILE);
		assert fileMenu != null;
		IAction printPreviewAction = new RenderedPrintPreviewAction(new EnhancedPrintActionHelper());
		fileMenu.insertBefore("print", printPreviewAction); //$NON-NLS-1$
		IMenuManager editMenu = bars.getMenuManager().findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);
		assert editMenu != null;
		if (editMenu.find("validationGroup") == null) { //$NON-NLS-1$
			editMenu.add(new GroupMarker("validationGroup")); //$NON-NLS-1$
		}
		IAction validateAction = new ValidateAction(page);
		editMenu.appendToGroup("validationGroup", validateAction); //$NON-NLS-1$
	}

	@Override
	protected void buildActions() {
		super.buildActions();
	}

	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		super.contributeToToolBar(toolBarManager);

		if (getPage() != null && getPage().getActivePart() instanceof IDiagramWorkbenchPart part) {
			// Adiciona o botão de exportar como imagem
			toolBarManager.add(new ExportDiagramAsImageAction(part));
		}
	}
}
