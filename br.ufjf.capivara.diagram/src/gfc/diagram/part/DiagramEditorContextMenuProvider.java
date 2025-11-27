package gfc.diagram.part;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.ContributionItemService;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.providers.DiagramContextMenuProvider;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchPart;

public class DiagramEditorContextMenuProvider extends DiagramContextMenuProvider {

	/**
	 * @generated
	 */
	private IWorkbenchPart part;

	/**
	* @generated
	*/
	private DeleteElementAction deleteAction;

	/**
	 * @generated
	 */
	public DiagramEditorContextMenuProvider(IWorkbenchPart part, EditPartViewer viewer) {
		super(part, viewer);
		this.part = part;
		deleteAction = new DeleteElementAction(part);
		deleteAction.init();
	}

	/**
	* @generated
	*/
	public void dispose() {
		if (deleteAction != null) {
			deleteAction.dispose();
			deleteAction = null;
		}
		super.dispose();
	}

	@Override
	public void buildContextMenu(final IMenuManager menu) {
		getViewer().flush();
		TransactionalEditingDomain editingDomain = TransactionUtil
				.getEditingDomain(getViewer().getContents().getModel());
		if (editingDomain == null) {
			System.out.print("Entrou");
			return;
		}

		try {
			editingDomain.runExclusive(new Runnable() {
				public void run() {
					ContributionItemService.getInstance().contributeToPopupMenu(DiagramEditorContextMenuProvider.this,
							part);

					menu.remove(ActionIds.ACTION_DELETE_FROM_MODEL);
					menu.remove(ActionIds.ACTION_ADD_NOTE);
					menu.remove(ActionIds.ACTION_ADD_NOTELINK);
					menu.remove(ActionIds.ACTION_ADD_TEXT);
					menu.remove(ActionIds.MENU_DIAGRAM_ADD);
					menu.remove(ActionIds.MENU_EDIT);
				}
			});
		} catch (Exception e) {
			// Lidar com exceção, se necessário
			System.out.print(e);
		}
	}
}
