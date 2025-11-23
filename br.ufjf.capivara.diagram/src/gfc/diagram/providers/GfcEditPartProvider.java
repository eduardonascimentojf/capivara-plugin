/*
 * 
 */
package gfc.diagram.providers;

import org.eclipse.gmf.tooling.runtime.providers.DefaultEditPartProvider;

import gfc.diagram.edit.parts.FlowchartEditPart;
import gfc.diagram.edit.parts.GfcEditPartFactory;
import gfc.diagram.part.GfcVisualIDRegistry;

/**
 * @generated
 */
public class GfcEditPartProvider extends DefaultEditPartProvider {

	/**
	* @generated
	*/
	public GfcEditPartProvider() {
		super(new GfcEditPartFactory(), GfcVisualIDRegistry.TYPED_INSTANCE, FlowchartEditPart.MODEL_ID);
	}

}
