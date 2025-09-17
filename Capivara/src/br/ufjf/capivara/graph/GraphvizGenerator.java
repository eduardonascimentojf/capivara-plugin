package br.ufjf.capivara.graph;

import java.util.*;
import br.ufjf.capivara.model.Edge;

/**
 * Classe responsável por gerar o código fonte do grafo no formato DOT,
 * compatível com a ferramenta Graphviz.
 * <p>
 * Esta classe recebe as estruturas de dados que representam o grafo de fluxo de controle
 * (nós, arestas e tipos), processadas pelo {@link br.ufjf.capivara.analyzer.CauseEffectVisitor},
 * e as converte em uma String formatada.
 *
 * @see br.ufjf.capivara.analyzer.CauseEffectVisitor
 */
public class GraphvizGenerator {

    public String generateDotGraph(Map<Integer, List<Edge>> graphEdges,
                                   Map<Integer, String> nodeTypes, 
                                   Map<Integer, String> nodeLabels) {

        StringBuilder dot = new StringBuilder();

        // Cabecalho do grafo
        dot.append("digraph G {\n");
        dot.append("  rankdir=TB;\n");
        dot.append("  node [shape=circle, style=solid, width=0.5, fixedsize=true, fontname=\"Helvetica\"];\n");
        dot.append("  edge [fontname=\"Helvetica\", fontsize=10];\n\n");

        Set<Integer> allNodes = new TreeSet<>(nodeTypes.keySet());

        for (Integer nodeId : allNodes) {
            String nodeType = nodeTypes.get(nodeId);
            String color = getNodeColor(nodeType);
            String shape = getNodeShape(nodeType);
            String label = String.valueOf(nodeId);
           
            String extraAttributes = "";

            dot.append(String.format(
                "  %d [label=\"%s\", shape=%s, color=\"%s\", style=solid%s];\n",
                nodeId, label, shape, color, extraAttributes
            ));
        }

        for (Map.Entry<Integer, List<Edge>> entry : graphEdges.entrySet()) {
            Integer fromNode = entry.getKey();
            List<Edge> edges = entry.getValue();

            for (Edge edge : edges) {
                Integer toNode = edge.getDestinationNodeId();
                dot.append(String.format("  %d -> %d;\n", fromNode, toNode));
            }
        }

        dot.append("}\n");
        return dot.toString();
    }

    private String getNodeColor(String nodeType) {
        if (nodeType == null) return "black";
        switch (nodeType) {
            case "ENTRY": return "#28a745";
            case "DECISION": return "#007bff";
            case "LOOP_DECISION": return "#FFA500";
            case "EXIT": return "#dc3545";
            case "PROCESSING":
            default: return "black";
        }
    }

    private String getNodeShape(String nodeType) {
        if (nodeType == null) return "circle";
        switch (nodeType) {
            case "EXIT": return "doublecircle";
            default: return "circle";
        }
    }
}