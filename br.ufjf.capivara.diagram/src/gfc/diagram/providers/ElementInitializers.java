/*
 * 
 */
package gfc.diagram.providers;

import gfc.diagram.part.GfcDiagramEditorPlugin;

/**
 * @generated
 */
public class ElementInitializers {

	protected ElementInitializers() {
		// use #getInstance to access cached instance
	}

	/**
	* @generated
	*/
	public static ElementInitializers getInstance() {
		ElementInitializers cached = GfcDiagramEditorPlugin.getInstance().getElementInitializers();
		if (cached == null) {
			GfcDiagramEditorPlugin.getInstance().setElementInitializers(cached = new ElementInitializers());
		}
		return cached;
	}
}
