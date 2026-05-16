package gfc.diagram.edit.helpers;

import java.util.ArrayList;
import java.util.List;
import gfc.Edge;
import gfc.Flowchart;
import gfc.Node;

public class CoverageHeuristics {

    // Status
    public static final int NODE_GREEN = 1;
    public static final int NODE_YELLOW = 2;
    public static final int NODE_RED = 3;

    public static final int EDGE_GREEN = 4;
    public static final int EDGE_YELLOW = 5;
    public static final int EDGE_RED = 6;

    /**
     * Calcula o score dos nos... 
     * verde 1.0, amarelo 0.5.
     */
    public static double calculateNodeScore(Flowchart flowchart) {
        double total = 0, score = 0;
        for (Node node : flowchart.getNodes()) {
            int s = node.getCoverageStatus();
            if (s == 0) continue; 
            total++;
            if (s == NODE_GREEN) score += 1.0;
            else if (s == NODE_YELLOW) score += 0.5;
        }
        return (total > 0) ? (score / total) * 100.0 : 0.0;
    }

   
    public static double calculateEdgeScore(Flowchart flowchart) {
        double total = 0, covered = 0;
        for (Edge edge : flowchart.getEdges()) {
            total++;
            if (isEdgeCovered(edge, flowchart)) {
                covered++;
            }
        }
        return (total > 0) ? (covered / total) * 100.0 : 0.0;
    }

  
    public static boolean isEdgeCovered(Edge edge, Flowchart flowchart) {
        Node source = edge.getSource();
        Node target = edge.getTarget();

        int s = source.getCoverageStatus();
        int t = target.getCoverageStatus();

        if (s == EDGE_RED) return false;      // origem vermelha
        if (s == EDGE_GREEN) return true;     // Origem verde 

   
        if (t == EDGE_RED) return false;

        List<Node> predecessors = getPredecessorsOf(target, flowchart);
        if (predecessors.size() <= 1) return true; // O filho só tem um 'pai'

        // Se tem outro caminho que não seja vermelho (pai)
        boolean outroCaminhoAtivo = predecessors.stream()
                .filter(p -> p != source)
                .filter(p -> p.getId() < target.getId()) 
                .anyMatch(p -> p.getCoverageStatus() != EDGE_RED 
                            && p.getCoverageStatus() != 0);

        return !outroCaminhoAtivo;
    }

    private static List<Node> getPredecessorsOf(Node target, Flowchart flowchart) {
        List<Node> result = new ArrayList<>();
        for (Edge e : flowchart.getEdges()) {
            if (e.getTarget() == target && e.getSource() != null) {
                result.add(e.getSource());
            }
        }
        return result;
    }
}