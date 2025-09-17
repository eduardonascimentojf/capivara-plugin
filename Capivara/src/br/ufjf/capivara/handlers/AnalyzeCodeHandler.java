package br.ufjf.capivara.handlers;

import java.util.Map;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import br.ufjf.capivara.analyzer.CodeAnalyzer;
import br.ufjf.capivara.analyzer.CauseEffectVisitor;
import br.ufjf.capivara.graph.GraphvizGenerator;
import br.ufjf.capivara.table.TruthTableGenerator;
import br.ufjf.capivara.views.AnalysisResultView;

/**
 * Handler principal do plugin, responsável por orquestrar a análise do código.
 * <p>
 * Esta classe é ativada quando o usuário aciona o comando "Analisar Código". Ela
 * captura o texto selecionado no editor, coordena as diferentes ferramentas de
 * análise (parser, visitor do grafo, gerador de tabela verdade) e envia os
 * resultados formatados para a {@link AnalysisResultView}.
 */
public class AnalyzeCodeHandler extends AbstractHandler {

	/**
	 * Executa a ação principal de análise do plugin.
	 * O fluxo de execução é:
	 * 1. Obter o código Java selecionado pelo usuário no editor ativo.
	 * 2. Utilizar o {@link CodeAnalyzer} para gerar uma Árvore de Sintaxe Abstrata (AST).
	 * 3. Encontrar o primeiro método declarado no código selecionado.
	 * 4. Invocar o {@link CauseEffectVisitor} para construir o grafo de fluxo de controle.
	 * 5. Invocar o {@link TruthTableGenerator} para criar a tabela verdade.
	 * 6. Formatar as saídas (código anotado e grafo DOT).
	 * 7. Exibir todos os resultados na {@link AnalysisResultView}.
	 *
	 * @param event O evento de execução do comando, fornecido pela plataforma Eclipse.
	 * @return Sempre {@code null}, pois este handler não retorna um resultado.
	 * @throws ExecutionException Se ocorrer um erro durante a execução do comando.
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart editor = HandlerUtil.getActiveEditor(event);
		if (editor == null) {
			return null;
		}

		ISelection selection = editor.getSite().getSelectionProvider().getSelection();
		if (selection instanceof ITextSelection) {
			String selectedText = ((ITextSelection) selection).getText();

			if (selectedText != null && !selectedText.trim().isEmpty()) {

				CodeAnalyzer analyzer = new CodeAnalyzer();
				CompilationUnit astRoot = analyzer.parse("class Wrapper {\n" + selectedText + "\n}");

				MethodDeclaration method = findMethod(astRoot);
				if (method == null) {
					System.out.println("AVISO: Nenhum método Java válido foi encontrado no código selecionado.");
					return null;
				}

				CauseEffectVisitor cfgVisitor = new CauseEffectVisitor();
				cfgVisitor.setup(astRoot);
				astRoot.accept(cfgVisitor);

				String annotatedCode = getAnnotatedCode(selectedText, cfgVisitor.getLineToNodeMap());
				
				TruthTableGenerator truthTableGenerator = new TruthTableGenerator();
				String truthTable = truthTableGenerator.generateTruthTable(astRoot, method);

				GraphvizGenerator graphGenerator = new GraphvizGenerator();
				String dotGraph = graphGenerator.generateDotGraph(cfgVisitor.getGraphEdges(), cfgVisitor.getNodeTypes(),
						cfgVisitor.getNodeLabels());

				showResultsInView(event, annotatedCode, truthTable, dotGraph);

			}
		}
		return null;
	}

	/**
	 * Exibe os resultados da análise na view customizada do plugin.
	 *
	 * @param event O evento de execução original, usado para obter o contexto da workbench.
	 * @param annotatedCode A string do código original anotado com os nós do grafo.
	 * @param truthTable A string formatada da tabela verdade.
	 * @param dotGraph A string do grafo no formato DOT.
	 */
	private void showResultsInView(ExecutionEvent event, String annotatedCode, String truthTable, String dotGraph) {
		try {
			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
			IWorkbenchPage page = window.getActivePage();
			AnalysisResultView view = (AnalysisResultView) page.showView(AnalysisResultView.ID);
			view.displayResults(annotatedCode, truthTable, dotGraph);
		} catch (PartInitException e) {
			System.err.println("Erro ao tentar abrir a view de resultados do Capivara.");
			e.printStackTrace();
		}
	}

	/**
	 * Utilitário para encontrar o primeiro nó de declaração de método na AST.
	 *
	 * @param cu A raiz da AST (CompilationUnit).
	 * @return O primeiro {@link MethodDeclaration} encontrado, ou {@code null} se nenhum existir.
	 */
	private MethodDeclaration findMethod(CompilationUnit cu) {
		final MethodDeclaration[] result = new MethodDeclaration[1];
		cu.accept(new ASTVisitor() {
			@Override
			public boolean visit(MethodDeclaration node) {
				if (result[0] == null) { // pega apenas o primeiro metodo
					result[0] = node;
				}
				return false; 
			}
		});
		return result[0];
	}

	/**
	 * Gera uma string do código original, com cada linha prefixada por um comentário
	 * indicando seu número de linha e o ID do nó do grafo correspondente.
	 *
	 * @param originalCode A string do código selecionado pelo usuário.
	 * @param lineToNodeMap O mapa que associa cada número de linha a um ID de nó.
	 * @return O código formatado com as anotações.
	 */
	private String getAnnotatedCode(String originalCode, Map<Integer, Integer> lineToNodeMap) {
		StringBuilder sb = new StringBuilder();
		String[] codeLines = originalCode.split("\\r?\\n");

		for (int i = 0; i < codeLines.length; i++) {
			// A linha no CompilationUnit é a linha do editor + 1 (por causa do "class Wrapper {")
			int currentLineInCU = i + 2; 
			Integer nodeId = lineToNodeMap.get(currentLineInCU);
			String nodeLabel = (nodeId != null) ? String.format("/*Nó %02d*/", nodeId) : "/* */";
			sb.append(String.format("/*Linha %02d*/ %s \t%s\n", (i + 1), nodeLabel, codeLines[i]));
		}
		return sb.toString();
	}
}