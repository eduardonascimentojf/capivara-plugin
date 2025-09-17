package br.ufjf.capivara.analyzer;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * Classe utilitária que encapsula a configuração e execução do {@link ASTParser} do Eclipse JDT.
 * <p>
 * Sua principal responsabilidade é transformar uma String contendo código-fonte Java
 * em uma Árvore de Sintaxe Abstrata ({@link CompilationUnit}), que pode então ser
 * analisada por um {@link ASTVisitor}, como o {@link CauseEffectVisitor}.
 */
public class CodeAnalyzer {

	/**
	 * Analisa uma String de código-fonte Java e a converte em uma {@link CompilationUnit}.
	 * <p>
	 * O parser é configurado para a versão Java 17 e para resolver bindings,
	 * permitindo uma análise semântica mais profunda do código.
	 *
	 * @param code O código-fonte Java a ser analisado.
	 * @return A raiz da Árvore de Sintaxe Abstrata (AST) gerada, ou {@code null} se ocorrer um erro.
	 */
	@SuppressWarnings("deprecation")
	public CompilationUnit parse(String code) {
		ASTParser parser = ASTParser.newParser(AST.JLS17);
		parser.setSource(code.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setEnvironment(new String[0], new String[0], null, true);
		parser.setUnitName("temp.java");

		return (CompilationUnit) parser.createAST(null);
	}
}