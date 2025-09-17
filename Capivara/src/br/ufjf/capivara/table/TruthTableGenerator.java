package br.ufjf.capivara.table;

import org.eclipse.jdt.core.dom.*;
import br.ufjf.capivara.analyzer.ConditionExtractor;
import br.ufjf.capivara.model.PathCondition;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Gerador de tabela verdade robusto:
 * - Gera todas as 2^n combinações (não usa mais '-')
 * - Parser booleano tokenizado (suporta !, &&, ||, parênteses)
 * - Usa ConditionExtractor para obter condições atômicas
 * - Extrai caminhos/effects com visitor interno
 */
public class TruthTableGenerator {

	private final ConditionExtractor conditionExtractor;

	public TruthTableGenerator() {
		this.conditionExtractor = new ConditionExtractor();
	}

	public String generateTruthTable(CompilationUnit cu, MethodDeclaration method) {
		try {
			List<String> atomicConditions = conditionExtractor.extractAtomicConditions(method);
			if (atomicConditions.isEmpty()) {
				return "Nenhuma condição encontrada no método.\n";
			}

			List<PathCondition> pathConditions = extractPathConditions(method);
			if (pathConditions.isEmpty()) {
				return "Nenhum caminho de execução com efeito encontrado.\n";
			}

			return buildTruthTable(atomicConditions, pathConditions);
		} catch (Exception e) {
			return "Erro ao gerar tabela verdade: " + e.getMessage() + "\n";
		}
	}

	private List<PathCondition> extractPathConditions(MethodDeclaration method) {
		PathExtractionVisitor visitor = new PathExtractionVisitor();
		method.accept(visitor);
		return visitor.getPathConditions();
	}

	private String buildTruthTable(List<String> atomicConditions, List<PathCondition> pathConditions) {
		StringBuilder table = new StringBuilder();

		int maxConditionWidth = Math.max(25, atomicConditions.stream().mapToInt(String::length).max().orElse(0));
		int maxEffectWidth = Math.max(25, pathConditions.stream().mapToInt(p -> p.getEffect().length()).max().orElse(0));

		// Cabecalho
		table.append("Caso");
		for (String condition : atomicConditions) {
			table.append(" | ").append(
					String.format("%-" + maxConditionWidth + "s", truncateString(condition, maxConditionWidth)));
		}
		table.append(" | ").append(String.format("%-" + maxEffectWidth + "s", "Resultado (Efeito)")).append("\n");

		int totalWidth = 4 + (atomicConditions.size() * (3 + maxConditionWidth)) + (3 + maxEffectWidth);
		table.append("-".repeat(totalWidth)).append("\n");

		int n = atomicConditions.size();
		if (n > 20) { // limitamos o numero de condições por motivo de perfomace 2^n
			return "Erro: O número de condições (" + n + ") é muito grande para gerar uma tabela verdade.\n";
		}
		int combinations = 1 << n;

		for (int mask = 0; mask < combinations; mask++) {
			table.append(String.format("%-4d", (mask + 1)));

			boolean[] values = new boolean[n];
			for (int j = 0; j < n; j++) {
				values[j] = (mask & (1 << (n - 1 - j))) != 0;
			}

			String effect = determineEffect(atomicConditions, values, pathConditions);

			// Colunas V/F
			for (int j = 0; j < n; j++) {
				String cell = values[j] ? "V" : "F";
				table.append(" | ").append(String.format("%-" + maxConditionWidth + "s", cell));
			}

			table.append(" | ").append(String.format("%-" + maxEffectWidth + "s", truncateString(effect, maxEffectWidth)))
					.append("\n");
		}

		return table.toString();
	}

	private String determineEffect(List<String> atomicConditions, boolean[] values,
			List<PathCondition> pathConditions) {
		Map<String, Boolean> conditionValues = new LinkedHashMap<>();
		for (int i = 0; i < atomicConditions.size(); i++) {
			conditionValues.put(atomicConditions.get(i), values[i]);
		}

		for (PathCondition pc : pathConditions) {
			if (!pc.isDefaultPath() && evaluatePathCondition(pc, conditionValues)) {
				return pc.getEffect();
			}
		}

		for (PathCondition pc : pathConditions) {
			if (pc.isDefaultPath())
				return pc.getEffect();
		}

		return "";
	}

	private boolean evaluatePathCondition(PathCondition pc, Map<String, Boolean> conditionValues) {
		String condition = pc.getCondition();
		if (condition == null || condition.trim().isEmpty() || condition.equals("default")) {
			return true;
		}
		try {
			return evaluateConditionExpression(condition, conditionValues);
		} catch (Exception e) {
			System.err.println("Erro ao avaliar condição: " + condition + " - " + e.getMessage());
			return false;
		}
	}

	private boolean evaluateConditionExpression(String expression, Map<String, Boolean> conditionValues) {
		String evaluated = expression;

		for (Map.Entry<String, Boolean> e : conditionValues.entrySet()) {
			evaluated = evaluated.replaceAll(Pattern.quote(e.getKey()), e.getValue() ? "T" : "F");
		}

		evaluated = evaluated.replaceAll("\\s+", "");

		List<String> tokens = tokenizeBooleanExpr(evaluated);
		return new TokenParser(tokens).parse();
	}

	private List<String> tokenizeBooleanExpr(String s) {
		List<String> tokens = new ArrayList<>();
		int i = 0;
		while (i < s.length()) {
			if (i + 1 < s.length() && s.charAt(i) == '&' && s.charAt(i + 1) == '&') {
				tokens.add("&&");
				i += 2;
			} else if (i + 1 < s.length() && s.charAt(i) == '|' && s.charAt(i + 1) == '|') {
				tokens.add("||");
				i += 2;
			} else {
				char c = s.charAt(i);
				if (c == '!' || c == '(' || c == ')') {
					tokens.add(String.valueOf(c));
					i++;
				} else if (c == 'T' || c == 'F') {
					tokens.add(String.valueOf(c));
					i++;
				} else {
					throw new IllegalArgumentException("Caractere inválido: " + s.substring(i));
				}
			}
		}
		return tokens;
	}

	private static class TokenParser {
		private final List<String> tokens;
		private int pos = 0;

		TokenParser(List<String> tokens) {
			this.tokens = tokens;
		}

		boolean parse() {
			boolean v = parseOr();
			if (pos != tokens.size()) {
				String rest = String.join("", tokens.subList(pos, tokens.size()));
				throw new IllegalArgumentException("Expressão inválida em: " + rest);
			}
			return v;
		}

		private boolean parseOr() {
			boolean v = parseAnd();
			while (pos < tokens.size() && tokens.get(pos).equals("||")) {
				pos++;
				boolean r = parseAnd();
				v = v || r;
			}
			return v;
		}

		private boolean parseAnd() {
			boolean v = parseNot();
			while (pos < tokens.size() && tokens.get(pos).equals("&&")) {
				pos++;
				boolean r = parseNot();
				v = v && r;
			}
			return v;
		}

		private boolean parseNot() {
			int neg = 0;
			while (pos < tokens.size() && tokens.get(pos).equals("!")) {
				neg++;
				pos++;
			}
			boolean v = parsePrimary();
			return (neg % 2 == 0) ? v : !v;
		}

		private boolean parsePrimary() {
			if (pos >= tokens.size())
				throw new IllegalArgumentException("Fim inesperado");
			String tk = tokens.get(pos);
			if (tk.equals("(")) {
				pos++;
				boolean v = parseOr();
				expect(")");
				return v;
			}
			if (tk.equals("T")) {
				pos++;
				return true;
			}
			if (tk.equals("F")) {
				pos++;
				return false;
			}
			throw new IllegalArgumentException("Token inválido em: " + String.join("", tokens.subList(pos, tokens.size())));
		}

		private void expect(String tok) {
			if (pos >= tokens.size() || !tokens.get(pos).equals(tok)) {
				String rest = (pos < tokens.size()) ? String.join("", tokens.subList(pos, tokens.size())) : "";
				throw new IllegalArgumentException("Esperado '" + tok + "' em: " + rest);
			}
			pos++;
		}
	}

	private String truncateString(String text, int maxWidth) {
		if (text == null)
			return "";
		return text.length() > maxWidth ? text.substring(0, maxWidth - 3) + "..." : text;
	}

	/**
	 * Visitor que extrai caminhos de execução (retornos / atribuições) e suas
	 * condições acumuladas.
	 * 
	 * Corrigido para: remover "return" e limpar aspas/literais.
	 */
	private static class PathExtractionVisitor extends ASTVisitor {
		private final List<PathCondition> pathConditions = new ArrayList<>();
		private final Deque<String> conditionStack = new ArrayDeque<>();

		public List<PathCondition> getPathConditions() {
			return pathConditions;
		}

		@Override
		public boolean visit(IfStatement node) {
			String cond = node.getExpression().toString();

			conditionStack.addLast("(" + cond + ")");
			node.getThenStatement().accept(this);
			conditionStack.removeLast();

			if (node.getElseStatement() != null) {
				conditionStack.addLast("!(" + "(" + cond + ")" + ")");
				node.getElseStatement().accept(this);
				conditionStack.removeLast();
			}
			return false;
		}

		@Override
		public boolean visit(ReturnStatement node) {
			String effect;
			Expression expr = node.getExpression();

			if (expr == null) {
				effect = "void";
			} else if (expr instanceof StringLiteral) {
				effect = ((StringLiteral) expr).getLiteralValue();
			} else if (expr instanceof CharacterLiteral) {
				effect = ((CharacterLiteral) expr).getEscapedValue().replace("'", "");
			} else if (expr instanceof NumberLiteral) {
				effect = ((NumberLiteral) expr).getToken();
			} else if (expr instanceof BooleanLiteral) {
				effect = String.valueOf(((BooleanLiteral) expr).booleanValue());
			} else {
				effect = expr.toString();
			}

			pathConditions.add(new PathCondition(buildPathCondition(), effect));
			return false;
		}

		@Override
		public boolean visit(ExpressionStatement node) {
			if (node.getExpression() instanceof Assignment) {
				Assignment a = (Assignment) node.getExpression();
				String rhs;

				if (a.getRightHandSide() instanceof StringLiteral) {
					rhs = ((StringLiteral) a.getRightHandSide()).getLiteralValue();
				} else if (a.getRightHandSide() instanceof CharacterLiteral) {
					rhs = ((CharacterLiteral) a.getRightHandSide()).getEscapedValue().replace("'", "");
				} else if (a.getRightHandSide() instanceof NumberLiteral) {
					rhs = ((NumberLiteral) a.getRightHandSide()).getToken();
				} else if (a.getRightHandSide() instanceof BooleanLiteral) {
					rhs = String.valueOf(((BooleanLiteral) a.getRightHandSide()).booleanValue());
				} else {
					rhs = a.getRightHandSide().toString();
				}

				String effect = a.getLeftHandSide().toString() + " " + a.getOperator().toString() + " " + rhs;
				pathConditions.add(new PathCondition(buildPathCondition(), effect));
			}
			return false;
		}

		private String buildPathCondition() {
			if (conditionStack.isEmpty())
				return "default";
			return conditionStack.stream().collect(Collectors.joining(" && "));
		}
	}
}
