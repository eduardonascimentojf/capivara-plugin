package gfc.diagram.actions;

import java.io.FileOutputStream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

public class ExportDiagramAsImageAction extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {

        IEditorPart editor = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow()
                .getActivePage()
                .getActiveEditor();

        if (!(editor instanceof IDiagramWorkbenchPart))
            return null;

        IDiagramWorkbenchPart diagramPart = (IDiagramWorkbenchPart) editor;
        DiagramEditPart diagramEditPart = diagramPart.getDiagramEditPart();

        if (diagramEditPart == null)
            return null;

        LayerManager lm = LayerManager.Helper.find(diagramEditPart);

        Layer printableLayer = (Layer) lm.getLayer(LayerConstants.PRINTABLE_LAYERS);
        Layer feedbackLayer = (Layer) lm.getLayer(LayerConstants.FEEDBACK_LAYER);


        Rectangle diagramBounds = null;

        for (Object obj : diagramEditPart.getChildren()) {
            if (obj instanceof GraphicalEditPart part) {
                IFigure fig = part.getFigure();
                Rectangle r = fig.getBounds().getCopy();
                fig.translateToAbsolute(r);

                if (diagramBounds == null)
                    diagramBounds = r;
                else
                    diagramBounds.union(r);
            }
        }

        if (diagramBounds == null) {
            diagramBounds = diagramEditPart.getContentPane().getBounds().getCopy();
        }

  
        IFigure legendFigure = null;
        if (feedbackLayer != null && !feedbackLayer.getChildren().isEmpty()) {
            legendFigure = (IFigure) feedbackLayer.getChildren().get(0);
        }

        Dimension legendSize = legendFigure != null
                ? legendFigure.getPreferredSize()
                : new Dimension(0, 0);



        int gap = 40;      
        int padding = 30;  

        int finalWidth = diagramBounds.width + legendSize.width + gap + padding * 2;
        int finalHeight = Math.max(diagramBounds.height, legendSize.height) + padding * 2;



        Display display = Display.getDefault();
        Image image = new Image(display, finalWidth, finalHeight);

        GC gc = new GC(image);
        gc.setAntialias(SWT.ON);
        gc.setTextAntialias(SWT.ON);

        SWTGraphics graphics = new SWTGraphics(gc);


        graphics.translate(-diagramBounds.x + padding,-diagramBounds.y + padding);

        printableLayer.paint(graphics);


        if (legendFigure != null) {

            Rectangle legendBounds = new Rectangle(diagramBounds.width + gap + padding, padding, legendSize.width, legendSize.height);
            legendFigure.setBounds(legendBounds);
            legendFigure.paint(graphics);
        }

        gc.dispose();


        Shell shell = display.getActiveShell();
        FileDialog dialog = new FileDialog(shell, SWT.SAVE);
        dialog.setText("Salvar diagrama como imagem");
        dialog.setFilterExtensions(new String[] { "*.png" });
        dialog.setFileName("diagrama.png");

        String path = dialog.open();

        if (path != null) {
            try (FileOutputStream out = new FileOutputStream(path)) {
                ImageLoader loader = new ImageLoader();
                loader.data = new ImageData[] { image.getImageData() };
                loader.save(out, SWT.IMAGE_PNG);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        image.dispose();

        return null;
    }
}