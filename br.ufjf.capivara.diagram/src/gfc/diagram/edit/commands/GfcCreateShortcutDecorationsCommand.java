/*
 * 
 */
package gfc.diagram.edit.commands;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.View;

import gfc.diagram.edit.parts.FlowchartEditPart;

/**
 * @generated
 */
public class GfcCreateShortcutDecorationsCommand extends AbstractTransactionalCommand {

	/**
	* @generated
	*/
	private List myDescriptors;

	/**
	* @generated
	*/
	public GfcCreateShortcutDecorationsCommand(TransactionalEditingDomain editingDomain, View parentView,
			List viewDescriptors) {
		super(editingDomain, "Create Shortcuts", getWorkspaceFiles(parentView));  
		myDescriptors = viewDescriptors;
	}

	/**
	* @generated
	*/
	public GfcCreateShortcutDecorationsCommand(TransactionalEditingDomain editingDomain, View parentView,
			CreateViewRequest.ViewDescriptor viewDescriptor) {
		this(editingDomain, parentView, Collections.singletonList(viewDescriptor));
	}

	/**
	* @generated
	*/
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		for (Iterator it = myDescriptors.iterator(); it.hasNext();) {
			CreateViewRequest.ViewDescriptor nextDescriptor = (CreateViewRequest.ViewDescriptor) it.next();
			View view = (View) nextDescriptor.getAdapter(View.class);
			if (view != null && view.getEAnnotation("Shortcut") == null) {  
				EAnnotation shortcutAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
				shortcutAnnotation.setSource("Shortcut");  
				shortcutAnnotation.getDetails().put("modelID", FlowchartEditPart.MODEL_ID);  
				view.getEAnnotations().add(shortcutAnnotation);
			}
		}
		return CommandResult.newOKCommandResult();
	}
}
