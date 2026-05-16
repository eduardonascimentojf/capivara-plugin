package gfc.diagram.edit.parts;

import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import gfc.Node;
import gfc.diagram.edit.policies.DecisionNodeItemSemanticEditPolicy;
import gfc.diagram.edit.policies.ReadOnlyComponentEditPolicy;
import gfc.diagram.part.GfcVisualIDRegistry;

public class DecisionNodeEditPart extends ShapeNodeEditPart {

	public static final int VISUAL_ID = 2003;
	protected IFigure contentPane;
	protected IFigure primaryShape;
	private WrappingLabel fFigureDecisionNodeLabelFigure;

	public DecisionNodeEditPart(View view) {
		super(view);
	}

	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new DecisionNodeItemSemanticEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
		installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new NonResizableEditPolicy());
		installEditPolicy(org.eclipse.gef.EditPolicy.COMPONENT_ROLE, new ReadOnlyComponentEditPolicy());
	}

	protected LayoutEditPolicy createLayoutEditPolicy() {
		org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy lep = new org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy() {
			protected EditPolicy createChildEditPolicy(EditPart child) {
				EditPolicy result = child.getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
				if (result == null) {
					result = new NonResizableEditPolicy();
				}
				return result;
			}

			protected Command getMoveChildrenCommand(Request request) {
				return null;
			}

			protected Command getCreateCommand(CreateRequest request) {
				return null;
			}
		};
		return lep;
	}

	protected IFigure createNodeShape() {
		Ellipse ellipse = new Ellipse();
		ellipse.setForegroundColor(new Color(null, 0, 123, 255)); // Azul
		ellipse.setBackgroundColor(new Color(null, 255, 255, 255)); // Branco
		ellipse.setLineWidth(2);

		ellipse.setPreferredSize(new Dimension(40, 40));

		GridLayout gl = new GridLayout(1, false);
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		gl.horizontalSpacing = 0;
		gl.verticalSpacing = 0;
		ellipse.setLayoutManager(gl);
		ellipse.setBorder(new MarginBorder(2, 2, 2, 2));

		fFigureDecisionNodeLabelFigure = new WrappingLabel();
		fFigureDecisionNodeLabelFigure.setText("");
		fFigureDecisionNodeLabelFigure.setTextAlignment(PositionConstants.CENTER);
		fFigureDecisionNodeLabelFigure.setAlignment(PositionConstants.CENTER);

		GridData gd = new GridData(GridData.CENTER, GridData.CENTER, true, true);
		gd.widthHint = -1;
		gd.heightHint = -1;

		ellipse.add(fFigureDecisionNodeLabelFigure);
		ellipse.setConstraint(fFigureDecisionNodeLabelFigure, gd);

		primaryShape = ellipse;
		return primaryShape;
	}

	@Override
	public void activate() {
		super.activate();
		Display.getDefault().asyncExec(() -> {
			if (isActive())
				refreshVisuals();
		});
	}

	public IFigure getPrimaryShape() {
		return primaryShape;
	}

	protected boolean addFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof DecisionNodeIdEditPart) {
			((DecisionNodeIdEditPart) childEditPart).setLabel(fFigureDecisionNodeLabelFigure);
			return true;
		}
		return false;
	}

	protected boolean removeFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof DecisionNodeIdEditPart) {
			return true;
		}
		return false;
	}

	protected void addChildVisual(EditPart childEditPart, int index) {
		if (addFixedChild(childEditPart)) {
			return;
		}
		super.addChildVisual(childEditPart, -1);
	}

	protected void removeChildVisual(EditPart childEditPart) {
		if (removeFixedChild(childEditPart)) {
			return;
		}
		super.removeChildVisual(childEditPart);
	}

	protected IFigure getContentPaneFor(IGraphicalEditPart editPart) {
		return getContentPane();
	}

	protected NodeFigure createNodePlate() {
		return new DefaultSizeNodeFigure(40, 40);
	}

	protected NodeFigure createNodeFigure() {
		NodeFigure figure = createNodePlate();
		figure.setLayoutManager(new StackLayout());
		IFigure shape = createNodeShape();
		figure.add(shape);
		contentPane = setupContentPane(shape);
		refreshTooltip();
		return figure;
	}

	protected IFigure setupContentPane(IFigure nodeShape) {
		if (nodeShape.getLayoutManager() == null) {
			ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
			layout.setSpacing(5);
			nodeShape.setLayoutManager(layout);
		}
		return nodeShape;
	}

	public IFigure getContentPane() {
		if (contentPane != null)
			return contentPane;
		return super.getContentPane();
	}

	public EditPart getPrimaryChildEditPart() {
		return getChildBySemanticHint(GfcVisualIDRegistry.getType(DecisionNodeIdEditPart.VISUAL_ID));
	}

	@Override
	protected void refreshVisuals() {
		if (!isActive() || getFigure() == null || primaryShape == null)
			return;
		super.refreshVisuals();

		Node node = (Node) resolveSemanticElement();
		if (node != null) {
			int status = node.getCoverageStatus();

			// Cores Padrão
			Color cor = new Color(null, 255, 255, 255); // Branco

			switch (status) {
			case 1:
				cor = new Color(null, 102, 255, 102);
				break; // Verde
			case 2:
				cor = new Color(null, 255, 215, 0);
				break; // Amarelo
			case 3:
				cor = new Color(null, 255, 82, 82);
				break; // Vermelho
			}

			primaryShape.setBackgroundColor(cor);
		}
		refreshTooltip();
	}

	@Override
	protected void handleNotificationEvent(Notification event) {
		if (gfc.GfcPackage.eINSTANCE.getNode_CoverageStatus().equals(event.getFeature())) {
			Display.getDefault().asyncExec(() -> {
				if (isActive())
					refreshVisuals();
			});
		}
		super.handleNotificationEvent(event);
		refreshTooltip();
	}

	protected void refreshTooltip() {
		final IFigure targetFigure = getPrimaryShape();
		if (targetFigure == null)
			return;
		final Node node = (Node) resolveSemanticElement();
		final String text = (node != null) ? node.getLabel() : null;

		Display.getDefault().asyncExec(() -> {
			try {
				if (targetFigure.getParent() != null) {
					if (text != null && !text.isEmpty()) {
						targetFigure.setToolTip(new Label(text));
					} else {
						targetFigure.setToolTip(null);
					}
				}
			} catch (Exception e) {
			}
		});
	}

	protected void setForegroundColor(Color color) {
		if (primaryShape != null)
			primaryShape.setForegroundColor(color);
	}

	protected void setBackgroundColor(Color color) {
		if (primaryShape != null)
			primaryShape.setBackgroundColor(color);
	}

	protected void setLineWidth(int width) {
		if (primaryShape instanceof Shape)
			((Shape) primaryShape).setLineWidth(width);
	}

	protected void setLineType(int style) {
		if (primaryShape instanceof Shape)
			((Shape) primaryShape).setLineStyle(style);
	}

	public class DecisionNodeFigure extends Ellipse {
		private WrappingLabel fFigureDecisionNodeLabelFigure;

		public DecisionNodeFigure() {
		}

		public WrappingLabel getFigureDecisionNodeLabelFigure() {
			return fFigureDecisionNodeLabelFigure;
		}
	}

	/**
	* @generated
	*/
	static final Color THIS_FORE = new Color(null, 0, 123, 255);
}