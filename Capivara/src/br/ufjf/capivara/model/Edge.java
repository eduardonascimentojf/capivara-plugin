package br.ufjf.capivara.model;

/**
 * Representa uma aresta direcionada no grafo de fluxo de controle.
 * <p>
 * Esta classe é um objeto de dados imutável que conecta um nó de origem
 * (implícito) a um nó de destino (`destinationNodeId`) e pode conter
 * um rótulo opcional (como "true" ou "false").
 *
 * @see br.ufjf.capivara.analyzer.CauseEffectVisitor
 */
public class Edge {

	private final int destinationNodeId;
	private final String label;

	/**
	 * Construtor para criar uma nova aresta.
	 * * @param destinationNodeId ID do nó de destino.
	 * @param label             Rótulo da aresta (ex: "true", "false", ou "").
	 */
	public Edge(int destinationNodeId, String label) {
		this.destinationNodeId = destinationNodeId;
		this.label = label;
	}

	/**
	 * Obtém o ID do nó de destino desta aresta.
	 * * @return O ID do nó de destino.
	 */
	public int getDestinationNodeId() {
		return destinationNodeId;
	}

	/**
	 * Obtém o rótulo desta aresta.
	 * * @return O rótulo da aresta.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Verifica se esta aresta tem um rótulo não vazio.
	 * * @return {@code true} se o rótulo existir e não for vazio, {@code false} caso contrário.
	 */
	public boolean hasLabel() {
		return label != null && !label.trim().isEmpty();
	}

	@Override
	public String toString() {
		return "Edge{" + "destinationNodeId=" + destinationNodeId + ", label='" + label + '\'' + '}';
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		Edge edge = (Edge) obj;

		if (destinationNodeId != edge.destinationNodeId)
			return false;
		return label != null ? label.equals(edge.label) : edge.label == null;
	}

	@Override
	public int hashCode() {
		int result = destinationNodeId;
		result = 31 * result + (label != null ? label.hashCode() : 0);
		return result;
	}
}