/*
* 
*/
package gfc.diagram.navigator;

import org.eclipse.jface.viewers.ViewerSorter;

import gfc.diagram.part.GfcVisualIDRegistry;

/**
 * @generated
 */
public class GfcNavigatorSorter extends ViewerSorter {

	/**
	* @generated
	*/
	private static final int GROUP_CATEGORY = 4003;

	/**
	* @generated
	*/
	private static final int SHORTCUTS_CATEGORY = 4002;

	/**
	* @generated
	*/
	public int category(Object element) {
		if (element instanceof GfcNavigatorItem) {
			GfcNavigatorItem item = (GfcNavigatorItem) element;
			if (item.getView().getEAnnotation("Shortcut") != null) { 
				return SHORTCUTS_CATEGORY;
			}
			return GfcVisualIDRegistry.getVisualID(item.getView());
		}
		return GROUP_CATEGORY;
	}

}
