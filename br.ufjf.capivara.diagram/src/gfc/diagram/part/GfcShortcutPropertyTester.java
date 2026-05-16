/*
* 
*/
package gfc.diagram.part;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.gmf.runtime.notation.View;

import gfc.diagram.edit.parts.FlowchartEditPart;

/**
 * @generated
 */
public class GfcShortcutPropertyTester extends PropertyTester {

	/**
	* @generated
	*/
	protected static final String SHORTCUT_PROPERTY = "isShortcut";   

	/**
	* @generated
	*/
	public boolean test(Object receiver, String method, Object[] args, Object expectedValue) {
		if (false == receiver instanceof View) {
			return false;
		}
		View view = (View) receiver;
		if (SHORTCUT_PROPERTY.equals(method)) {
			EAnnotation annotation = view.getEAnnotation("Shortcut");   
			if (annotation != null) {
				return FlowchartEditPart.MODEL_ID.equals(annotation.getDetails().get("modelID"));   
			}
		}
		return false;
	}

}
