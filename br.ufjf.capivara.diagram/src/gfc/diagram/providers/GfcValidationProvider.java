/*
 * 
 */
package gfc.diagram.providers;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.View;

import gfc.diagram.edit.parts.FlowchartEditPart;
import gfc.diagram.part.GfcDiagramEditorPlugin;
import gfc.diagram.part.GfcVisualIDRegistry;

/**
 * @generated
 */
public class GfcValidationProvider {

	/**
	* @generated
	*/
	private static boolean constraintsActive = false;

	/**
	* @generated
	*/
	public static boolean shouldConstraintsBePrivate() {
		return false;
	}

	/**
	* @generated
	*/
	public static void runWithConstraints(TransactionalEditingDomain editingDomain, Runnable operation) {
		final Runnable op = operation;
		Runnable task = new Runnable() {
			public void run() {
				try {
					constraintsActive = true;
					op.run();
				} finally {
					constraintsActive = false;
				}
			}
		};
		if (editingDomain != null) {
			try {
				editingDomain.runExclusive(task);
			} catch (Exception e) {
				GfcDiagramEditorPlugin.getInstance().logError("Validation failed", e);   
			}
		} else {
			task.run();
		}
	}

	/**
	* @generated
	*/
	static boolean isInDefaultEditorContext(Object object) {
		if (shouldConstraintsBePrivate() && !constraintsActive) {
			return false;
		}
		if (object instanceof View) {
			return constraintsActive
					&& FlowchartEditPart.MODEL_ID.equals(GfcVisualIDRegistry.getModelID((View) object));
		}
		return true;
	}

}
