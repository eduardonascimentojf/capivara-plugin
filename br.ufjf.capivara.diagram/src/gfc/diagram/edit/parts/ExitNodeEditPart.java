package gfc.diagram.edit.parts;

import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label; // usado para tooltip simples
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.graphics.Color;

import gfc.Node;
import gfc.diagram.edit.policies.ExitNodeItemSemanticEditPolicy;
import gfc.diagram.edit.policies.ReadOnlyComponentEditPolicy;
import gfc.diagram.figures.DoubleEllipseFigure;
import gfc.diagram.part.GfcVisualIDRegistry;

/**
 * ExitNodeEditPart com tooltip seguro (mostrando o label do modelo).
 */
public class ExitNodeEditPart extends ShapeNodeEditPart {

    public static final int VISUAL_ID = 2005;
    protected IFigure contentPane;
    protected IFigure primaryShape;
    private WrappingLabel fFigureExitNodeLabelFigure;

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

    /**
     * Cria a figura usando DoubleEllipseFigure (sua figura customizada).
     */
    protected IFigure createNodeShape() {
        DoubleEllipseFigure ellipse = new DoubleEllipseFigure();
        ellipse.setPreferredSize(new Dimension(getMapMode().DPtoLP(40), getMapMode().DPtoLP(40)));

        GridLayout gl = new GridLayout(1, false);
        gl.marginHeight = 0;
        gl.marginWidth = 0;
        gl.horizontalSpacing = 0;
        gl.verticalSpacing = 0;
        ellipse.setLayoutManager(gl);

        ellipse.setBorder(new MarginBorder(2, 2, 2, 2));

        fFigureExitNodeLabelFigure = new WrappingLabel();
        fFigureExitNodeLabelFigure.setText("");
        fFigureExitNodeLabelFigure.setTextAlignment(PositionConstants.CENTER);
        fFigureExitNodeLabelFigure.setAlignment(PositionConstants.CENTER);
        fFigureExitNodeLabelFigure.setPreferredSize(-1, -1);

        GridData gd = new GridData(GridData.CENTER, GridData.CENTER, true, true);
        ellipse.add(fFigureExitNodeLabelFigure);
        ellipse.setConstraint(fFigureExitNodeLabelFigure, gd);

        primaryShape = ellipse;
        return primaryShape;
    }

    public IFigure getPrimaryShape() {
        return primaryShape;
    }

    /**
     * Conecta o EditPart do label ao WrappingLabel interno.
     */
    protected boolean addFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof ExitNodeIdEditPart) {
            ((ExitNodeIdEditPart) childEditPart).setLabel(fFigureExitNodeLabelFigure);
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
        DefaultSizeNodeFigure result = new DefaultSizeNodeFigure(40, 40);
        return result;
    }

    protected NodeFigure createNodeFigure() {
        NodeFigure figure = createNodePlate();
        figure.setLayoutManager(new StackLayout());
        IFigure shape = createNodeShape();
        figure.add(shape);
        contentPane = setupContentPane(shape);

        // instala tooltip inicial (refreshVisuals e handleNotificationEvent também cuidam disso)
        refreshTooltip();

        return figure;
    }

    /**
     * Mantém o comportamento padrão de layout de conteúdo quando não há layout definido.
     */
    protected IFigure setupContentPane(IFigure nodeShape) {
        if (nodeShape.getLayoutManager() == null) {
            ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
            layout.setSpacing(5);
            nodeShape.setLayoutManager(layout);
        }
        return nodeShape; // contentPane é a própria figura (com o label dentro)
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
        return getChildBySemanticHint(GfcVisualIDRegistry.getType(ExitNodeIdEditPart.VISUAL_ID));
    }

    // ---------------- Tooltip seguro ----------------

    @Override
    protected void refreshVisuals() {
        super.refreshVisuals();
        refreshTooltip();
    }

    @Override
    protected void handleNotificationEvent(Notification notification) {
        super.handleNotificationEvent(notification);
        // atualiza tooltip sempre que houver mudanças no modelo
        refreshTooltip();
    }

    /**
     * Atualiza o tooltip da figura de forma segura:
     * - executa na UI thread
     * - usa Label simples
     * - captura IllegalArgumentException para evitar crash do loop de eventos
     */
    protected void refreshTooltip() {
        final IFigure targetFigure = primaryShape != null ? (IFigure) primaryShape : getFigure();
        if (targetFigure == null) {
            return;
        }

        final Object modelElement = resolveSemanticElement();
        final String tooltipText;
        if (modelElement instanceof Node) {
            Node node = (Node) modelElement;
            tooltipText = node.getLabel();
        } else {
            // fallback: texto do WrappingLabel interno (se preenchido)
            String t = null;
            if (fFigureExitNodeLabelFigure != null) {
                t = fFigureExitNodeLabelFigure.getText();
            }
            tooltipText = t;
        }

        Runnable uiJob = new Runnable() {
            @Override
            public void run() {
                try {
                    if (tooltipText != null && !tooltipText.trim().isEmpty()) {
                        // limita comprimento para evitar problemas extremos de renderização
                        String safeText = tooltipText.length() > 2000 ? tooltipText.substring(0, 2000) + "…" : tooltipText;
                        Label tip = new Label(safeText);
                        // define o tooltip na figura primaria (mais próximo da forma)
                        targetFigure.setToolTip(tip);
                    } else {
                        targetFigure.setToolTip(null);
                    }
                } catch (IllegalArgumentException iae) {
                    // proteje o loop de eventos: limpa tooltip se ocorrer erro de fonte/GC
                    try {
                        targetFigure.setToolTip(null);
                    } catch (Throwable ignore) {}
                } catch (Throwable t) {
                    // qualquer outro problema: remove tooltip para não atrapalhar UI
                    try {
                        targetFigure.setToolTip(null);
                    } catch (Throwable ignore) {}
                } finally {
                    // força repaint seguro
                    try {
                        targetFigure.repaint();
                    } catch (Throwable ignore) {}
                }
            }
        };

        if (Display.getCurrent() != null) {
            // já estamos na UI thread
            uiJob.run();
        } else {
            Display.getDefault().asyncExec(uiJob);
        }
    }

    // ---------------- fim tooltip ----------------

}
