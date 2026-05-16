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
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import br.ufjf.capivara.model.Edge;
/**
 * Visitador AST encarregado de gerar o Grafo de Fluxo de Controle (CFG) de um método.
 * <p>
 * A classe mapeia os nós da sintaxe Java para nós lógicos do grafo (PROCESSING, DECISION, 
 * LOOP_DECISION, SWITCH_DECISION e EXIT), calculando as arestas de transição e correlacionando
 * as linhas físicas do editor de texto aos IDs dos nós gerados.
 * </p>
 */
public class GFCVisitor extends ASTVisitor {

    private CompilationUnit compilationUnit;
    
    private final Map<Integer, List<Edge>> graphEdges = new HashMap<>();
    private final Map<Integer, String> nodeTypes = new HashMap<>();
    private final Map<Integer, String> nodeLabels = new HashMap<>();
    private final Map<Integer, Integer> lineToNodeMap = new HashMap<>();
    private final Map<Integer, List<Integer>> nodeToLinesMap = new TreeMap<>();
    
    private int nodeCounter = 0;
    
    private final Stack<Integer> predecessorStack = new Stack<>();
    
    private Integer currentSequentialNode = null;
    private boolean inSequentialBlock = false;
    
    private String currentMethodReturnType = "";
    
    private final Stack<String> branchLabelStack = new Stack<>();
    private final Set<Integer> decisionNodesWithFalseExit = new HashSet<>();
    
    private final List<Integer> pendingClosingBraceLines = new ArrayList<>();

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
        pendingClosingBraceLines.clear();
    }

    public Map<Integer, List<Edge>> getGraphEdges() { return this.graphEdges; }
    public Map<Integer, String> getNodeTypes() { return this.nodeTypes; }
    public Map<Integer, String> getNodeLabels() { return this.nodeLabels; }
    public Map<Integer, List<Integer>> getNodeToLinesMap() { return this.nodeToLinesMap; }
    public Map<Integer, Integer> getLineToNodeMap() { return lineToNodeMap; }


    @Override
    public boolean visit(MethodDeclaration node) {
        if (node.getReturnType2() != null) {
            currentMethodReturnType = node.getReturnType2().toString();
        } else {
            currentMethodReturnType = "void";
        }
        
        if (node.getBody() != null) {
            node.getBody().accept(this);
        }
        
        if (nodeCounter > 0) {
            int startLine = compilationUnit.getLineNumber(node.getStartPosition());
            int bodyStartLine = compilationUnit.getLineNumber(node.getBody().getStartPosition());
            int endLine = compilationUnit.getLineNumber(node.getStartPosition() + node.getLength() - 1);
            
            for (int i = startLine; i <= bodyStartLine; i++) {
                forceMapLineToNode(i, 1);
            }
            forceMapLineToNode(endLine, 1);
        }
        
        predecessorStack.clear();
        currentMethodReturnType = "";
        currentSequentialNode = null;
        inSequentialBlock = false;
        pendingClosingBraceLines.clear();
        return false;
    }
    /**
     * Processa a estrutura condicional IF-ELSE. Cria ramificações true/false, bifurca o fluxo 
     * empilhando o nó de decisão e intercepta o retorno dos blocos filhos para unificar os caminhos no final.
     */
    @Override
    public boolean visit(IfStatement node) {
        int decisionNode;

        // IF
        if (inSequentialBlock && currentSequentialNode != null) {
            decisionNode = currentSequentialNode;
            String oldLabel = nodeLabels.get(decisionNode);
            nodeLabels.put(decisionNode, oldLabel + "\nIF: " + node.getExpression().toString());
            nodeTypes.put(decisionNode, "DECISION");
            mapLineToNode(node.getExpression(), decisionNode);
            // Encerra bloco sequencial para os filhos
            inSequentialBlock = false;
            currentSequentialNode = null;
        } else {
            finishSequentialBlock();
            decisionNode = createNode("DECISION");
            nodeLabels.put(decisionNode, "IF: " + node.getExpression().toString());
            mapLineToNode(node.getExpression(), decisionNode);
            while (!predecessorStack.isEmpty()) { 
            	addEdge(predecessorStack.pop(), decisionNode, ""); 
            }
        }

        List<Integer> branchEndNodes = new ArrayList<>();
        Statement thenStmt = node.getThenStatement();

        predecessorStack.push(decisionNode);
        branchLabelStack.push("true");
        if (thenStmt != null) { 
        	thenStmt.accept(this); 
        }
        branchLabelStack.pop();
        currentSequentialNode = null;
        inSequentialBlock = false;

        collectBranchEndNodes(decisionNode, branchEndNodes);

    
        if (thenStmt instanceof Block) {
            int thenEndLine = compilationUnit.getLineNumber(thenStmt.getStartPosition() + thenStmt.getLength() - 1);
            if (!lineToNodeMap.containsKey(thenEndLine)) {
                pendingClosingBraceLines.add(thenEndLine);
            }
        }

        Statement elseStmt = node.getElseStatement();
        if (elseStmt != null) {
            //  ELSE
            if (containsControlStructure(elseStmt)) {
                predecessorStack.push(decisionNode);
                branchLabelStack.push("false");
                elseStmt.accept(this);
                branchLabelStack.pop();
                collectBranchEndNodes(decisionNode, branchEndNodes);
            } else {
                String nodeType = determineNodeTypeForStatement(elseStmt);
                int elseNode = createNode(nodeType);
                nodeLabels.put(elseNode, getComprehensiveNodeLabel(elseStmt));
                mapLineToNode(elseStmt, elseNode);
                addEdge(decisionNode, elseNode, "false");
                if (!"EXIT".equals(nodeType)) {
                    branchEndNodes.add(elseNode);
                }
            }
            
            if (elseStmt instanceof Block) {
                int elseEndLine = compilationUnit.getLineNumber(elseStmt.getStartPosition() + elseStmt.getLength() - 1);
                if (!lineToNodeMap.containsKey(elseEndLine)) {
                    pendingClosingBraceLines.add(elseEndLine);
                }
            }
        } else {
            decisionNodesWithFalseExit.add(decisionNode);
            branchEndNodes.add(decisionNode);
        }

        predecessorStack.clear();
        predecessorStack.addAll(branchEndNodes);
        
        currentSequentialNode = null;
        inSequentialBlock = false;
        return false;
    }
    /**
     * Transforma a instrução de retorno em um nó terminal (EXIT).
     */
    @Override
    public boolean visit(ReturnStatement node) {
        int returnNode;
        if (inSequentialBlock && currentSequentialNode != null) {
            returnNode = currentSequentialNode;
            String oldLabel = nodeLabels.get(returnNode);
            String retVal = (node.getExpression() != null) ? node.getExpression().toString() : "";
            String retLabel = "return" + (retVal.isEmpty() ? "" : " " + retVal);
            nodeLabels.put(returnNode, oldLabel + "\n" + retLabel);
            nodeTypes.put(returnNode, "EXIT");
            mapLineToNode(node, returnNode);
        } else {
            finishSequentialBlock();
            returnNode = createNode("EXIT");
            String returnValue = "";
            if (node.getExpression() != null) {
                returnValue = node.getExpression().toString();
            }
            String retType = (currentMethodReturnType != null && !currentMethodReturnType.isEmpty()) ? currentMethodReturnType : "void";
            String label = returnValue.isEmpty() ? retType + " : return" : retType + " : return " + returnValue;
            
            nodeLabels.put(returnNode, label);
            mapLineToNode(node, returnNode);
            
            while(!predecessorStack.isEmpty()){
                addEdge(predecessorStack.pop(), returnNode, "");
            }
        }
        currentSequentialNode = null;
        inSequentialBlock = false;
        return false;
    }
    /**
     * Mapeia a estrutura complexa do SWITCH e Gerencia os (cases), 
     */
    
    @SuppressWarnings({ "unchecked", "deprecation" })
    @Override
    public boolean visit(SwitchStatement node) {
        int switchNode;
        if (inSequentialBlock && currentSequentialNode != null) {
            switchNode = currentSequentialNode;
            String oldLabel = nodeLabels.get(switchNode);
            nodeLabels.put(switchNode, oldLabel + "\nSWITCH (" + node.getExpression().toString() + ")");
            nodeTypes.put(switchNode, "SWITCH_DECISION");
            mapLineToNode(node.getExpression(), switchNode);
            inSequentialBlock = false;
            currentSequentialNode = null;
        } else {
            finishSequentialBlock();
            switchNode = createNode("SWITCH_DECISION");
            nodeLabels.put(switchNode, "SWITCH (" + node.getExpression().toString() + ")");
            mapLineToNode(node.getExpression(), switchNode);
            while (!predecessorStack.isEmpty()) { addEdge(predecessorStack.pop(), switchNode, ""); }
        }

        List<Integer> nodesGoingToExit = new ArrayList<>(); 
        List<String> pendingEdgeLabels = new ArrayList<>();
        List<ASTNode> pendingCaseASTs = new ArrayList<>();
        Integer currentCaseNodeId = null; 
        Integer fallThroughNode = null;

        List<Statement> statements = node.statements();

        if (statements.isEmpty()) {
            nodesGoingToExit.add(switchNode);
        }

        for (Statement stmt : statements) {
            if (stmt instanceof SwitchCase) {
                SwitchCase sc = (SwitchCase) stmt;
                if (currentCaseNodeId != null) {
                    fallThroughNode = currentCaseNodeId;
                    currentCaseNodeId = null;
                }
                String edgeLabel = sc.isDefault() ? "default" : (sc.getExpression() != null ? sc.getExpression().toString() : "?");
                pendingEdgeLabels.add(edgeLabel);
                pendingCaseASTs.add(sc);
            } else {
                if (currentCaseNodeId == null) {
                    currentCaseNodeId = createNode("CASE");
                    nodeLabels.put(currentCaseNodeId, ""); 
                    for (String label : pendingEdgeLabels) { addEdge(switchNode, currentCaseNodeId, label); }
                    for (ASTNode caseAst : pendingCaseASTs) {
                        String caseText = caseAst.toString().trim();
                        String currentLbl = nodeLabels.get(currentCaseNodeId);
                        if (currentLbl.isEmpty()) nodeLabels.put(currentCaseNodeId, caseText);
                        else nodeLabels.put(currentCaseNodeId, currentLbl + "\n" + caseText);
                        mapLineToNode(caseAst, currentCaseNodeId);
                    }
                    if (fallThroughNode != null) {
                        if (!"EXIT".equals(nodeTypes.get(fallThroughNode))) { addEdge(fallThroughNode, currentCaseNodeId, ""); }
                        fallThroughNode = null;
                    }
                    pendingEdgeLabels.clear();
                    pendingCaseASTs.clear();
                }

                String stmtLabel = getNodeLabel(stmt);
                String currentLabel = nodeLabels.get(currentCaseNodeId);
                if (currentLabel.isEmpty()) { nodeLabels.put(currentCaseNodeId, stmtLabel); } 
                else if (!currentLabel.contains(stmtLabel)) { nodeLabels.put(currentCaseNodeId, currentLabel + "\n" + stmtLabel); }
                mapLineToNode(stmt, currentCaseNodeId);

                if (stmt instanceof BreakStatement) {
                    nodesGoingToExit.add(currentCaseNodeId);
                    currentCaseNodeId = null; 
                } else if (stmt instanceof ReturnStatement) {
                    nodeTypes.put(currentCaseNodeId, "EXIT");
                    currentCaseNodeId = null; 
                }
            }
        }

        if (!pendingEdgeLabels.isEmpty()) {
             int finalNodeId = createNode("CASE");
             StringBuilder sb = new StringBuilder();
             for (ASTNode caseAst : pendingCaseASTs) {
                 if (sb.length() > 0) sb.append("\n");
                 sb.append(caseAst.toString().trim());
                 mapLineToNode(caseAst, finalNodeId);
             }
             nodeLabels.put(finalNodeId, sb.toString());
             for (String label : pendingEdgeLabels) { addEdge(switchNode, finalNodeId, label); }
             if (fallThroughNode != null && !"EXIT".equals(nodeTypes.get(fallThroughNode))) {
                 addEdge(fallThroughNode, finalNodeId, "");
             }
             nodesGoingToExit.add(finalNodeId);
        } else {
             if (currentCaseNodeId != null && !"EXIT".equals(nodeTypes.get(currentCaseNodeId))) {
                nodesGoingToExit.add(currentCaseNodeId);
             }
             if (fallThroughNode != null && !"EXIT".equals(nodeTypes.get(fallThroughNode))) {
                nodesGoingToExit.add(fallThroughNode);
             }
        }

        finishSequentialBlock(); 
        predecessorStack.clear();
        predecessorStack.addAll(nodesGoingToExit);
        currentSequentialNode = null;
        inSequentialBlock = false;
        return false;
    }
    /**
     * Processa laços WHILE. Cria um nó de decisão de looping e realimenta a origem do laço
     * com as arestas coletadas ao fim da execução do bloco interno.
     */
    @Override
    public boolean visit(WhileStatement node) {
        int decisionNode;
        if (inSequentialBlock && currentSequentialNode != null) {
            decisionNode = currentSequentialNode;
            String oldLabel = nodeLabels.get(decisionNode);
            nodeLabels.put(decisionNode, oldLabel + "\nWHILE: " + node.getExpression().toString());
            nodeTypes.put(decisionNode, "LOOP_DECISION");
            mapLineToNode(node.getExpression(), decisionNode);
            inSequentialBlock = false;
            currentSequentialNode = null;
        } else {
            finishSequentialBlock(); 
            decisionNode = createNode("LOOP_DECISION");
            nodeLabels.put(decisionNode, "WHILE: " + node.getExpression().toString());
            mapLineToNode(node.getExpression(), decisionNode);
            while(!predecessorStack.isEmpty()) { addEdge(predecessorStack.pop(), decisionNode, ""); }
        }
        
        decisionNodesWithFalseExit.add(decisionNode);
        Stack<Integer> bodyPredecessors = new Stack<>();
        bodyPredecessors.push(decisionNode);

        branchLabelStack.push("true");
        visitLoopBody(node.getBody(), bodyPredecessors);
        branchLabelStack.pop();
        
        if (node.getBody() instanceof Block) {
            int endLine = compilationUnit.getLineNumber(node.getBody().getStartPosition() + node.getBody().getLength() - 1);
            pendingClosingBraceLines.remove(Integer.valueOf(endLine));
            lineToNodeMap.remove(endLine); 
            pendingClosingBraceLines.add(endLine);
        }

        for (Integer bodyEndNode : bodyPredecessors) { 
            if (!"EXIT".equals(nodeTypes.get(bodyEndNode))) addEdge(bodyEndNode, decisionNode, ""); 
        }
        predecessorStack.clear();
        predecessorStack.push(decisionNode);
        currentSequentialNode = null;
        inSequentialBlock = false;
        return false;
    }
    
    /**
     * Processa laços FOR convencionais. Isola a inicialização, a condição e 
     * cria um nó de incremento (PROCESSING) que faz a ponte do looping.
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean visit(ForStatement node) {
        finishSequentialBlock();
        int decisionNode = createNode("LOOP_DECISION");
        
        String init = ((List<ASTNode>)node.initializers()).stream().map(ASTNode::toString).collect(Collectors.joining(", "));
        String cond = node.getExpression() == null ? "" : node.getExpression().toString();
        String update = ((List<ASTNode>)node.updaters()).stream().map(ASTNode::toString).collect(Collectors.joining(", "));
        
        nodeLabels.put(decisionNode, String.format("FOR (%s; %s; %s)", init, cond, update));
        
        int startLine = compilationUnit.getLineNumber(node.getStartPosition());
        mapSingleLineToNode(startLine, decisionNode);
        if (node.getExpression() != null) mapLineToNode(node.getExpression(), decisionNode);
        
        while(!predecessorStack.isEmpty()) { addEdge(predecessorStack.pop(), decisionNode, ""); }

        decisionNodesWithFalseExit.add(decisionNode);
        
        Stack<Integer> bodyPredecessors = new Stack<>();
        bodyPredecessors.push(decisionNode);
        
        Stack<Integer> globalStack = new Stack<>();
        globalStack.addAll(this.predecessorStack);
        this.predecessorStack.clear();
        this.predecessorStack.addAll(bodyPredecessors);
        
        branchLabelStack.push("true");
        if (node.getBody() != null) { node.getBody().accept(this); }
        branchLabelStack.pop();
        
        int endLine = compilationUnit.getLineNumber(node.getStartPosition() + node.getLength() - 1);
        pendingClosingBraceLines.remove(Integer.valueOf(endLine));

        Stack<Integer> currentBodyEnds = new Stack<>();
        currentBodyEnds.addAll(this.predecessorStack);

        int incrementNode = createNode("PROCESSING");
        nodeLabels.put(incrementNode, update.isEmpty() ? "inc" : update);
        
        lineToNodeMap.remove(endLine); 
        forceMapLineToNode(endLine, incrementNode);
        
        while (!currentBodyEnds.isEmpty()) {
            int p = currentBodyEnds.pop();
            if (!"EXIT".equals(nodeTypes.get(p))) {
                addEdge(p, incrementNode, "");
            }
        }
        
        addEdge(incrementNode, decisionNode, "");
        
        this.predecessorStack.clear();
        this.predecessorStack.addAll(globalStack);
        this.predecessorStack.push(decisionNode);
        
        currentSequentialNode = null;
        inSequentialBlock = false;
        return false;
    }
    /**
     * Processa estruturas DO-WHILE garantindo que o corpo seja percorrido antes do nó 
     * de validação.
     */
    @Override
    public boolean visit(DoStatement node) {
        finishSequentialBlock();
        
        int predecessor = predecessorStack.isEmpty() ? -1 : predecessorStack.pop();
        int loopNode = createNode("LOOP_DECISION");
        nodeLabels.put(loopNode, "DO-WHILE LOOP");
        mapSingleLineToNode(compilationUnit.getLineNumber(node.getStartPosition()), loopNode);
        
        if (predecessor != -1) { addEdge(predecessor, loopNode, ""); }
        
        currentSequentialNode = loopNode;
        inSequentialBlock = true;
        
        if (node.getBody() != null) {
            node.getBody().accept(this);
        }
        
        String condExpr = node.getExpression() != null ? node.getExpression().toString() : "";
        String currentLbl = nodeLabels.get(loopNode);
        if (!currentLbl.contains("WHILE")) {
            nodeLabels.put(loopNode, currentLbl + "\nWHILE (" + condExpr + ")");
        }
        mapLineToNode(node.getExpression(), loopNode);
        
        int endPos = node.getStartPosition() + node.getLength() - 1;
        mapSingleLineToNode(compilationUnit.getLineNumber(endPos), loopNode);
        
        if (node.getBody() instanceof Block) {
             int bodyEnd = node.getBody().getStartPosition() + node.getBody().getLength() - 1;
             mapSingleLineToNode(compilationUnit.getLineNumber(bodyEnd), loopNode);
        }
        
        addEdge(loopNode, loopNode, "true");
        decisionNodesWithFalseExit.add(loopNode);
        
        predecessorStack.clear();
        predecessorStack.push(loopNode);
        
        currentSequentialNode = null;
        inSequentialBlock = false;
        
        return false;
    }

    @Override
    public boolean visit(VariableDeclarationStatement node) { 
    	handleSequentialStatement(node); 
    	return false; 
    }

    @Override
    public boolean visit(ExpressionStatement node) { 
    	handleSequentialStatement(node); 
    	return false; 
    }
    /**
     * Centraliza o tratamento de comandos puramente sequenciais. 
     * Junta declarações ou expressões seguidas acumulando-as sob o mesmo nó conceitual "PROCESSING".
     */
    private void handleSequentialStatement(ASTNode node) {
        if (!inSequentialBlock || currentSequentialNode == null) {
            finishSequentialBlock();
            currentSequentialNode = createNode("PROCESSING");
            nodeLabels.put(currentSequentialNode, getNodeLabel(node));
            while(!predecessorStack.isEmpty()){ 
                addEdge(predecessorStack.pop(), currentSequentialNode, ""); 
            }
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


    private void collectBranchEndNodes(int decisionNode, List<Integer> targetList) {
        while (!predecessorStack.isEmpty() && predecessorStack.peek() != decisionNode) { 
            int p = predecessorStack.pop();
            if (!"EXIT".equals(nodeTypes.get(p))) {
                targetList.add(p);
            }
        }
        if (!predecessorStack.isEmpty() && predecessorStack.peek() == decisionNode) {
            predecessorStack.pop();
        }
    }

    private void visitLoopBody(Statement body, Stack<Integer> localPredecessorStack) {
        Stack<Integer> globalStack = new Stack<>();
        globalStack.addAll(this.predecessorStack);
        this.predecessorStack.clear();
        this.predecessorStack.addAll(localPredecessorStack);
        
        if (body != null) { body.accept(this); }
        
        if (body != null) {
            int endLine = compilationUnit.getLineNumber(body.getStartPosition() + body.getLength() - 1);
            if (!lineToNodeMap.containsKey(endLine)) {
                pendingClosingBraceLines.add(endLine);
            }
        }

        localPredecessorStack.clear();
        localPredecessorStack.addAll(this.predecessorStack);
        this.predecessorStack.clear();
        this.predecessorStack.addAll(globalStack);
    }

    private void finishSequentialBlock() {
        if (inSequentialBlock && currentSequentialNode != null) { inSequentialBlock = false; }
    }
    /**
     * Registra e instancia um novo identificador numérico de nó no grafo, resolvendo também 
     * pendências acumuladas de mapeamento de linhas anteriores (como fechamento de chaves).
     */
    private int createNode(String type) {
        nodeCounter++;
        nodeTypes.put(nodeCounter, type);
        
        if (!pendingClosingBraceLines.isEmpty()) {
            for (int line : pendingClosingBraceLines) {
                if (!lineToNodeMap.containsKey(line)) {
                    mapSingleLineToNode(line, nodeCounter);
                }
            }
            pendingClosingBraceLines.clear();
        }
        return nodeCounter;
    }
    /**
     * Interconecta dois nós gerando um objeto {@link Edge}, inferindo automaticamente 
     * rótulos condicionais contextuais baseados no topo da pilha de controle da ramificação.
     */
    private void addEdge(int from, int to, String label) {
        if (from <= 0 || to <= 0) return;
        String effectiveLabel = label;
        String fromType = nodeTypes.get(from);
        if ((effectiveLabel == null || effectiveLabel.isEmpty())
                && ( "DECISION".equals(fromType) || "LOOP_DECISION".equals(fromType) )) {
            if (!branchLabelStack.isEmpty()) { effectiveLabel = branchLabelStack.peek(); }
            else if (decisionNodesWithFalseExit.contains(from)) { effectiveLabel = "false"; }
            else { effectiveLabel = ""; }
        }
        List<Edge> edges = getOrCreateEdges(from);
        int existingIndex = -1;
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getDestinationNodeId() == to) { existingIndex = i; break; }
        }
        if (existingIndex == -1) { edges.add(new Edge(to, effectiveLabel)); }
        else {
            Edge existing = edges.get(existingIndex);
            String existingLabel = existing.getLabel();
            boolean existingEmpty = (existingLabel == null || existingLabel.isEmpty());
            boolean newNonEmpty = (effectiveLabel != null && !effectiveLabel.isEmpty());
            if (existingEmpty && newNonEmpty) {
                edges.remove(existingIndex);
                edges.add(new Edge(to, effectiveLabel));
            }
        }
    }

    private void mapLineToNode(ASTNode node, int nodeId) {
        if (compilationUnit == null || node == null) return;
        int startLine = compilationUnit.getLineNumber(node.getStartPosition());
        int endLine = compilationUnit.getLineNumber(node.getStartPosition() + node.getLength() - 1);
        for (int line = startLine; line <= endLine; line++) {
            if (lineToNodeMap.containsKey(line)) {
                int existingId = lineToNodeMap.get(line);
                String existingType = nodeTypes.get(existingId);
                String newType = nodeTypes.get(nodeId);
                if (("DECISION".equals(existingType) || "LOOP_DECISION".equals(existingType)) 
                        && "PROCESSING".equals(newType)) {
                    continue;
                }
            }
            lineToNodeMap.put(line, nodeId);
            List<Integer> lines = nodeToLinesMap.computeIfAbsent(nodeId, k -> new ArrayList<>());
            if (!lines.contains(line)) { lines.add(line); }
        }
    }

    private void forceMapLineToNode(int line, int nodeId) {
        if (compilationUnit == null) return;
        lineToNodeMap.put(line, nodeId);
        List<Integer> lines = nodeToLinesMap.computeIfAbsent(nodeId, k -> new ArrayList<>());
        if (!lines.contains(line)) lines.add(0, line);
    }
    
    private void mapSingleLineToNode(int line, int nodeId) {
        if (compilationUnit == null) return;
        if (lineToNodeMap.containsKey(line)) {
             int existingId = lineToNodeMap.get(line);
             String existingType = nodeTypes.get(existingId);
             String newType = nodeTypes.get(nodeId);
             if (("DECISION".equals(existingType) || "LOOP_DECISION".equals(existingType)) 
                     && "PROCESSING".equals(newType)) {
                 return;
             }
        }
        lineToNodeMap.put(line, nodeId);
        List<Integer> lines = nodeToLinesMap.computeIfAbsent(nodeId, k -> new ArrayList<>());
        if (!lines.contains(line)) lines.add(line);
    }

    private String getNodeLabel(ASTNode node) {
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
            if (labelBuilder.length() > 0) labelBuilder.setLength(labelBuilder.length() - 1);
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
        if (statement instanceof SwitchStatement) return "SWITCH_DECISION";
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