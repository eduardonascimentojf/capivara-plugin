package br.ufjf.capivara.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.jface.resource.JFaceResources;

/**
 * View do Eclipse responsável por exibir os resultados da análise de código do plugin Capivara.
 * <p>
 * Apresenta a análise em três abas: o código-fonte anotado com os nós do grafo,
 * a tabela verdade de causa-efeito e o código-fonte do grafo no formato DOT.
 * Esta view é preenchida pelo {@link br.ufjf.capivara.handlers.AnalyzeCodeHandler}.
 *
 * @see org.eclipse.ui.part.ViewPart
 */
public class AnalysisResultView extends ViewPart {
	public static final String ID = "br.ufjf.capivara.views.analysisResultView";

	private StyledText annotatedCodeText;
	private Text truthTableText;
	private Text dotGraphText;
	private Button copyDotButton;
	private Label statusLabel;

	@Override
	public void createPartControl(Composite parent) {
		TabFolder tabFolder = new TabFolder(parent, SWT.NONE);

		// Aba 1: Enumeração de Nós
		TabItem nodeTab = new TabItem(tabFolder, SWT.NONE);
		nodeTab.setText("Enumeração de Nós");
		annotatedCodeText = new StyledText(tabFolder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		annotatedCodeText.setFont(JFaceResources.getFont(JFaceResources.TEXT_FONT));
		annotatedCodeText.setEditable(false);
		nodeTab.setControl(annotatedCodeText);

		// Aba 2: Tabela Verdade
		TabItem truthTableTab = new TabItem(tabFolder, SWT.NONE);
		truthTableTab.setText("Tabela Verdade");
		truthTableText = new Text(tabFolder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		Font monoFont = JFaceResources.getFont(JFaceResources.TEXT_FONT); // Fonte monoespaçada
		truthTableText.setFont(monoFont);
		truthTableText.setEditable(false);
		truthTableTab.setControl(truthTableText);

		// Aba 3: Grafo (DOT)
		TabItem graphTab = new TabItem(tabFolder, SWT.NONE);
		graphTab.setText("Grafo (DOT)");

		Composite graphComposite = new Composite(tabFolder, SWT.NONE);
		graphComposite.setLayout(new GridLayout(1, false));

		dotGraphText = new Text(graphComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		dotGraphText.setFont(monoFont);
		dotGraphText.setEditable(false);
		dotGraphText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite buttonComposite = new Composite(graphComposite, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(2, false));
		buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		copyDotButton = new Button(buttonComposite, SWT.PUSH);
		copyDotButton.setText("📋 Copiar Código DOT");

		copyDotButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				copyDotToClipboard();
			}
		});

		statusLabel = new Label(buttonComposite, SWT.NONE);
		statusLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		graphTab.setControl(graphComposite);

		// Limpa a view inicialmente
		displayResults("""
			    Selecione um método e use o menu de contexto 'Analisar Código (Capivara)' para começar.
			    Para visualizar o Grafo DOT utilize:
			    • Graphviz Online: https://dreampuf.github.io/GraphvizOnline
			    • Instale na sua máquina o Graphviz (Crie um novo arquivo .dot e rode o comando para gerar o grafo)
			    """,
			    "",
			    "");
	}

	public void displayResults(String annotatedCode, String truthTable, String dotGraph) {
		if (getViewSite() == null || getViewSite().getShell().isDisposed())
			return;

		getViewSite().getShell().getDisplay().asyncExec(() -> {
			annotatedCodeText.setText(annotatedCode != null ? annotatedCode : "");
			truthTableText.setText(truthTable != null ? truthTable : "");
			dotGraphText.setText(dotGraph != null ? dotGraph : "");
			copyDotButton.setEnabled(dotGraph != null && !dotGraph.isEmpty());
			updateStatus("Análise concluída.");
		});
	}

	private void copyDotToClipboard() {
		String dotContent = dotGraphText.getText();
		if (dotContent != null && !dotContent.isEmpty()) {
			Clipboard clipboard = new Clipboard(getViewSite().getShell().getDisplay());
			try {
				TextTransfer textTransfer = TextTransfer.getInstance();
				clipboard.setContents(new Object[] { dotContent }, new Transfer[] { textTransfer });
				updateStatus("Copiado para a área de transferência!");
			} finally {
				clipboard.dispose();
			}
		}
	}

	private void updateStatus(String message) {
		if (statusLabel != null && !statusLabel.isDisposed()) {
			statusLabel.setText(message);
		}
	}

	@Override
	public void setFocus() {
		if (annotatedCodeText != null) {
			annotatedCodeText.setFocus();
		}
	}
}