package gfc.diagram.edit.parts;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure; // NOVO IMPORT
import org.eclipse.draw2d.Label; // NOVO IMPORT
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.emf.common.notify.Notification; // NOVO IMPORT
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITreeBranchEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Color;

import gfc.diagram.edit.policies.EdgeItemSemanticEditPolicy;
import gfc.diagram.edit.policies.ReadOnlyComponentEditPolicy;
import gfc.Edge; // NOVO IMPORT

public class EdgeEditPart extends ConnectionNodeEditPart implements ITreeBranchEditPart {

	public static final int VISUAL_ID = 4001;

	public EdgeEditPart(View view) {
		super(view);
	}

	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new EdgeItemSemanticEditPolicy());
		installEditPolicy(org.eclipse.gef.EditPolicy.COMPONENT_ROLE, new ReadOnlyComponentEditPolicy());
	}

	protected Connection createConnectionFigure() {
		return new EdgeFigure();
	}

	public EdgeFigure getPrimaryShape() {
		return (EdgeFigure) getFigure();
	}

	// --- INÍCIO DO CÓDIGO ADICIONADO PARA O TOOLTIP ---

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		refreshTooltip();
	}

	@Override
	protected void handleNotificationEvent(Notification notification) {
		super.handleNotificationEvent(notification);
		refreshTooltip();
	}

	protected void refreshTooltip() {
		IFigure figure = getFigure();
		if (figure == null) {
			return;
		}

		Object modelElement = resolveSemanticElement();
		if (modelElement instanceof Edge) {
			Edge edge = (Edge) modelElement;
			String tooltipText = edge.getLabel(); // Pega o label da aresta (ex: "true", "false")

			// Mostra o tooltip apenas se o label não for nulo ou vazio
			if (tooltipText != null && !tooltipText.isEmpty()) {
				figure.setToolTip(new Label(tooltipText));
			} else {
				figure.setToolTip(null);
			}
		}
	}

	// --- FIM DO CÓDIGO ADICIONADO PARA O TOOLTIP ---

	public class EdgeFigure extends PolylineConnectionEx {
		public EdgeFigure() {
			this.setLineWidth(2);
			this.setForegroundColor(THIS_FORE);
			setTargetDecoration(createTargetDecoration());
		}

		private RotatableDecoration createTargetDecoration() {
			PolylineDecoration df = new PolylineDecoration();
			return df;
		}
	}

	static final Color THIS_FORE = new Color(null, 0, 0, 0);
}