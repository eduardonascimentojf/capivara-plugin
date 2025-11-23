package br.ufjf.capivara.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.jface.resource.JFaceResources;

/**
 * View do Eclipse responsável por exibir os resultados da análise de código do plugin Capivara.
 * <p>
 * Apresenta apenas o código-fonte anotado com a enumeração dos nós do grafo.
 * Esta view é preenchida pelo {@link br.ufjf.capivara.handlers.AnalyzeCodeHandler}.
 *
 * @see org.eclipse.ui.part.ViewPart
 */
public class AnalysisResultView extends ViewPart {
    public static final String ID = "br.ufjf.capivara.views.analysisResultView";

    private StyledText annotatedCodeText;

    @Override
    public void createPartControl(Composite parent) {
        // Como só existe uma visualização agora, removemos o TabFolder e
        // adicionamos o StyledText diretamente ao pai.
        
        annotatedCodeText = new StyledText(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
        annotatedCodeText.setFont(JFaceResources.getFont(JFaceResources.TEXT_FONT));
        annotatedCodeText.setEditable(false);

        // Texto inicial de instrução
        displayResults("Select a method and use the 'Capivara | Node enumerate' context menu to get started.");
    }

    /**
     * Atualiza a view com o código anotado.
     * * @param annotatedCode A string contendo o código fonte e os comentários dos nós.
     */
    public void displayResults(String annotatedCode) {
        if (getViewSite() == null || getViewSite().getShell().isDisposed())
            return;

        getViewSite().getShell().getDisplay().asyncExec(() -> {
            annotatedCodeText.setText(annotatedCode != null ? annotatedCode : "");
        });
    }

    @Override
    public void setFocus() {
        if (annotatedCodeText != null) {
            annotatedCodeText.setFocus();
        }
    }
}