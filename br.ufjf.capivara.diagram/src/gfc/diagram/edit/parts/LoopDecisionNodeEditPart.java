package gfc.diagram.edit.parts;

import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label; // NOVO IMPORT
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.notify.Notification; // NOVO IMPORT
import org.eclipse.emf.ecore.EcorePackage;
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

import gfc.Node; // NOVO IMPORT
import gfc.diagram.edit.policies.LoopDecisionNodeItemSemanticEditPolicy;
import gfc.diagram.edit.policies.ReadOnlyComponentEditPolicy;
import gfc.diagram.part.GfcVisualIDRegistry;

public class LoopDecisionNodeEditPart extends ShapeNodeEditPart {

	public static final int VISUAL_ID = 2004;
	protected IFigure contentPane;
	protected IFigure primaryShape;
	private WrappingLabel fFigureLoopDecisionNodeLabelFigure;

	public LoopDecisionNodeEditPart(View view) {
		super(view);
	}

	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new LoopDecisionNodeItemSemanticEditPolicy());
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
		ellipse.setForegroundColor(new Color(null, 255, 165, 0));
		ellipse.setLineWidth(2);
		ellipse.setPreferredSize(new Dimension(getMapMode().DPtoLP(40), getMapMode().DPtoLP(40)));

		GridLayout gl = new GridLayout(1, false);
		gl.horizontalSpacing = 0;
		gl.verticalSpacing = 0;
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		ellipse.setLayoutManager(gl);

		ellipse.setBorder(new MarginBorder(2, 2, 2, 2));

		fFigureLoopDecisionNodeLabelFigure = new WrappingLabel();
		fFigureLoopDecisionNodeLabelFigure.setText("");
		fFigureLoopDecisionNodeLabelFigure.setTextAlignment(PositionConstants.CENTER);
		fFigureLoopDecisionNodeLabelFigure.setAlignment(PositionConstants.CENTER);
		fFigureLoopDecisionNodeLabelFigure.setPreferredSize(-1, -1);

		GridData gd = new GridData(GridData.CENTER, GridData.CENTER, true, true);
		gd.widthHint = -1;
		gd.heightHint = -1;

		ellipse.add(fFigureLoopDecisionNodeLabelFigure);
		ellipse.setConstraint(fFigureLoopDecisionNodeLabelFigure, gd);

		primaryShape = ellipse;
		return primaryShape;
	}

	public IFigure getPrimaryShape() {
		return primaryShape;
	}

	protected boolean addFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof LoopDecisionNodeIdEditPart) {
			((LoopDecisionNodeIdEditPart) childEditPart).setLabel(fFigureLoopDecisionNodeLabelFigure);
			return true;
		}
		return false;
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
		if (notification.getFeature() instanceof org.eclipse.emf.ecore.EAttribute
				&& ((org.eclipse.emf.ecore.EAttribute) notification.getFeature()).getName().equals("label")) {
			refreshTooltip();
		}
	}

	/**
	* @generated
	*/
	public class LoopDecisionNodeFigure extends Ellipse {

		/**
		 * @generated
		 */
		private WrappingLabel fFigureLoopDecisionNodeLabelFigure;

		/**
		 * @generated
		 */
		public LoopDecisionNodeFigure() {
			this.setForegroundColor(THIS_FORE);
			this.setPreferredSize(new Dimension(getMapMode().DPtoLP(40), getMapMode().DPtoLP(40)));
			this.setBorder(new MarginBorder(getMapMode().DPtoLP(5), getMapMode().DPtoLP(5), getMapMode().DPtoLP(5),
					getMapMode().DPtoLP(5)));
			createContents();
		}

		/**
		 * @generated
		 */
		private void createContents() {

			fFigureLoopDecisionNodeLabelFigure = new WrappingLabel();

			fFigureLoopDecisionNodeLabelFigure.setText("LoopDecisionNode");

			this.add(fFigureLoopDecisionNodeLabelFigure);

		}

		/**
		 * @generated
		 */
		public WrappingLabel getFigureLoopDecisionNodeLabelFigure() {
			return fFigureLoopDecisionNodeLabelFigure;
		}

	}

	/**
	* @generated
	*/
	static final Color THIS_FORE = new Color(null, 255, 165, 0);

	protected void refreshTooltip() {
		IFigure figure = getFigure();
		if (figure == null) {
			return;
		}

		Object modelElement = resolveSemanticElement();
		if (modelElement instanceof Node) {
			Node node = (Node) modelElement;
			String tooltipText = node.getLabel();

			if (tooltipText != null && !tooltipText.isEmpty()) {
				figure.setToolTip(new Label(tooltipText));
			} else {
				figure.setToolTip(null);
			}
		}
	}

	// --- FIM DO CÓDIGO ADICIONADO PARA O TOOLTIP ---

	protected boolean removeFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof LoopDecisionNodeIdEditPart) {
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
		DefaultSizeNodeFigure result = new DefaultSizeNodeFigure(40, 40);
		return result;
	}

	protected NodeFigure createNodeFigure() {
		NodeFigure figure = createNodePlate();
		figure.setLayoutManager(new StackLayout());
		IFigure shape = createNodeShape();
		figure.add(shape);
		contentPane = setupContentPane(shape);
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
		if (contentPane != null) {
			return contentPane;
		}
		return super.getContentPane();
	}

	protected void setForegroundColor(Color color) {
		if (primaryShape != null) {
			primaryShape.setForegroundColor(color);
		}
	}

	protected void setBackgroundColor(Color color) {
		if (primaryShape != null) {
			primaryShape.setBackgroundColor(color);
		}
	}

	protected void setLineWidth(int width) {
		if (primaryShape instanceof Shape) {
			((Shape) primaryShape).setLineWidth(width);
		}
	}

	protected void setLineType(int style) {
		if (primaryShape instanceof Shape) {
			((Shape) primaryShape).setLineStyle(style);
		}
	}

	public EditPart getPrimaryChildEditPart() {
		return getChildBySemanticHint(GfcVisualIDRegistry.getType(LoopDecisionNodeIdEditPart.VISUAL_ID));
	}
}