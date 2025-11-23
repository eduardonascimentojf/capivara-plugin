package gfc.diagram.actions;

import java.io.FileOutputStream;

import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Ação para exportar o diagrama como imagem PNG.
 */
public class ExportDiagramAsImageAction extends Action {

    private final IDiagramWorkbenchPart diagramPart;

    public ExportDiagramAsImageAction(IDiagramWorkbenchPart part) {
        super("Exportar como PNG");
        this.diagramPart = part;
    }

    @Override
    public void run() {
        if (diagramPart == null)
            return;

        DiagramEditPart diagramEditPart = diagramPart.getDiagramEditPart();
        if (diagramEditPart == null)
            return;

        LayerManager lm = LayerManager.Helper.find(diagramEditPart);
        Layer rootFigure = (Layer) lm.getLayer(LayerConstants.PRINTABLE_LAYERS);

        Rectangle bounds = rootFigure.getBounds();

        Display display = Display.getDefault();
        Image image = new Image(display, bounds.width, bounds.height);
        GC gc = new GC(image);
        gc.setAntialias(SWT.ON);
        gc.setTextAntialias(SWT.ON);

        SWTGraphics swtGraphics = new SWTGraphics(gc);
        swtGraphics.translate(-bounds.x, -bounds.y);
        rootFigure.paint(swtGraphics);

        gc.dispose();

        Shell shell = display.getActiveShell();
        FileDialog dialog = new FileDialog(shell, SWT.SAVE);
        dialog.setText("Salvar diagrama como imagem");
        dialog.setFilterExtensions(new String[] { "*.png" });
        dialog.setFileName("diagrama.png");

        String selected = dialog.open();
        if (selected != null) {
            try (FileOutputStream out = new FileOutputStream(selected)) {
                ImageLoader loader = new ImageLoader();
                loader.data = new ImageData[] { image.getImageData() };
                loader.save(out, SWT.IMAGE_PNG);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        image.dispose();
    }
}
