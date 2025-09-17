package br.ufjf.capivara.analyzer;

import org.eclipse.jdt.core.dom.*;
import java.util.*;

/**
 * Classe utilitária responsável por analisar um método e extrair todas as suas
 * condições lógicas de forma individual e única.
 * <p>
 * O objetivo é quebrar expressões complexas em suas partes mais simples e indivisíveis.
 * Por exemplo, de uma expressão como {@code (a > 0 && b < 10)}, este extrator
 * identifica as duas condições individuais: {@code "a > 0"} e {@code "b < 10"}.
 * <p>
 * O resultado é usado pelo {@link br.ufjf.capivara.table.TruthTableGenerator}
 * para construir as colunas da tabela verdade.
 */
public class ConditionExtractor {

	private final Set<String> atomicConditions = new LinkedHashSet<>();

	/**
	 * Extrai todas as condições individuais e únicas de um determinado método,
	 * mantendo a ordem em que aparecem no código.
	 *
	 * @param method O nó {@code MethodDeclaration} da AST a ser analisado.
	 * @return Uma {@code List<String>} contendo cada condição simples encontrada.
	 */
	public List<String> extractAtomicConditions(MethodDeclaration method) {
		atomicConditions.clear();
		method.accept(new ConditionVisitor());
		return new ArrayList<>(atomicConditions);
	}

	/**
	 * Visitor interno que navega na AST para encontrar e extrair as condições.
	 */
	private class ConditionVisitor extends ASTVisitor {

		@Override
		public boolean visit(IfStatement node) {
			extractFromExpression(node.getExpression());
			return true; // Continua a visita nos filhos para encontrar 'if' aninhados
		}

		private void extractFromExpression(Expression expression) {
			if (expression instanceof InfixExpression) {
				InfixExpression infixExpr = (InfixExpression) expression;
				InfixExpression.Operator operator = infixExpr.getOperator();

				if (operator == InfixExpression.Operator.CONDITIONAL_AND
						|| operator == InfixExpression.Operator.CONDITIONAL_OR) {
					// se e um operador logico && ou || vamos analisar os dois lados.
					extractFromExpression(infixExpr.getLeftOperand());
					extractFromExpression(infixExpr.getRightOperand());
				} else {
					// se e um operador (==, >, <, <= ou >= então e uma condição individual.
					addAtomicCondition(expression.toString());
				}
			} else if (expression instanceof ParenthesizedExpression) {
				extractFromExpression(((ParenthesizedExpression) expression).getExpression());
			} else if (expression instanceof PrefixExpression) {
				PrefixExpression prefixExpr = (PrefixExpression) expression;
				if (prefixExpr.getOperator() == PrefixExpression.Operator.NOT) {
					extractFromExpression(prefixExpr.getOperand());
				} else {
					addAtomicCondition(expression.toString());
				}
			} else {
				addAtomicCondition(expression.toString());
			}
		}

		private void addAtomicCondition(String condition) {
			String normalized = condition.trim().replaceAll("\\s+", " ");
			if (!normalized.isEmpty()) {
				atomicConditions.add(normalized); 
			}
		}
	}

	/**
	 * Método auxiliar que quebra uma condição complexa em uma lista de condições simples.
	 * <p>
	 * Este método usa uma abordagem mais simples baseada em String, que pode não
	 * cobrir todos os casos complexos que o visitor da AST cobre.
	 *
	 * @param complexCondition Uma String com uma condição composta, ex: "(a > 1) && b < 2".
	 * @return Uma lista com as partes individuais, ex: ["(a > 1)", "b < 2"].
	 */
	public static List<String> breakDownComplexCondition(String complexCondition) {
		List<String> atomicParts = new ArrayList<>();
		String normalized = complexCondition.trim().replaceAll("\\s+", " ");

		// quebra a string pelos operadores lógicos && ou ||
		String[] parts = normalized.split("\\s*(&&|\\|\\|)\\s*");

		for (String part : parts) {
			part = part.trim();
			if (part.startsWith("(") && part.endsWith(")")) {
				part = part.substring(1, part.length() - 1).trim();
			}
			if (!part.isEmpty() && !atomicParts.contains(part)) {
				atomicParts.add(part);
			}
		}
		return atomicParts;
	}

	/**
	 * Método auxiliar para padronizar o espaçamento em uma String de condição.
	 *
	 * @param condition A condição a ser normalizada.
	 * @return A condição com espaçamento padronizado.
	 */
	public static String normalizeCondition(String condition) {
		return condition.trim().replaceAll("\\s+", " ").replace(" == ", " == ").replace(" != ", " != ")
				.replace(" < ", " < ").replace(" > ", " > ").replace(" <= ", " <= ").replace(" >= ", " >= ");
	}
}