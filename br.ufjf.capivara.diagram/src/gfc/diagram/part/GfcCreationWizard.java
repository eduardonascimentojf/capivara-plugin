/*
 * 
 */
package gfc.diagram.part;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

/**
 * @generated
 */
public class GfcCreationWizard extends Wizard implements INewWizard {

	/**
	* @generated
	*/
	private IWorkbench workbench;

	/**
	* @generated
	*/
	protected IStructuredSelection selection;

	/**
	* @generated
	*/
	protected GfcCreationWizardPage diagramModelFilePage;

	/**
	* @generated
	*/
	protected GfcCreationWizardPage domainModelFilePage;

	/**
	* @generated
	*/
	protected Resource diagram;

	/**
	* @generated
	*/
	private boolean openNewlyCreatedDiagramEditor = true;

	/**
	* @generated
	*/
	public IWorkbench getWorkbench() {
		return workbench;
	}

	/**
	* @generated
	*/
	public IStructuredSelection getSelection() {
		return selection;
	}

	/**
	* @generated
	*/
	public final Resource getDiagram() {
		return diagram;
	}

	/**
	* @generated
	*/
	public final boolean isOpenNewlyCreatedDiagramEditor() {
		return openNewlyCreatedDiagramEditor;
	}

	/**
	* @generated
	*/
	public void setOpenNewlyCreatedDiagramEditor(boolean openNewlyCreatedDiagramEditor) {
		this.openNewlyCreatedDiagramEditor = openNewlyCreatedDiagramEditor;
	}

	/**
	* @generated
	*/
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
		setWindowTitle(Messages.GfcCreationWizardTitle);
		setDefaultPageImageDescriptor(
				GfcDiagramEditorPlugin.getBundledImageDescriptor("icons/wizban/NewGfcWizard.gif")); //$NON-NLS-1$
		setNeedsProgressMonitor(true);
	}

	/**
	* @generated
	*/
	public void addPages() {
		diagramModelFilePage = new GfcCreationWizardPage("DiagramModelFile", getSelection(), "gfc_diagram"); //$NON-NLS-1$ //$NON-NLS-2$
		diagramModelFilePage.setTitle(Messages.GfcCreationWizard_DiagramModelFilePageTitle);
		diagramModelFilePage.setDescription(Messages.GfcCreationWizard_DiagramModelFilePageDescription);
		addPage(diagramModelFilePage);

		domainModelFilePage = new GfcCreationWizardPage("DomainModelFile", getSelection(), "gfc") { //$NON-NLS-1$ //$NON-NLS-2$

			public void setVisible(boolean visible) {
				if (visible) {
					String fileName = diagramModelFilePage.getFileName();
					fileName = fileName.substring(0, fileName.length() - ".gfc_diagram".length()); //$NON-NLS-1$
					setFileName(GfcDiagramEditorUtil.getUniqueFileName(getContainerFullPath(), fileName, "gfc")); //$NON-NLS-1$
				}
				super.setVisible(visible);
			}
		};
		domainModelFilePage.setTitle(Messages.GfcCreationWizard_DomainModelFilePageTitle);
		domainModelFilePage.setDescription(Messages.GfcCreationWizard_DomainModelFilePageDescription);
		addPage(domainModelFilePage);
	}

	/**
	* @generated
	*/
	public boolean performFinish() {
		IRunnableWithProgress op = new WorkspaceModifyOperation(null) {

			protected void execute(IProgressMonitor monitor) throws CoreException, InterruptedException {
				diagram = GfcDiagramEditorUtil.createDiagram(diagramModelFilePage.getURI(),
						domainModelFilePage.getURI(), monitor);
				if (isOpenNewlyCreatedDiagramEditor() && diagram != null) {
					try {
						GfcDiagramEditorUtil.openDiagram(diagram);
					} catch (PartInitException e) {
						ErrorDialog.openError(getContainer().getShell(), Messages.GfcCreationWizardOpenEditorError,
								null, e.getStatus());
					}
				}
			}
		};
		try {
			getContainer().run(false, true, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof CoreException) {
				ErrorDialog.openError(getContainer().getShell(), Messages.GfcCreationWizardCreationError, null,
						((CoreException) e.getTargetException()).getStatus());
			} else {
				GfcDiagramEditorPlugin.getInstance().logError("Error creating diagram", e.getTargetException()); //$NON-NLS-1$
			}
			return false;
		}
		return diagram != null;
	}
}
