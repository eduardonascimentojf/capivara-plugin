package gfc.diagram.edit.parts;

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
import gfc.diagram.edit.policies.ExitNodeItemSemanticEditPolicy;
import gfc.diagram.edit.policies.ReadOnlyComponentEditPolicy;
import gfc.diagram.figures.DoubleEllipseFigure;
import gfc.diagram.part.GfcVisualIDRegistry;

public class ExitNodeEditPart extends ShapeNodeEditPart {

	public static final int VISUAL_ID = 2005;
	protected IFigure contentPane;
	protected IFigure primaryShape;

	public ExitNodeEditPart(View view) {
		super(view);
	}

	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new ExitNodeItemSemanticEditPolicy());
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
		ExitNodeFigure ellipse = new ExitNodeFigure();

  
		ellipse.setPreferredSize(new Dimension(40, 40));
		ellipse.setMinimumSize(new Dimension(40, 40));
		ellipse.setSize(40, 40);
		ellipse.setOpaque(true);

		// Cores Iniciais
		ellipse.setBackgroundColor(new Color(null, 255, 255, 255)); // Branco
		ellipse.setForegroundColor(new Color(null, 255, 0, 0)); // Borda Vermelha
		GridLayout gl = new GridLayout(1, false);
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		gl.horizontalSpacing = 0;
		gl.verticalSpacing = 0;
		ellipse.setLayoutManager(gl);
		ellipse.setBorder(new MarginBorder(2, 2, 2, 2));

		// label da Figura (circulo duplo)
		WrappingLabel label = ellipse.getFigureExitNodeLabelFigure();
				GridData gd = new GridData(GridData.CENTER, GridData.CENTER, true, true);
		ellipse.setConstraint(label, gd);

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

	public ExitNodeFigure getPrimaryShape() {
		return (ExitNodeFigure) primaryShape;
	}

	protected boolean addFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof ExitNodeIdEditPart) {
			((ExitNodeIdEditPart) childEditPart).setLabel(getPrimaryShape().getFigureExitNodeLabelFigure());
			return true;
		}
		return false;
	}

	protected boolean removeFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof ExitNodeIdEditPart) {
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
		return getChildBySemanticHint(GfcVisualIDRegistry.getType(ExitNodeIdEditPart.VISUAL_ID));
	}

	@Override
	protected void refreshVisuals() {
		if (!isActive() || getFigure() == null || primaryShape == null)
			return;
		super.refreshVisuals();

		Node node = (Node) resolveSemanticElement();
		if (node != null) {
			int status = node.getCoverageStatus();

			Color backColor = null;
			Color foreColor = null;

			switch (status) {
			case 1: // Verde
				backColor = new Color(null, 102, 255, 102);
				foreColor = new Color(null, 255, 0, 0);
				break;
			case 2: // Amarelo
				backColor = new Color(null, 255, 215, 0);
				foreColor = new Color(null, 255, 0, 0);
				break;
			case 3: // Vermelho
				backColor = new Color(null, 255, 82, 82);
				foreColor = new Color(null, 0, 0, 0);
				break;
			default: // Branco
				backColor = new Color(null, 255, 255, 255);
				foreColor = new Color(null, 255, 0, 0);
				break;
			}

			primaryShape.setBackgroundColor(backColor);
			primaryShape.setForegroundColor(foreColor);
			
			// Ajusta o texto para id nao deformar a elipse
			if (getPrimaryShape() != null && getPrimaryShape().getFigureExitNodeLabelFigure() != null) {
				getPrimaryShape().getFigureExitNodeLabelFigure().setText(String.valueOf(node.getId()));
			}
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

	public class ExitNodeFigure extends DoubleEllipseFigure {
		private WrappingLabel fFigureExitNodeLabelFigure;

		public ExitNodeFigure() {
			createContents(); 
		}

		private void createContents() {
			fFigureExitNodeLabelFigure = new WrappingLabel();
			fFigureExitNodeLabelFigure.setText("Exit");
			fFigureExitNodeLabelFigure.setTextAlignment(PositionConstants.CENTER);
			
			this.add(fFigureExitNodeLabelFigure);
		}

		public WrappingLabel getFigureExitNodeLabelFigure() {
			return fFigureExitNodeLabelFigure;
		}
	}
}