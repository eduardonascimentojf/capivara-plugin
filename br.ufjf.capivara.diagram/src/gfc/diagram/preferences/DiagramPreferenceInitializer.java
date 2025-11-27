/*
 * */
package gfc.diagram.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants; 

import gfc.diagram.part.GfcDiagramEditorPlugin;

/**
 * @generated
 */
public class DiagramPreferenceInitializer extends AbstractPreferenceInitializer {

	/**
	* @generated NOT
	*/
	public void initializeDefaultPreferences() {
		IPreferenceStore store = getPreferenceStore();
		DiagramGeneralPreferencePage.initDefaults(store);
		DiagramAppearancePreferencePage.initDefaults(store);
		DiagramConnectionsPreferencePage.initDefaults(store);
		DiagramPrintingPreferencePage.initDefaults(store);
		DiagramRulersAndGridPreferencePage.initDefaults(store);
		
		store.setDefault(IPreferenceConstants.PREF_SHOW_POPUP_BARS, false);
	}

	/**
	* @generated
	*/
	protected IPreferenceStore getPreferenceStore() {
		return GfcDiagramEditorPlugin.getInstance().getPreferenceStore();
	}
}