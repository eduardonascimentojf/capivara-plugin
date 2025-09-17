package br.ufjf.capivara.model;

/**
 * Representa um par de Causa-Efeito para um caminho de execução específico no código.
 * <p>
 * Esta classe é um objeto de dados imutável que armazena a condição lógica
 * completa (a "causa") necessária para percorrer um caminho, e o resultado
 * final (o "efeito") que ocorre nesse caminho, como um retorno ou uma atribuição.
 * <p>
 * É utilizada primariamente pelo {@link br.ufjf.capivara.table.TruthTableGenerator}
 * para construir as linhas da tabela verdade.
 *
 * @see br.ufjf.capivara.table.TruthTableGenerator
 */
public class PathCondition {

	private final String condition;
	private final String effect;

	/**
	 * Construtor para criar uma nova condição de caminho.
	 * * @param condition A condição lógica que define este caminho (ex: "a > 0 && b < 5").
	 * @param effect    O efeito ou resultado deste caminho (ex: "return true", "x = 10").
	 */
	public PathCondition(String condition, String effect) {
		this.condition = condition;
		this.effect = effect;
	}

	/**
	 * Obtém a condição lógica que define este caminho.
	 * * @return A condição como uma String.
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * Obtém o efeito ou resultado final deste caminho.
	 * * @return O efeito como uma String.
	 */
	public String getEffect() {
		return effect;
	}

	/**
	 * Verifica se este caminho representa um caminho padrão (default), ou seja,
	 * um fluxo que não depende de nenhuma condição explícita.
	 * * @return {@code true} se for o caminho padrão, {@code false} caso contrário.
	 */
	public boolean isDefaultPath() {
		return condition == null || condition.trim().isEmpty() || condition.equals("default");
	}

	@Override
	public String toString() {
		return "PathCondition{" + "condition='" + condition + '\'' + ", effect='" + effect + '\'' + '}';
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		PathCondition that = (PathCondition) obj;

		if (condition != null ? !condition.equals(that.condition) : that.condition != null)
			return false;
		return effect != null ? effect.equals(that.effect) : that.effect == null;
	}

	@Override
	public int hashCode() {
		int result = condition != null ? condition.hashCode() : 0;
		result = 31 * result + (effect != null ? effect.hashCode() : 0);
		return result;
	}
}