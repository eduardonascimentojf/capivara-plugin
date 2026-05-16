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
import org.eclipse.swt.widgets.Display;

import gfc.Node;
import gfc.diagram.edit.policies.LoopDecisionNodeItemSemanticEditPolicy;
import gfc.diagram.edit.policies.ReadOnlyComponentEditPolicy;
import gfc.diagram.part.GfcVisualIDRegistry;

public class LoopDecisionNodeEditPart extends ShapeNodeEditPart {

	public static final int VISUAL_ID = 2004;
	protected IFigure contentPane;
	protected IFigure primaryShape;

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
		LoopDecisionNodeFigure ellipse = new LoopDecisionNodeFigure();
		
		ellipse.setPreferredSize(new Dimension(40, 40));
		ellipse.setMinimumSize(new Dimension(40, 40));
		ellipse.setSize(40, 40);
		ellipse.setOpaque(true);

		// Cores Padrão
		ellipse.setForegroundColor(new Color(null, 255, 165, 0)); // Laranja (Loop)
		ellipse.setBackgroundColor(new Color(null, 255, 255, 255)); // Branco
		ellipse.setLineWidth(2);

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

	public LoopDecisionNodeFigure getPrimaryShape() {
		return (LoopDecisionNodeFigure) primaryShape;
	}

	protected boolean addFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof LoopDecisionNodeIdEditPart) {
			((LoopDecisionNodeIdEditPart) childEditPart).setLabel(getPrimaryShape().getFigureLoopDecisionNodeLabelFigure());
			return true;
		}
		return false;
	}

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
		if (contentPane != null) {
			return contentPane;
		}
		return super.getContentPane();
	}

	@Override
	protected void refreshVisuals() {
		if (!isActive() || getFigure() == null || primaryShape == null)
			return;
		super.refreshVisuals();

		Node node = (Node) resolveSemanticElement();
		if (node != null) {
			int status = node.getCoverageStatus();

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
			if (getPrimaryShape() != null && getPrimaryShape().getFigureLoopDecisionNodeLabelFigure() != null) {
				getPrimaryShape().getFigureLoopDecisionNodeLabelFigure().setText(String.valueOf(node.getId()));
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
		
		if (event.getNotifier() == getModel()
				&& EcorePackage.eINSTANCE.getEModelElement_EAnnotations().equals(event.getFeature())) {
			handleMajorSemanticChange();
		} else {
			super.handleNotificationEvent(event);
		}
		
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

	public class LoopDecisionNodeFigure extends Ellipse {
		private WrappingLabel fFigureLoopDecisionNodeLabelFigure;

		public LoopDecisionNodeFigure() {
			// Layout para centralizar o texto
			GridLayout layoutThis = new GridLayout(1, false);
			layoutThis.marginHeight = 0;
			layoutThis.marginWidth = 0;
			layoutThis.horizontalSpacing = 0;
			layoutThis.verticalSpacing = 0;
			this.setLayoutManager(layoutThis);
			
			this.setForegroundColor(THIS_FORE);
			this.setPreferredSize(new Dimension(getMapMode().DPtoLP(40), getMapMode().DPtoLP(40)));
			this.setBorder(new MarginBorder(getMapMode().DPtoLP(5), getMapMode().DPtoLP(5), getMapMode().DPtoLP(5),
					getMapMode().DPtoLP(5)));
			createContents();
		}

		private void createContents() {
			fFigureLoopDecisionNodeLabelFigure = new WrappingLabel();
			fFigureLoopDecisionNodeLabelFigure.setText("");
			fFigureLoopDecisionNodeLabelFigure.setTextAlignment(PositionConstants.CENTER);
			fFigureLoopDecisionNodeLabelFigure.setAlignment(PositionConstants.CENTER);
			
			this.add(fFigureLoopDecisionNodeLabelFigure, new GridData(GridData.CENTER, GridData.CENTER, true, true));
		}

		public WrappingLabel getFigureLoopDecisionNodeLabelFigure() {
			return fFigureLoopDecisionNodeLabelFigure;
		}
	}

	static final Color THIS_FORE = new Color(null, 255, 165, 0); // Laranja
}