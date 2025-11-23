package gfc.diagram.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.swt.SWT;

public class DoubleEllipseFigure extends Shape {

    private static final int INSET = 4;
    private WrappingLabel fFigureLabel;

    public DoubleEllipseFigure() {
        // Usa GridLayout 1x1 para poder centralizar vertical+horizontal com GridData
        GridLayout gl = new GridLayout(1, false);
        gl.horizontalSpacing = 0;
        gl.verticalSpacing = 0;
        gl.marginHeight = 0;
        gl.marginWidth = 0;
        this.setLayoutManager(gl);

        // margem interna para afastar o texto da borda (ajuste se necessário)
        this.setBorder(new MarginBorder(2));

        createContents();
    }

    private void createContents() {
        fFigureLabel = new WrappingLabel();
        fFigureLabel.setText(""); // O GMF/ID edit part vai preencher o texto
        fFigureLabel.setForegroundColor(ColorConstants.black);

        // centraliza o texto dentro do WrappingLabel
        fFigureLabel.setTextAlignment(PositionConstants.CENTER);
        fFigureLabel.setAlignment(PositionConstants.CENTER);
        fFigureLabel.setPreferredSize(-1, -1);

        // adiciona e registra constraint (GridData) para centralizar vertical e horizontalmente
        this.add(fFigureLabel);
        GridData gd = new GridData(GridData.CENTER, GridData.CENTER, true, true);
        gd.widthHint = -1;
        gd.heightHint = -1;
        this.setConstraint(fFigureLabel, gd);
    }

    // Método para o EditPart encontrar o label
    public WrappingLabel getFigureLabel() {
        return fFigureLabel;
    }

    @Override
    protected void fillShape(Graphics graphics) {
        graphics.setAntialias(SWT.ON);
        graphics.setBackgroundColor(ColorConstants.white);

        // Use client area (já considera insets/border)
        Rectangle client = getClientArea().getCopy();
        // Garantir que client tenha dimensões válidas
        if (client.width > 0 && client.height > 0) {
            graphics.fillOval(client);
        }
    }

    @Override
    protected void outlineShape(Graphics graphics) {
        graphics.setAntialias(SWT.ON);
        // Usar client area para desenhar contornos dentro da área disponível
        Rectangle client = getClientArea().getCopy();

        if (client.width <= 0 || client.height <= 0) {
            return; // nada a desenhar
        }

        // Contorno externo
        int outerWidth = 1;
        // desenhar com a linha inteiramente dentro de 'client': reduzir por outerWidth/2
        Rectangle outer = client.getCopy();
        outer.shrink(outerWidth / 2, outerWidth / 2);
        graphics.setLineWidth(outerWidth);
        graphics.setForegroundColor(ColorConstants.red);
        if (outer.width > 0 && outer.height > 0) {
            graphics.drawOval(outer);
        }

        // Contorno interno (leva em conta INSET e metade das larguras das linhas)
        int innerWidth = 1;
        Rectangle inner = client.getCopy();
        // total shrink = outerWidth/2 + INSET + innerWidth/2
        int totalShrink = (outerWidth / 2) + INSET + (innerWidth / 2);
        inner.shrink(totalShrink, totalShrink);
        graphics.setLineWidth(innerWidth);
        if (inner.width > 0 && inner.height > 0) {
            graphics.drawOval(inner);
        }
    }

}
