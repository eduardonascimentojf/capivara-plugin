package gfc.diagram.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color; 

public class DoubleEllipseFigure extends Shape {

    private static final int INSET = 4;
    private WrappingLabel fFigureLabel;

    public DoubleEllipseFigure() {
        GridLayout gl = new GridLayout(1, false);
        gl.horizontalSpacing = 0;
        gl.verticalSpacing = 0;
        gl.marginHeight = 0;
        gl.marginWidth = 0;
        this.setLayoutManager(gl);
        this.setBorder(new MarginBorder(2));
        createContents();
    }

    private void createContents() {
        fFigureLabel = new WrappingLabel();
        fFigureLabel.setText(""); 
        fFigureLabel.setTextAlignment(PositionConstants.CENTER);
        fFigureLabel.setAlignment(PositionConstants.CENTER);
        
        this.add(fFigureLabel);
        GridData gd = new GridData(GridData.CENTER, GridData.CENTER, true, true);
        this.setConstraint(fFigureLabel, gd);
    }

    public WrappingLabel getFigureLabel() {
        return fFigureLabel;
    }

    @Override
    protected void fillShape(Graphics graphics) {
        graphics.setAntialias(SWT.ON);
        Color bg = getBackgroundColor();
        if (bg != null) {
            graphics.setBackgroundColor(bg);
        }
        
        Rectangle client = getClientArea().getCopy();
        if (client.width > 0 && client.height > 0) {
            graphics.fillOval(client);
        }
    }

    @Override
    protected void outlineShape(Graphics graphics) {
        graphics.setAntialias(SWT.ON);
        Rectangle client = getClientArea().getCopy();

        if (client.width <= 0 || client.height <= 0) return;

        // circulo de fora
        int outerWidth = 1;
        Rectangle outer = client.getCopy();
        outer.shrink(outerWidth / 2, outerWidth / 2);
        graphics.setLineWidth(outerWidth);
        

        Color fg = getForegroundColor();
        if (fg != null) {
            graphics.setForegroundColor(fg);
        }
        
        if (outer.width > 0 && outer.height > 0) {
            graphics.drawOval(outer);
        }
        // circulo interno
        int innerWidth = 1;
        Rectangle inner = client.getCopy();
        int totalShrink = (outerWidth / 2) + INSET + (innerWidth / 2);
        inner.shrink(totalShrink, totalShrink);
        graphics.setLineWidth(innerWidth);
        
        if (inner.width > 0 && inner.height > 0) {
            graphics.drawOval(inner);
        }
    }
}