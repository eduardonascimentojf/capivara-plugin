package br.ufjf.capivara.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import br.ufjf.capivara.model.Edge;

public class GFCVisitor extends ASTVisitor {

    private CompilationUnit compilationUnit;
    private final Map<Integer, List<Edge>> graphEdges = new HashMap<>();
    private final Map<Integer, String> nodeTypes = new HashMap<>();
    private final Map<Integer, String> nodeLabels = new HashMap<>();
    private int nodeCounter = 0;
    private final Stack<Integer> predecessorStack = new Stack<>();
    private final Map<Integer, Integer> lineToNodeMap = new HashMap<>();
    private final Map<Integer, List<Integer>> nodeToLinesMap = new TreeMap<>();
    private Integer currentSequentialNode = null;
    private boolean inSequentialBlock = false;
    private String currentMethodReturnType = "";

    // nova pilha para sinalizar qual rótulo aplicar às arestas de decisão enquanto visitamos um ramo
    private final Stack<String> branchLabelStack = new Stack<>();
    // conjunto para marcar nós de decisão que possuem saída "false" (fall-through / exit do ramo)
    private final Set<Integer> decisionNodesWithFalseExit = new HashSet<>();

    private static class ControlStructureVisitor extends ASTVisitor {
        private boolean hasControlStructure = false;
        @Override public boolean visit(IfStatement node) { hasControlStructure = true; return false; }
        @Override public boolean visit(WhileStatement node) { hasControlStructure = true; return false; }
        @Override public boolean visit(ForStatement node) { hasControlStructure = true; return false; }
        @Override public boolean visit(EnhancedForStatement node) { hasControlStructure = true; return false; }
        @Override public boolean visit(DoStatement node) { hasControlStructure = true; return false; }
        @Override public boolean visit(SwitchStatement node) { hasControlStructure = true; return false; }
        public boolean hasControlStructure() { return hasControlStructure; }
    }

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
        currentMethodReturnType = "";
        branchLabelStack.clear();
        decisionNodesWithFalseExit.clear();
    }

    public Map<Integer, List<Edge>> getGraphEdges() { return this.graphEdges; }
    public Map<Integer, String> getNodeTypes() { return this.nodeTypes; }
    public Map<Integer, String> getNodeLabels() { return this.nodeLabels; }
    public Map<Integer, List<Integer>> getNodeToLinesMap() { return this.nodeToLinesMap; }
    public Map<Integer, Integer> getLineToNodeMap() { return lineToNodeMap; }

    @Override
    public boolean visit(MethodDeclaration node) {
        int methodNodeId = createNode("ENTRY");
        String methodName = node.getName().getIdentifier();
        if (node.getReturnType2() != null) {
            currentMethodReturnType = node.getReturnType2().toString();
        } else {
            currentMethodReturnType = "void";
        }
        @SuppressWarnings("unchecked")
        String params = ((List<SingleVariableDeclaration>) node.parameters()).stream()
                .map(p -> p.getType().toString() + " " + p.getName().getIdentifier())
                .collect(Collectors.joining(", "));
        String fullLabel = "Método: " + methodName + "(" + params + ")";
        nodeLabels.put(methodNodeId, fullLabel);
        mapLineToNode(node, methodNodeId);
        predecessorStack.push(methodNodeId);
        if (node.getBody() != null) {
            node.getBody().accept(this);
        }
        predecessorStack.clear();
        currentMethodReturnType = "";
        return false;
    }
    
    @Override
    public boolean visit(IfStatement node) {
        finishSequentialBlock();
        int decisionNode = createNode("DECISION");
        nodeLabels.put(decisionNode, "IF: " + node.getExpression().toString());
        mapLineToNode(node.getExpression(), decisionNode);
    
        while (!predecessorStack.isEmpty()) { addEdge(predecessorStack.pop(), decisionNode, ""); }

        List<Integer> branchEndNodes = new ArrayList<>();
        Statement thenStmt = node.getThenStatement();

        // VISIT THEN BRANCH (rotular arestas de decision -> then com "true")
        predecessorStack.push(decisionNode);
        branchLabelStack.push("true");
        if (thenStmt != null) { thenStmt.accept(this); }
        branchLabelStack.pop();

        // coletar nós finais do branch THEN (tudo que foi empilhado além do decisionNode)
        while (!predecessorStack.isEmpty() && predecessorStack.peek() != decisionNode) { branchEndNodes.add(predecessorStack.pop()); }
        if (!predecessorStack.isEmpty()) predecessorStack.pop();

        if (thenStmt instanceof Block && node.getElseStatement() == null) {
            int thenEndPosition = thenStmt.getStartPosition() + thenStmt.getLength() - 1;
            int thenEndLine = compilationUnit.getLineNumber(thenEndPosition);
            mapSingleLineToNode(thenEndLine, decisionNode);
        }

        Statement elseStmt = node.getElseStatement();
        if (elseStmt != null) {
            if (containsControlStructure(elseStmt)) {
                // VISIT ELSE BRANCH (rotular arestas de decision -> else com "false")
                predecessorStack.push(decisionNode);
                branchLabelStack.push("false");
                elseStmt.accept(this);
                branchLabelStack.pop();

                while (!predecessorStack.isEmpty() && predecessorStack.peek() != decisionNode) { branchEndNodes.add(predecessorStack.pop()); }
                if (!predecessorStack.isEmpty()) predecessorStack.pop();
            } else {
                // else simples: criamos um nó e ligamos com label "false"
                String nodeType = determineNodeTypeForStatement(elseStmt);
                int elseNode = createNode(nodeType);
                nodeLabels.put(elseNode, getComprehensiveNodeLabel(elseStmt));
                mapLineToNode(elseStmt, elseNode);
                addEdge(decisionNode, elseNode, "false");
                branchEndNodes.add(elseNode);
            }
        } else {
            // se não há else, o próprio decisionNode representa o caminho "false" (fall-through).
            // marcamos o nó para que, quando for usado como predecessor para o próximo nó, a aresta seja rotulada como "false".
            decisionNodesWithFalseExit.add(decisionNode);
            branchEndNodes.add(decisionNode);
        }

        predecessorStack.clear();
        predecessorStack.addAll(branchEndNodes);
        currentSequentialNode = null;
        inSequentialBlock = false;
        return false;
    }

    @Override
    public boolean visit(ReturnStatement node) {
        finishSequentialBlock();
        int returnNode = createNode("EXIT");
        String returnValue = "";
        if (node.getExpression() != null) {
            returnValue = node.getExpression().toString();
        }
        String retType = (currentMethodReturnType != null && !currentMethodReturnType.isEmpty()) ? currentMethodReturnType : "void";
        String label;
        if (returnValue.isEmpty()) {
            label = retType + " : return";
        } else {
            label = retType + " : return " + returnValue;
        }
        nodeLabels.put(returnNode, label);
        mapLineToNode(node, returnNode);
        while(!predecessorStack.isEmpty()){
            addEdge(predecessorStack.pop(), returnNode, "");
        }
        currentSequentialNode = null;
        inSequentialBlock = false;
        return false;
    }

    @Override
    public boolean visit(WhileStatement node) {
        finishSequentialBlock();
        int decisionNode = createNode("LOOP_DECISION");
        nodeLabels.put(decisionNode, "WHILE: " + node.getExpression().toString());
        mapLineToNode(node, decisionNode);
        while(!predecessorStack.isEmpty()) { addEdge(predecessorStack.pop(), decisionNode, ""); }

        // loop: saída para o corpo deve ser rotulada "true"
        decisionNodesWithFalseExit.add(decisionNode);
        Stack<Integer> bodyPredecessors = new Stack<>();
        bodyPredecessors.push(decisionNode);

        // sinaliza rótulo "true" ao visitar o corpo
        branchLabelStack.push("true");
        visitLoopBody(node.getBody(), bodyPredecessors);
        branchLabelStack.pop();

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

        // loop: saída para o corpo deve ser rotulada "true"
        decisionNodesWithFalseExit.add(decisionNode);
        Stack<Integer> bodyPredecessors = new Stack<>();
        bodyPredecessors.push(decisionNode);

        // sinaliza rótulo "true" ao visitar o corpo
        branchLabelStack.push("true");
        visitLoopBody(node.getBody(), bodyPredecessors);
        branchLabelStack.pop();

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
        nodeLabels.put(bodyEntryNode, "DO");
        mapLineToNode(node, bodyEntryNode);
        if (predecessor != -1) { addEdge(predecessor, bodyEntryNode, ""); }
        Stack<Integer> bodyPredecessors = new Stack<>();
        bodyPredecessors.push(bodyEntryNode);
        visitLoopBody(node.getBody(), bodyPredecessors);
        int decisionNode = createNode("LOOP_DECISION");
        String condExpr = node.getExpression() != null ? node.getExpression().toString() : "";
        nodeLabels.put(decisionNode, "DO-WHILE: " + condExpr);
        mapLineToNode(node.getExpression(), decisionNode);
        for (Integer bodyEndNode : bodyPredecessors) { addEdge(bodyEndNode, decisionNode, ""); }
        addEdge(decisionNode, bodyEntryNode, "true");

        // do-while também tem saída "false" para o caminho de continuação
        decisionNodesWithFalseExit.add(decisionNode);

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
        String paramName = node.getParameter().getName().getIdentifier();
        String expr = node.getExpression() != null ? node.getExpression().toString() : "<expr>";
        nodeLabels.put(decisionNode, "FOR-EACH: " + paramName + " in " + expr);
        mapLineToNode(node, decisionNode);
        while(!predecessorStack.isEmpty()) { addEdge(predecessorStack.pop(), decisionNode, ""); }

        // loop: saída para o corpo deve ser rotulada "true"
        decisionNodesWithFalseExit.add(decisionNode);
        Stack<Integer> bodyPredecessors = new Stack<>();
        bodyPredecessors.push(decisionNode);

        // sinaliza rótulo "true" ao visitar o corpo
        branchLabelStack.push("true");
        visitLoopBody(node.getBody(), bodyPredecessors);
        branchLabelStack.pop();

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

    private void handleSequentialStatement(ASTNode node) {
        if (!inSequentialBlock || currentSequentialNode == null) {
            finishSequentialBlock();
            currentSequentialNode = createNode("PROCESSING");
            nodeLabels.put(currentSequentialNode, getNodeLabel(node));
            // quando conectamos predecessores ao novo nó sequencial, addEdge decidirá automaticamente
            // se deve usar "true"/"false" conforme o contexto (branchLabelStack / decisionNodesWithFalseExit).
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

    private int createNode(String type) {
        nodeCounter++;
        nodeTypes.put(nodeCounter, type);
        return nodeCounter;
    }

    /**
     * addEdge agora decide o rótulo quando label == "" e o nó 'from' é DECISION/LOOP_DECISION:
     * - se houver um label atual em branchLabelStack, usa esse (tipicamente "true" ou "false")
     * - senão, se o nó estiver marcado em decisionNodesWithFalseExit, usa "false"
     * - caso contrário, mantém vazio
     *
     * Observação: aqui tratamos o caso da classe Edge ser imutável — se já existir uma aresta para o mesmo destino
     * sem label e precisarmos atualizá-la, removemos e recriamos a aresta com o novo label.
     */
    private void addEdge(int from, int to, String label) {
        if (from <= 0 || to <= 0) return;
        String effectiveLabel = label;
        String fromType = nodeTypes.get(from);
        if ((effectiveLabel == null || effectiveLabel.isEmpty())
                && ( "DECISION".equals(fromType) || "LOOP_DECISION".equals(fromType) )) {
            if (!branchLabelStack.isEmpty()) {
                effectiveLabel = branchLabelStack.peek();
            } else if (decisionNodesWithFalseExit.contains(from)) {
                effectiveLabel = "false";
            } else {
                effectiveLabel = "";
            }
        }

        List<Edge> edges = getOrCreateEdges(from);
        // procura se já existe uma aresta para 'to'
        int existingIndex = -1;
        for (int i = 0; i < edges.size(); i++) {
            Edge e = edges.get(i);
            if (e.getDestinationNodeId() == to) {
                existingIndex = i;
                break;
            }
        }

        if (existingIndex == -1) {
            // não existe: adiciona nova aresta com effectiveLabel (pode ser vazio)
            edges.add(new Edge(to, effectiveLabel));
        } else {
            // já existe uma aresta; checar se precisa atualizar label
            Edge existing = edges.get(existingIndex);
            String existingLabel = existing.getLabel();
            boolean existingEmpty = (existingLabel == null || existingLabel.isEmpty());
            boolean newNonEmpty = (effectiveLabel != null && !effectiveLabel.isEmpty());
            if (existingEmpty && newNonEmpty) {
                // remove a antiga e coloca a nova com label (para suportar Edge imutável)
                edges.remove(existingIndex);
                edges.add(new Edge(to, effectiveLabel));
            }
            // se já existe label (ou novo label vazio), mantemos a aresta como está
        }
    }

    private void mapLineToNode(ASTNode node, int nodeId) {
        if (compilationUnit == null || node == null) return;
        int startLine = compilationUnit.getLineNumber(node.getStartPosition());
        int endLine = compilationUnit.getLineNumber(node.getStartPosition() + node.getLength() - 1);
        for (int line = startLine; line <= endLine; line++) {
            lineToNodeMap.put(line, nodeId);
        }
        List<Integer> lines = nodeToLinesMap.computeIfAbsent(nodeId, k -> new ArrayList<>());
        for (int line = startLine; line <= endLine; line++) {
            if (!lines.contains(line)) {
                lines.add(line);
            }
        }
    }

    private void mapSingleLineToNode(int line, int nodeId) {
        if (compilationUnit == null) return;
        lineToNodeMap.put(line, nodeId);
        List<Integer> lines = nodeToLinesMap.computeIfAbsent(nodeId, k -> new ArrayList<>());
        if (!lines.contains(line)) {
            lines.add(line);
        }
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

    private String getComprehensiveNodeLabel(Statement statement) {
        if (statement instanceof Block) {
            StringBuilder labelBuilder = new StringBuilder();
            Block block = (Block) statement;
            for (Object st : block.statements()) {
                if (st instanceof ASTNode) {
                    labelBuilder.append(getNodeLabel((ASTNode) st)).append("\n");
                }
            }
            if (labelBuilder.length() > 0) {
                labelBuilder.setLength(labelBuilder.length() - 1);
            }
            return labelBuilder.toString();
        }
        return getNodeLabel(statement);
    }

    private boolean containsControlStructure(Statement stmt) {
        if (stmt == null) return false;
        if (stmt instanceof IfStatement) return true;
        ControlStructureVisitor visitor = new ControlStructureVisitor();
        stmt.accept(visitor);
        return visitor.hasControlStructure();
    }

    private String determineNodeTypeForStatement(Statement statement) {
        if (statement instanceof ReturnStatement) return "EXIT";
        if (statement instanceof WhileStatement || statement instanceof ForStatement ||
            statement instanceof DoStatement || statement instanceof EnhancedForStatement) {
            return "LOOP_DECISION";
        }
        if (statement instanceof IfStatement) return "DECISION";
        if (statement instanceof Block) {
            Block block = (Block) statement;
            if (block.statements().size() == 1) {
                Statement singleStatement = (Statement) block.statements().get(0);
                return determineNodeTypeForStatement(singleStatement);
            }
        }
        return "PROCESSING";
    }

    private List<Edge> getOrCreateEdges(int nodeId) {
        return graphEdges.computeIfAbsent(nodeId, k -> new ArrayList<>());
    }
}
