package br.ufjf.capivara.analyzer;

import org.eclipse.jdt.core.dom.*;
import br.ufjf.capivara.model.Edge;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Um visitor da Árvore de Sintaxe Abstrata (AST) responsável por percorrer o código
 * de um método Java e construir um Grafo de Fluxo de Controle (GFC).
 * <p>
 * Esta classe gerencia a criação de nós, arestas e o estado do fluxo através de uma
 * pilha de predecessores para conectar corretamente os diferentes caminhos do código.
 */
public class CauseEffectVisitor extends ASTVisitor {

    // Trata a estrutura condicional 'if-else'.
    @Override
    public boolean visit(IfStatement node) {
        finishSequentialBlock();
        int decisionNode = createNode("DECISION");
        nodeLabels.put(decisionNode, "IF: " + node.getExpression().toString());
        mapLineToNode(node.getExpression(), decisionNode);
        while(!predecessorStack.isEmpty()) { addEdge(predecessorStack.pop(), decisionNode, ""); }
        List<Integer> branchEndNodes = new ArrayList<>();
        
        // Processa o ramo THEN
        predecessorStack.push(decisionNode);
        currentSequentialNode = null;
        inSequentialBlock = false;
        node.getThenStatement().accept(this);
        while (!predecessorStack.isEmpty() && predecessorStack.peek() != decisionNode) { branchEndNodes.add(predecessorStack.pop()); }
        if(!predecessorStack.isEmpty()) predecessorStack.pop();

        // Processa o ramo ELSE
        if (node.getElseStatement() != null) {
            predecessorStack.push(decisionNode);
            currentSequentialNode = null;
            inSequentialBlock = false;
            node.getElseStatement().accept(this);
            while (!predecessorStack.isEmpty() && predecessorStack.peek() != decisionNode) { branchEndNodes.add(predecessorStack.pop()); }
            if(!predecessorStack.isEmpty()) predecessorStack.pop();
        } else {
            branchEndNodes.add(decisionNode);
        }
        
        predecessorStack.clear();
        predecessorStack.addAll(branchEndNodes);
        currentSequentialNode = null;
        inSequentialBlock = false;
        return false;
    }

    private CompilationUnit compilationUnit;
    private final Map<Integer, List<Integer>> nodeToLinesMap = new TreeMap<>();
    private final Map<Integer, List<Edge>> graphEdges = new HashMap<>();
    private final Map<Integer, String> nodeTypes = new HashMap<>();
    private final Map<Integer, String> nodeLabels = new HashMap<>();
    private int nodeCounter = 0;
    private final Stack<Integer> predecessorStack = new Stack<>();
    private final Map<Integer, Integer> lineToNodeMap = new HashMap<>();
    private Integer currentSequentialNode = null;
    private boolean inSequentialBlock = false;
    public void setup(CompilationUnit cu) {
        this.compilationUnit = cu;
        nodeCounter = 0;
        lineToNodeMap.clear();
        nodeToLinesMap.clear();
        graphEdges.clear();
        nodeTypes.clear();
        nodeLabels.clear();
        predecessorStack.clear();
        currentSequentialNode = null;
        inSequentialBlock = false;
    }
    public Map<Integer, List<Edge>> getGraphEdges() { return this.graphEdges; }
    public Map<Integer, String> getNodeTypes() { return this.nodeTypes; }
    public Map<Integer, String> getNodeLabels() { return this.nodeLabels; }
    public Map<Integer, List<Integer>> getNodeToLinesMap() { return this.nodeToLinesMap; }
    public Map<Integer, Integer> getLineToNodeMap() { return lineToNodeMap; }
    @Override
    public boolean visit(MethodDeclaration node) {
        int methodNode = createNode("ENTRY");
        nodeLabels.put(methodNode, "Método: " + node.getName().getIdentifier());
        mapLineToNode(node, methodNode);
        predecessorStack.push(methodNode);
        currentSequentialNode = null;
        inSequentialBlock = false;
        if (node.getBody() != null) {
            node.getBody().accept(this);
        }
        predecessorStack.clear();
        return false;
    }
    @Override
    public boolean visit(WhileStatement node) {
        finishSequentialBlock();
        int decisionNode = createNode("LOOP_DECISION");
        nodeLabels.put(decisionNode, "WHILE: " + node.getExpression().toString());
        mapLineToNode(node, decisionNode);
        while(!predecessorStack.isEmpty()) { addEdge(predecessorStack.pop(), decisionNode, ""); }
        Stack<Integer> bodyPredecessors = new Stack<>();
        bodyPredecessors.push(decisionNode);
        visitLoopBody(node.getBody(), bodyPredecessors);
        for (Integer bodyEndNode : bodyPredecessors) { addEdge(bodyEndNode, decisionNode, ""); }
        predecessorStack.clear();
        predecessorStack.push(decisionNode);
        currentSequentialNode = null;
        inSequentialBlock = false;
        return false;
    }
    @SuppressWarnings("unchecked")
    @Override
    public boolean visit(ForStatement node) {
        finishSequentialBlock();
        int decisionNode = createNode("LOOP_DECISION");
        String init = ((List<ASTNode>)node.initializers()).stream().map(ASTNode::toString).collect(Collectors.joining(", "));
        String cond = node.getExpression() == null ? "" : node.getExpression().toString();
        String update = ((List<ASTNode>)node.updaters()).stream().map(ASTNode::toString).collect(Collectors.joining(", "));
        nodeLabels.put(decisionNode, String.format("FOR (%s; %s; %s)", init, cond, update));
        mapLineToNode(node, decisionNode);
        while(!predecessorStack.isEmpty()) { addEdge(predecessorStack.pop(), decisionNode, ""); }
        Stack<Integer> bodyPredecessors = new Stack<>();
        bodyPredecessors.push(decisionNode);
        visitLoopBody(node.getBody(), bodyPredecessors);
        for (Integer bodyEndNode : bodyPredecessors) { addEdge(bodyEndNode, decisionNode, ""); }
        predecessorStack.clear();
        predecessorStack.push(decisionNode);
        currentSequentialNode = null;
        inSequentialBlock = false;
        return false;
    }
    @Override
    public boolean visit(DoStatement node) {
        finishSequentialBlock();
        int predecessor = predecessorStack.isEmpty() ? -1 : predecessorStack.pop();
        int bodyEntryNode = createNode("PROCESSING");
        nodeLabels.put(bodyEntryNode, "{do}");
        mapLineToNode(node, bodyEntryNode);
        if (predecessor != -1) { addEdge(predecessor, bodyEntryNode, ""); }
        Stack<Integer> bodyPredecessors = new Stack<>();
        bodyPredecessors.push(bodyEntryNode);
        visitLoopBody(node.getBody(), bodyPredecessors);
        int decisionNode = createNode("LOOP_DECISION");
        nodeLabels.put(decisionNode, "while: " + node.getExpression().toString());
        mapLineToNode(node.getExpression(), decisionNode);
        for (Integer bodyEndNode : bodyPredecessors) { addEdge(bodyEndNode, decisionNode, ""); }
        addEdge(decisionNode, bodyEntryNode, "true");
        predecessorStack.clear();
        predecessorStack.push(decisionNode);
        currentSequentialNode = null;
        inSequentialBlock = false;
        return false;
    }
    @Override
    public boolean visit(EnhancedForStatement node) {
        finishSequentialBlock();
        int decisionNode = createNode("LOOP_DECISION");
        nodeLabels.put(decisionNode, "FOR-EACH: " + node.getParameter().getName() + " in " + node.getExpression());
        mapLineToNode(node, decisionNode);
        while(!predecessorStack.isEmpty()) { addEdge(predecessorStack.pop(), decisionNode, ""); }
        Stack<Integer> bodyPredecessors = new Stack<>();
        bodyPredecessors.push(decisionNode);
        visitLoopBody(node.getBody(), bodyPredecessors);
        for (Integer bodyEndNode : bodyPredecessors) { addEdge(bodyEndNode, decisionNode, ""); }
        predecessorStack.clear();
        predecessorStack.push(decisionNode);
        currentSequentialNode = null;
        inSequentialBlock = false;
        return false;
    }
    @Override
    public boolean visit(VariableDeclarationStatement node) { handleSequentialStatement(node); return false; }
    @Override
    public boolean visit(ExpressionStatement node) { handleSequentialStatement(node); return false; }
    @Override
    public boolean visit(ReturnStatement node) {
        finishSequentialBlock();
        int returnNode = createNode("EXIT");
        String returnValue = node.getExpression() != null ? node.getExpression().toString() : "";
        nodeLabels.put(returnNode, "RETURN" + (returnValue.isEmpty() ? "" : ": " + returnValue));
        mapLineToNode(node, returnNode);
        while(!predecessorStack.isEmpty()){
             addEdge(predecessorStack.pop(), returnNode, "");
        }
        currentSequentialNode = null;
        inSequentialBlock = false;
        return false;
    }
    private void handleSequentialStatement(ASTNode node) {
        if (!inSequentialBlock || currentSequentialNode == null) {
            finishSequentialBlock();
            currentSequentialNode = createNode("PROCESSING");
            nodeLabels.put(currentSequentialNode, getNodeLabel(node));
            while(!predecessorStack.isEmpty()){ addEdge(predecessorStack.pop(), currentSequentialNode, ""); }
            predecessorStack.push(currentSequentialNode);
            inSequentialBlock = true;
        } else {
            String currentLabel = nodeLabels.getOrDefault(currentSequentialNode, "");
            String newLabel = getNodeLabel(node);
            if (!currentLabel.contains(newLabel)) {
                nodeLabels.put(currentSequentialNode, currentLabel + "\n" + newLabel);
            }
        }
        mapLineToNode(node, currentSequentialNode);
    }
    private void visitLoopBody(Statement body, Stack<Integer> localPredecessorStack) {
        Stack<Integer> globalStack = new Stack<>();
        globalStack.addAll(this.predecessorStack);
        this.predecessorStack.clear();
        this.predecessorStack.addAll(localPredecessorStack);
        if (body != null) { body.accept(this); }
        localPredecessorStack.clear();
        localPredecessorStack.addAll(this.predecessorStack);
        this.predecessorStack.clear();
        this.predecessorStack.addAll(globalStack);
    }
    private void finishSequentialBlock() {
        if (inSequentialBlock && currentSequentialNode != null) { inSequentialBlock = false; }
    }
    private int createNode(String type) { nodeCounter++; nodeTypes.put(nodeCounter, type); return nodeCounter; }
    private void addEdge(int from, int to, String label) {
        if (from <= 0 || to <= 0) return;
        List<Edge> edges = getOrCreateEdges(from);
        if (edges.stream().noneMatch(e -> e.getDestinationNodeId() == to)) {
            edges.add(new Edge(to, label));
        }
    }
    private void mapLineToNode(ASTNode node, int nodeId) {
        if (compilationUnit == null || node == null) return;
        int startLine = compilationUnit.getLineNumber(node.getStartPosition());
        int endLine = compilationUnit.getLineNumber(node.getStartPosition() + node.getLength() - 1);
        for (int line = startLine; line <= endLine; line++) { lineToNodeMap.put(line, nodeId); }
        List<Integer> lines = nodeToLinesMap.computeIfAbsent(nodeId, k -> new ArrayList<>());
        for (int line = startLine; line <= endLine; line++) { if (!lines.contains(line)) { lines.add(line); } }
    }
    private String getNodeLabel(ASTNode node) {
        if (node instanceof ReturnStatement) {
            ReturnStatement ret = (ReturnStatement) node;
            return "RETURN" + (ret.getExpression() != null ? ": " + ret.getExpression().toString() : "");
        } else if (node instanceof VariableDeclarationStatement) {
            return "VAR: " + node.toString().trim().replace("\n", "").replace("\r", "");
        } else if (node instanceof ExpressionStatement) {
            return "EXPR: " + ((ExpressionStatement) node).getExpression().toString();
        }
        return node.toString().trim().replace("\n", "").replace("\r", "");
    }
    private List<Edge> getOrCreateEdges(int nodeId) {
        return graphEdges.computeIfAbsent(nodeId, k -> new ArrayList<>());
    }
}