package gfc.diagram.edit.parts;

import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
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
import gfc.diagram.edit.policies.EntryNodeItemSemanticEditPolicy;
import gfc.diagram.edit.policies.ReadOnlyComponentEditPolicy;
import gfc.diagram.part.GfcVisualIDRegistry;

public class EntryNodeEditPart extends ShapeNodeEditPart {

	public static final int VISUAL_ID = 2001;
	protected IFigure contentPane;
	protected IFigure primaryShape;
	public EntryNodeEditPart(View view) {
		super(view);
	}

	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new EntryNodeItemSemanticEditPolicy());
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
		EntryNodeFigure ellipse = new EntryNodeFigure();
		primaryShape = ellipse;
		return primaryShape;
	}

	public EntryNodeFigure getPrimaryShape() {
		return (EntryNodeFigure) primaryShape;
	}

	protected boolean addFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof EntryNodeIdEditPart) {
			((EntryNodeIdEditPart) childEditPart).setLabel(getPrimaryShape().getFigureEntryNodeLabelFigure());
			return true;
		}
		return false;
	}

	protected boolean removeFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof EntryNodeIdEditPart) {
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

	public EditPart getPrimaryChildEditPart() {
		return getChildBySemanticHint(GfcVisualIDRegistry.getType(EntryNodeIdEditPart.VISUAL_ID));
	}



	@Override
	public void activate() {
		super.activate();
		Display.getDefault().asyncExec(() -> {
			if (isActive()) refreshVisuals();
		});
	}

	@Override
	protected void refreshVisuals() {
		if (!isActive() || getFigure() == null || primaryShape == null) return;
		super.refreshVisuals();

		Object model = resolveSemanticElement();
		if (model instanceof Node) {
			Node node = (Node) model;
			int status = node.getCoverageStatus();

			Color cor = new Color(null, 255, 255, 255); // Padrão Branco
			
			switch (status) {
				case 1: cor = new Color(null, 102, 255, 102); break; // Verde
				case 2: cor = new Color(null, 255, 215, 0); break;   // Amarelo
				case 3: cor = new Color(null, 255, 82, 82); break;   // Vermelho
			}
			primaryShape.setBackgroundColor(cor);
		}
		refreshTooltip();
	}

	@Override
	protected void handleNotificationEvent(Notification event) {
		if (gfc.GfcPackage.eINSTANCE.getNode_CoverageStatus().equals(event.getFeature())) {
			Display.getDefault().asyncExec(() -> {
				if (isActive()) refreshVisuals();
			});
		}
		super.handleNotificationEvent(event);
		refreshTooltip();
	}

	protected void refreshTooltip() {
		IFigure figure = getFigure();
		if (figure == null) return;

		Object modelElement = resolveSemanticElement();
		if (modelElement instanceof Node) {
			Node node = (Node) modelElement;
			String tooltipText = node.getLabel(); 
			
			Display.getDefault().asyncExec(() -> {
				try {
					if (figure.getParent() != null) {
						if (tooltipText != null && !tooltipText.isEmpty()) {
							figure.setToolTip(new Label(tooltipText));
						} else {
							figure.setToolTip(null);
						}
					}
				} catch (Exception e) {}
			});
		}
	}

	
	public class EntryNodeFigure extends Ellipse {
		private WrappingLabel fFigureEntryNodeLabelFigure;

		public EntryNodeFigure() {
			this.setForegroundColor(THIS_FORE);
			this.setPreferredSize(new Dimension(getMapMode().DPtoLP(40), getMapMode().DPtoLP(40)));
			this.setSize(40, 40);
			this.setMinimumSize(new Dimension(40, 40));
			this.setBorder(new MarginBorder(getMapMode().DPtoLP(2), getMapMode().DPtoLP(2), getMapMode().DPtoLP(2), getMapMode().DPtoLP(2)));
			createContents();
		}

		private void createContents() {
			fFigureEntryNodeLabelFigure = new WrappingLabel();
			fFigureEntryNodeLabelFigure.setText("");
			fFigureEntryNodeLabelFigure.setTextAlignment(PositionConstants.CENTER);
			fFigureEntryNodeLabelFigure.setAlignment(PositionConstants.CENTER);
			
			this.setLayoutManager(new GridLayout());
			GridData gd = new GridData(GridData.CENTER, GridData.CENTER, true, true);
			this.add(fFigureEntryNodeLabelFigure, gd);
		}

		public WrappingLabel getFigureEntryNodeLabelFigure() {
			return fFigureEntryNodeLabelFigure;
		}
	}

	static final Color THIS_FORE = new Color(null, 40, 167, 69);
}