package gfc.diagram.edit.parts;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITreeBranchEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Color;

import gfc.Edge;
import gfc.Node;
import gfc.diagram.edit.helpers.CoverageHeuristics;
import gfc.diagram.edit.policies.EdgeItemSemanticEditPolicy;
import gfc.diagram.edit.policies.ReadOnlyComponentEditPolicy;

/**
 * EditPart para as arestas (Edges) do diagrama GFC.
 * Centraliza a lógica de cores e scores através do CoverageHeuristics.
 */
public class EdgeEditPart extends ConnectionNodeEditPart implements ITreeBranchEditPart {

	public static final int VISUAL_ID = 4001;

	// Cores Padronizadas
	private static final Color COLOR_GREEN_EXEC = new Color(null, 0, 153, 0);
	private static final Color COLOR_RED_MISSED = new Color(null, 255, 0, 0);
	private static final Color COLOR_DEFAULT = new Color(null, 0, 0, 0);

	// espessura da linha
	private static final int LINE_WIDTH = 2;

	public EdgeEditPart(View view) {
		super(view);
	}

	@Override
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new EdgeItemSemanticEditPolicy());
		installEditPolicy(org.eclipse.gef.EditPolicy.COMPONENT_ROLE, new ReadOnlyComponentEditPolicy());
		removeEditPolicy(EditPolicyRoles.CONNECTION_HANDLES_ROLE);
		removeEditPolicy(org.eclipse.gef.EditPolicy.CONNECTION_BENDPOINTS_ROLE);
		removeEditPolicy(org.eclipse.gef.EditPolicy.CONNECTION_ENDPOINTS_ROLE);
	}

	@Override
	protected Connection createConnectionFigure() {
		return new EdgeFigure();
	}

	public EdgeFigure getPrimaryShape() {
		return (EdgeFigure) getFigure();
	}

	@Override
	protected void refreshVisuals() {
	    super.refreshVisuals();

	    if (!isActive() || getDiagramEditDomain() == null) return;

	    refreshTooltip();

	    IFigure fig = getFigure();
	    if (!(fig instanceof PolylineConnectionEx)) return;
	    PolylineConnectionEx polyline = (PolylineConnectionEx) fig;

	    Object semantic = resolveSemanticElement();
	    if (!(semantic instanceof Edge)) return;
	    Edge edge = (Edge) semantic;

	    polyline.setLineWidth(LINE_WIDTH);

	    if (FlowchartEditPart.SHOW_NODE_COVERAGE) {
	        polyline.setForegroundColor(COLOR_DEFAULT);
	    } else {
	        polyline.setForegroundColor(computeEdgeCoverageColor(edge));
	    }
	}

	private Color computeEdgeCoverageColor(Edge edge) {
	    Node source = edge.getSource();
	    Node target = edge.getTarget();
	    if (source == null || target == null) return COLOR_DEFAULT;
	    if (source.getCoverageStatus() == 0) return COLOR_DEFAULT;

	    // Navega no Flowchart 
	    org.eclipse.emf.ecore.EObject semantic = resolveSemanticElement();
	    if (semantic == null) return COLOR_DEFAULT;

	    org.eclipse.emf.ecore.EObject root = semantic.eContainer();
	    if (!(root instanceof gfc.Flowchart)) return COLOR_DEFAULT;

	    boolean covered = CoverageHeuristics.isEdgeCovered(edge, (gfc.Flowchart) root);
	    return covered ? COLOR_GREEN_EXEC : COLOR_RED_MISSED;
	}

	@Override
	protected void handleNotificationEvent(Notification notification) {
	    if (!isActive() || getDiagramEditDomain() == null) return;
	    super.handleNotificationEvent(notification);
	    refreshTooltip();
	    refreshVisuals();
	}

	protected void refreshTooltip() {
	    if (!isActive() || getDiagramEditDomain() == null) return;

	    IFigure figure = getFigure();
	    if (figure == null) return;

	    Object modelElement = resolveSemanticElement();
	    if (!(modelElement instanceof Edge)) return;

	    Edge edge = (Edge) modelElement;
	    String tooltipText = edge.getLabel();
	    if (tooltipText != null && !tooltipText.isEmpty()) {
	        figure.setToolTip(new Label(tooltipText));
	    } else {
	        figure.setToolTip(null);
	    }
	}
	public class EdgeFigure extends PolylineConnectionEx {
		public EdgeFigure() {
			this.setLineWidth(LINE_WIDTH);
			this.setForegroundColor(COLOR_DEFAULT);
			setTargetDecoration(createTargetDecoration());
		}

		private RotatableDecoration createTargetDecoration() {
			PolylineDecoration df = new PolylineDecoration();
			df.setScale(7, 3); // Seta
			return df;
		}
	}
}