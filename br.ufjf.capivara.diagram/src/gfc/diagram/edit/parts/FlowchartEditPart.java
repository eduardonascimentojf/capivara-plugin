package gfc.diagram.edit.parts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DiagramDragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tooling.runtime.edit.policies.reparent.CreationEditPolicyWithCustomReparent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

import gfc.Flowchart;
import gfc.Node;
import gfc.diagram.edit.commands.GfcCreateShortcutDecorationsCommand;
import gfc.diagram.edit.helpers.CoverageHeuristics;
import gfc.diagram.edit.policies.FlowchartCanonicalEditPolicy;
import gfc.diagram.edit.policies.FlowchartItemSemanticEditPolicy;
import gfc.diagram.part.GfcVisualIDRegistry;

public class FlowchartEditPart extends DiagramEditPart {

    public static final int VISUAL_ID = 1000;
    public static final String MODEL_ID = "Gfc";
    public static boolean SHOW_NODE_COVERAGE = true;

    private LegendFigure legendFigure;
    private FigureListener layoutListener;

    public FlowchartEditPart(View view) {
        super(view);
    }

    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new FlowchartItemSemanticEditPolicy());
        installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, new FlowchartCanonicalEditPolicy());
        installEditPolicy(EditPolicyRoles.CREATION_ROLE,
                new CreationEditPolicyWithCustomReparent(GfcVisualIDRegistry.TYPED_INSTANCE));

        installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new DiagramDragDropEditPolicy() {
            public Command getDropObjectsCommand(DropObjectsRequest dropRequest) {
                ArrayList<CreateViewRequest.ViewDescriptor> viewDescriptors = new ArrayList<CreateViewRequest.ViewDescriptor>();
                for (Iterator<?> it = dropRequest.getObjects().iterator(); it.hasNext();) {
                    Object nextObject = it.next();
                    if (false == nextObject instanceof EObject) {
                        continue;
                    }
                    viewDescriptors.add(new CreateViewRequest.ViewDescriptor(new EObjectAdapter((EObject) nextObject),
                            org.eclipse.gmf.runtime.notation.Node.class, null, getDiagramPreferencesHint()));
                }
                return createShortcutsCommand(dropRequest, viewDescriptors);
            }

            private Command createShortcutsCommand(DropObjectsRequest dropRequest,
                    List<CreateViewRequest.ViewDescriptor> viewDescriptors) {
                Command command = createViewsAndArrangeCommand(dropRequest, viewDescriptors);
                if (command != null) {
                    return command.chain(new ICommandProxy(new GfcCreateShortcutDecorationsCommand(getEditingDomain(),
                            (View) getModel(), viewDescriptors)));
                }
                return null;
            }
        });
        removeEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.POPUPBAR_ROLE);
    }

    @Override
    protected IFigure createFigure() {
        IFigure fig = super.createFigure();
        fig.setOpaque(true);
        fig.setBackgroundColor(ColorConstants.white);
        return fig;
    }

    @Override
    public void activate() {
        super.activate();
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                if (isActive()) {
                    createLegend();
                }
            }
        });
    }

    @Override
    public void deactivate() {
        removeLegend();
        super.deactivate();
    }

    @Override
    protected void refreshVisuals() {
        super.refreshVisuals();
        if (!isActive()) return;
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                if (!isActive() || getDiagramEditDomain() == null) return;
                if (legendFigure != null) {
                    legendFigure.refreshContent();
                    Viewport viewport = findViewport(getFigure());
                    repositionLegend(viewport);
                }
            }
        });
    }

    private void createLegend() {
        if (legendFigure == null && shouldShowLegend()) {
            legendFigure = new LegendFigure();
            getLayer(LayerConstants.FEEDBACK_LAYER).add(legendFigure);
            final Viewport viewport = findViewport(getFigure());
            if (viewport != null) {
                layoutListener = new FigureListener() {
                    @Override
                    public void figureMoved(IFigure source) {
                        repositionLegend(viewport);
                    }
                };
                viewport.addFigureListener(layoutListener);
                repositionLegend(viewport);
            }
        }
    }

    private boolean shouldShowLegend() {
        if (!isActive() || getDiagramEditDomain() == null) return false;
        try {
            EObject element = resolveSemanticElement();
            if (element instanceof Flowchart) {
                Flowchart flowchart = (Flowchart) element;
                for (Node node : flowchart.getNodes()) {
                    if (node.getCoverageStatus() != 0) return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private void removeLegend() {
        if (legendFigure != null) {
            legendFigure.disposeFontsAndColors(); 
            getLayer(LayerConstants.FEEDBACK_LAYER).remove(legendFigure);
            Viewport viewport = findViewport(getFigure());
            if (viewport != null && layoutListener != null) {
                viewport.removeFigureListener(layoutListener);
            }
            legendFigure = null;
            layoutListener = null;
        }
    }

    private Viewport findViewport(IFigure figure) {
        IFigure current = figure;
        while (current != null) {
            if (current instanceof Viewport) {
                return (Viewport) current;
            }
            current = current.getParent();
        }
        return null;
    }

    private void repositionLegend(Viewport viewport) {
        if (legendFigure == null || viewport == null)
            return;
        Rectangle area = viewport.getClientArea();
        Dimension legendSize = legendFigure.getPreferredSize();
        int padding = 20;
        int x = area.x + area.width - legendSize.width - padding;
        int y = area.y + area.height - legendSize.height - padding;
        legendFigure.setBounds(new Rectangle(x, y, legendSize.width, legendSize.height));
    }

    private class LegendFigure extends Figure {

        private final List<Font> createdFonts = new ArrayList<>();
        private final List<Color> createdColors = new ArrayList<>();

        public LegendFigure() {
            ToolbarLayout layout = new ToolbarLayout();
            layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
            layout.setStretchMinorAxis(false);
            layout.setSpacing(2);
            setLayoutManager(layout);

            setOpaque(true);
            setBackgroundColor(ColorConstants.white);

            refreshContent();
        }

        public void refreshContent() {
            disposeFontsAndColors();

            removeAll();

            if (SHOW_NODE_COVERAGE) {
                add(createTitle(" Node Coverage "));
                add(createNodeItem(createColor(102, 255, 102), " Covered"));
                add(createNodeItem(createColor(255, 215, 0), " Partial"));
                add(createNodeItem(createColor(255, 82, 82), " Missed"));
                add(createSeparator());
                add(createNodeScoreLabel());
            } else {
                add(createTitle(" Edge Coverage "));
                add(createEdgeItem(createColor(0, 153, 0), " Traversed"));
                add(createEdgeItem(createColor(255, 0, 0), " Missed"));
                add(createSeparator());
                add(createEdgeScoreLabel());
            }
            revalidate();
            repaint();
        }

        private Color createColor(int r, int g, int b) {
            Color c = new Color(null, r, g, b);
            createdColors.add(c);
            return c;
        }

      
        private Font createBoldFont() {
            FontData[] fontData = Display.getDefault().getSystemFont().getFontData();
            if (fontData != null && fontData.length > 0) {
                fontData[0].setStyle(SWT.BOLD);
                Font font = new Font(Display.getDefault(), fontData[0]);
                createdFonts.add(font);
                return font;
            }
            return null;
        }

        public void disposeFontsAndColors() {
            for (Font f : createdFonts) {
                if (f != null && !f.isDisposed()) f.dispose();
            }
            createdFonts.clear();

            for (Color c : createdColors) {
                if (c != null && !c.isDisposed()) c.dispose();
            }
            createdColors.clear();
        }

        private Figure createTitle(String text) {
            Label label = new Label(text);
            label.setForegroundColor(ColorConstants.black);
            Font font = createBoldFont();
            if (font != null) {
                label.setFont(font);
            }
            return label;
        }

        private Figure createNodeItem(Color color, String text) {
            Figure row = new Figure();
            ToolbarLayout rowLayout = new ToolbarLayout(true);
            rowLayout.setSpacing(5);
            row.setLayoutManager(rowLayout);

            Ellipse circle = new Ellipse();
            circle.setSize(12, 12);
            circle.setPreferredSize(12, 12);
            circle.setBackgroundColor(color);
            circle.setForegroundColor(ColorConstants.black);
            circle.setOpaque(true);

            Label lbl = new Label(text);
            lbl.setForegroundColor(ColorConstants.black);

            row.add(circle);
            row.add(lbl);
            return row;
        }

        private Figure createEdgeItem(Color color, String text) {
            Figure row = new Figure();
            ToolbarLayout rowLayout = new ToolbarLayout(true);
            rowLayout.setSpacing(5);
            row.setLayoutManager(rowLayout);

            Figure lineBox = new Figure() {
                @Override
                protected void paintFigure(Graphics g) {
                    g.setForegroundColor(color);
                    g.setLineWidth(3);
                    Rectangle r = getBounds();
                    g.drawLine(r.x + 2, r.y + r.height / 2, r.x + r.width - 2, r.y + r.height / 2);
                }
            };
            lineBox.setPreferredSize(20, 15);

            Label lbl = new Label(text);
            lbl.setForegroundColor(ColorConstants.black);

            row.add(lineBox);
            row.add(lbl);
            return row;
        }

        private Figure createNodeScoreLabel() {
            String scoreText = " Score: N/A";
            try {
                // resolveSemanticElement() é do FlowchartEditPart (classe externa)
                if (!isActive() || getDiagramEditDomain() == null) return createBoldLabel(scoreText);
                EObject element = resolveSemanticElement();
                if (element instanceof Flowchart) {
                    double pct = CoverageHeuristics.calculateNodeScore((Flowchart) element);
                    scoreText = String.format(" Score: %.1f%%", pct);
                }
            } catch (Exception e) {
                scoreText = " Score: Error";
            }
            return createBoldLabel(scoreText);
        }

        private Figure createEdgeScoreLabel() {
            String scoreText = " Score: N/A";
            try {
                if (!isActive() || getDiagramEditDomain() == null) return createBoldLabel(scoreText);
                EObject element = resolveSemanticElement();
                if (element instanceof Flowchart) {
                    double pct = CoverageHeuristics.calculateEdgeScore((Flowchart) element);
                    scoreText = String.format(" Score: %.1f%%", pct);
                }
            } catch (Exception e) {
                scoreText = " Score: Error";
            }
            return createBoldLabel(scoreText);
        }

        private Figure createSeparator() {
            Label l = new Label("");
            l.setPreferredSize(1, 4);
            return l;
        }

        private Label createBoldLabel(String text) {
            Label label = new Label(text);
            label.setForegroundColor(ColorConstants.black);
            Font font = createBoldFont();
            if (font != null) {
                label.setFont(font);
            }
            return label;
        }
    }
}