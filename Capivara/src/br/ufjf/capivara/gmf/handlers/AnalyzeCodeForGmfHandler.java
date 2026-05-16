package br.ufjf.capivara.gmf.handlers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NodeFinder;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;

// Imports ELK
import org.eclipse.elk.core.RecursiveGraphLayoutEngine;
import org.eclipse.elk.core.options.CoreOptions;
import org.eclipse.elk.core.options.Direction;
import org.eclipse.elk.core.options.EdgeRouting;
import org.eclipse.elk.core.service.IDiagramLayoutConnector;
import org.eclipse.elk.core.service.LayoutConnectorsService;
import org.eclipse.elk.core.service.LayoutMapping;
import org.eclipse.elk.core.util.BasicProgressMonitor;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.properties.Property;

// Imports GMF/GEF
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;

// Imports Capivara
import br.ufjf.capivara.analyzer.GFCVisitor;
import br.ufjf.capivara.model.Edge;
import br.ufjf.capivara.util.CapivaraCoverageCache;
import gfc.Flowchart;
import gfc.GfcFactory;
import gfc.Node;
import gfc.diagram.edit.parts.FlowchartEditPart;
import gfc.diagram.part.GfcDiagramEditorUtil;


/**
 * Handler responsável por orquestrar a geração e renderização do modelo gráfico GMF.
 * <p>
 * O fluxo consiste em extrair a AST do método Java selecionado, convertê-la em nós e 
 * arestas do metamodelo GFC, mapear dados de cobertura do cache, 
 * gerar os arquivos físicos de modelagem e disparar o motor de layout automático ELK.
 * </p>
 */
public class AnalyzeCodeForGmfHandler extends AbstractHandler {

    @SuppressWarnings("deprecation")
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        String commandId = event.getCommand().getId();
        boolean forceCleanGraph = commandId.endsWith(".analyzeForGmf");
        boolean setNodeMode = !commandId.endsWith(".analyzeEdgeCoverage");

        FlowchartEditPart.SHOW_NODE_COVERAGE = setNodeMode;

        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
        IWorkbenchPage page = window.getActivePage();
        IEditorPart activeEditor = page.getActiveEditor();
        ISelection selection = activeEditor.getSite().getSelectionProvider().getSelection();
        ITextSelection textSelection = (ITextSelection) selection;
        IFile currentFile = activeEditor.getEditorInput().getAdapter(IFile.class);
        String currentFilePath = currentFile.getFullPath().toString();
        
        ICompilationUnit javaUnit = JavaCore.createCompilationUnitFrom(currentFile);
        ASTParser parser = ASTParser.newParser(AST.JLS17); 
        parser.setSource(javaUnit);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setResolveBindings(true);
        CompilationUnit astRoot = (CompilationUnit) parser.createAST(null);
        
        MethodDeclaration methodNode = findSelectedMethod(astRoot, textSelection.getOffset(), textSelection.getLength());

        if (methodNode == null) {
            MessageDialog.openInformation(
                window.getShell(), 
                "Capivara GMF - Method Not Found", 
                "We couldn't identify a Java method in your current selection.\n\n" +
                "Quick Tips:\n" +
                "• Click directly on the METHOD NAME and try again (fastest way).\n" +
                "• Or select the entire method block (from the signature to the closing brace).\n" +
                "• Avoid selecting only comments or blank spaces outside the method's scope."
            );
            return null;
        }

        GFCVisitor visitor = new GFCVisitor();
        visitor.setup(astRoot);
        methodNode.accept(visitor);
        
        GfcFactory factory = GfcFactory.eINSTANCE;
        Flowchart flowchartModel = factory.createFlowchart();
        Map<Integer, Node> createdNodes = new HashMap<>(); 
        Map<Integer, String> nodeTypes = visitor.getNodeTypes();
        CapivaraCoverageCache coverageCache = CapivaraCoverageCache.getInstance();

        // 1. Criar nós e preencher dados
        for (Map.Entry<Integer, String> entry : nodeTypes.entrySet()) {
            Integer nodeId = entry.getKey();
            String type = entry.getValue();
            Node newNode = createTypedNode(factory, type);
            newNode.setLabel(visitor.getNodeLabels().get(nodeId));
            newNode.setId(nodeId); 
            String label = visitor.getNodeLabels().get(nodeId);
            List<Integer> lines = visitor.getNodeToLinesMap().get(nodeId);
            if (lines != null && !lines.isEmpty()) {
                newNode.setLineNumber(lines.get(0));
            }
            
            int finalStatus = 0;
            if (lines != null && !lines.isEmpty() && !forceCleanGraph && coverageCache.temDados()) {
            	finalStatus = calculateStatusFromLines(coverageCache, currentFilePath, lines, type, label);            }
            newNode.setCoverageStatus(finalStatus);
            flowchartModel.getNodes().add(newNode);
            createdNodes.put(nodeId, newNode);
        }

        // 2. Propagação (x++)
        if (!forceCleanGraph && coverageCache.temDados()) {
            for (Node node : createdNodes.values()) {
                if (node.getCoverageStatus() == 0) {
                    node.setCoverageStatus(findPredecessorStatus(node.getId(), visitor, createdNodes));
                }
            }
        }

        // 3. Shift para Edge Coverage (Status 4, 5, 6)
        if (!setNodeMode) {
            for (Node n : flowchartModel.getNodes()) {
                if (n.getCoverageStatus() > 0 && n.getCoverageStatus() <= 3) 
                    n.setCoverageStatus(n.getCoverageStatus() + 3);
            }
        }
        
        // 4. Arestas usadno a heuriistica do bypass
        Map<Integer, List<Edge>> graphEdges = visitor.getGraphEdges();
        for (Map.Entry<Integer, List<Edge>> entry : graphEdges.entrySet()) {
            Node fromNode = createdNodes.get(entry.getKey());
            for (Edge oldEdge : entry.getValue()) {
                Node toNode = createdNodes.get(oldEdge.getDestinationNodeId());
                if (fromNode != null && toNode != null) {
                    gfc.Edge newEdge = factory.createEdge();
                    newEdge.setSource(fromNode);
                    newEdge.setTarget(toNode);
                    newEdge.setLabel(oldEdge.getLabel());

                    if (!setNodeMode) {
                        boolean traversed = false;
                        if (fromNode.getCoverageStatus() == 4) { // Verde
                            traversed = true;
                        } else if (fromNode.getCoverageStatus() == 5) { // Amarelo
                            if (toNode.getId() > fromNode.getId() + 1) { 
                                Node bodyNode = createdNodes.get(fromNode.getId() + 1);
                                traversed = !(bodyNode != null && bodyNode.getCoverageStatus() > 0);
                            } else {
                                traversed = (toNode.getCoverageStatus() > 0);
                            }
                        }
                        if (!traversed) {
                        	newEdge.setLabel(oldEdge.getLabel() != null ? oldEdge.getLabel() : "");                        }
                    }
                    flowchartModel.getEdges().add(newEdge);
                }
            }
        }

        saveAndOpen(page, currentFile, flowchartModel);
        return null;
    }

    /**
     * Calcula o status consolidado do nó avaliando todas as linhas vinculadas a ele.
     * Diferencia a avaliação entre nós de Decisão (IF/SWITCH) e nós de Processamento.
     */
    private int calculateStatusFromLines(CapivaraCoverageCache cache, String path, List<Integer> lines, String type, String label) {
        boolean v = false, a = false, r = false;
        boolean temInformacao = false;

        for (int l : lines) { // passa por todas as linhas do no
            int s = cache.getStatusLinha(path, l); // Status do cache
            if (s > 0) temInformacao = true;
            
            if (s == 1) {
            	v = true;  // Verde
            }else if (s == 2) {
            	a = true; // Amarelo
            }else if (s == 3) {
            	r = true; // Vermelho
            }
        }

      // Usado0 para o incremento não ficar como no vermelho
        if (label != null && (label.equals("inc") || label.contains("++"))) {
            if (!temInformacao) {
                return 0; 
            }
        }

        if (type.contains("DECISION") || type.contains("SWITCH")) {
            if (a || (v && r)) {
            	return 2; // Parcial
            }
            if (v) {
            	return 1;  // Total
            }
            if (r) {
            	return 3;  // Não executado
            }
            return 0;
        } 
        
        else {

            if (v) 
            	return 1;
            if (a) 
            	return 2;
            if (r) 
            	return 3;
            
            
            return 0;
        }
    }
    /**
     * Cria os arquivos físicos persistidos do modelo (.gfc e .gfc_diagram) 
     * e abre no editor padrão do Eclipse após a sincronização do Workspace.
     */
    private void saveAndOpen(IWorkbenchPage page, IFile currentFile, Flowchart model) {
        IProject project = currentFile.getProject();
        IFile modelFile = project.getFile("capivara.gfc");
        IFile diagramFile = project.getFile("capivara.gfc_diagram");
        URI modelURI = URI.createPlatformResourceURI(modelFile.getFullPath().toString(), true);
        URI diagramURI = URI.createPlatformResourceURI(diagramFile.getFullPath().toString(), true);
        try {
            GfcDiagramEditorUtil.createDiagram(diagramURI, modelURI, new NullProgressMonitor());
            Resource res = new ResourceSetImpl().createResource(modelURI);
            res.getContents().add(model);
            res.save(Collections.EMPTY_MAP);
            project.refreshLocal(IResource.DEPTH_INFINITE, null);
            IEditorPart editor = IDE.openEditor(page, diagramFile, true);
            Display.getDefault().timerExec(1000, () -> {
                applySmartLayoutWithFallback(editor, 0);
                forceRefreshVisuals(editor);
            });
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void applySmartLayoutWithFallback(IEditorPart editorPart, int attempt) {
        if (!(editorPart instanceof DiagramEditor)) return;
        DiagramEditor diagramEditor = (DiagramEditor) editorPart;    
        DiagramEditPart diagramEditPart = diagramEditor.getDiagramEditPart();
        if (diagramEditPart == null || diagramEditPart.getChildren().isEmpty()) {
            if (attempt < 5) Display.getDefault().timerExec(500, () -> applySmartLayoutWithFallback(editorPart, attempt + 1));
            return; 
        }
        boolean success = false;
        if (isGraphvizAvailable()) { success = tryApplyELKGraphvizLayout(diagramEditor, diagramEditPart); }
        if (!success) { tryApplyELKLayered(diagramEditor, diagramEditPart); }
        disableAllEditPolicies(diagramEditPart);
    }
    /**
     * Constrói e aplica a árvore de layout utilizando o motor externo Graphviz via comandos dot.
     */
    private boolean tryApplyELKGraphvizLayout(DiagramEditor diagramEditor, DiagramEditPart diagramEditPart) {
        try {
            IDiagramLayoutConnector connector = LayoutConnectorsService.getInstance().getConnector(diagramEditor, diagramEditPart);
            LayoutMapping mapping = connector.buildLayoutGraph(diagramEditor, diagramEditPart);
            ElkNode rootNode = mapping.getLayoutGraph();
            configureGraphvizPath(rootNode);
            rootNode.setProperty(CoreOptions.ALGORITHM, "org.eclipse.elk.graphviz.dot");
            rootNode.setProperty(CoreOptions.DIRECTION, Direction.DOWN);
            rootNode.setProperty(CoreOptions.EDGE_ROUTING, EdgeRouting.SPLINES);
            rootNode.setProperty(CoreOptions.SPACING_NODE_NODE, 60.0);
            new RecursiveGraphLayoutEngine().layout(rootNode, new BasicProgressMonitor());
            connector.applyLayout(mapping, rootNode);
            return true;
        } catch (Exception e) { return false; }
    }

    private boolean tryApplyELKLayered(DiagramEditor diagramEditor, DiagramEditPart diagramEditPart) {
        try {
            IDiagramLayoutConnector connector = LayoutConnectorsService.getInstance().getConnector(diagramEditor, diagramEditPart);
            LayoutMapping mapping = connector.buildLayoutGraph(diagramEditor, diagramEditPart);
            ElkNode rootNode = mapping.getLayoutGraph();
            rootNode.setProperty(CoreOptions.ALGORITHM, "org.eclipse.elk.layered");
            rootNode.setProperty(CoreOptions.DIRECTION, Direction.DOWN);
            new RecursiveGraphLayoutEngine().layout(rootNode, new BasicProgressMonitor());
            connector.applyLayout(mapping, rootNode);
            return true;
        } catch (Exception e) { return false; }
    }

    private boolean isGraphvizAvailable() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            Process p = os.contains("win") ? Runtime.getRuntime().exec("cmd /c dot -V") : Runtime.getRuntime().exec(new String[]{"sh", "-c", "dot -V 2>/dev/null"});
            return p.waitFor(500, java.util.concurrent.TimeUnit.MILLISECONDS) && p.exitValue() == 0;
        } catch (Exception e) { return false; }
    }

    private void configureGraphvizPath(ElkNode rootNode) {
        String os = System.getProperty("os.name").toLowerCase();
        Property<String> propKey = new Property<String>("org.eclipse.elk.alg.graphviz.dot.executable");

        if (os.contains("win")) { // Caminhos que o Graphviz fica no winddows
            java.io.File winPath = new java.io.File("C:\\Program Files\\Graphviz\\bin\\dot.exe");
            java.io.File winPathX86 = new java.io.File("C:\\Program Files (x86)\\Graphviz\\bin\\dot.exe");

            if (winPath.exists()) {
                rootNode.setProperty(propKey, winPath.getAbsolutePath());
            } else if (winPathX86.exists()) {
                rootNode.setProperty(propKey, winPathX86.getAbsolutePath());
            }
        } else {
            rootNode.setProperty(propKey, "/usr/bin/dot"); // Caminho no linux
        }
    }

    private Node createTypedNode(GfcFactory factory, String type) {
        switch (type) {
            case "ENTRY": return factory.createEntryNode();
            case "EXIT": return factory.createExitNode();
            case "DECISION": return factory.createDecisionNode();
            case "LOOP_DECISION": return factory.createLoopDecisionNode();
            case "PROCESSING": return factory.createProcessingNode();
            case "SWITCH_DECISION": return factory.createSwitchNode(); 
            case "CASE": return factory.createCaseNode();
            default: return factory.createProcessingNode();
        }
    }

    private int findPredecessorStatus(int nodeId, GFCVisitor visitor, Map<Integer, Node> nodes) {
        for (Map.Entry<Integer, List<Edge>> entry : visitor.getGraphEdges().entrySet()) {
            for (Edge edge : entry.getValue()) {
                if (edge.getDestinationNodeId() == nodeId) {
                    Node p = nodes.get(entry.getKey());
                    if (p != null && p.getCoverageStatus() > 0) return p.getCoverageStatus();
                }
            }
        }
        return 0;
    }

    private void forceRefreshVisuals(IEditorPart ep) {
        if (!(ep instanceof DiagramEditor)) return;
        DiagramEditor diagramEditor = (DiagramEditor) ep;
        DiagramEditPart dep = diagramEditor.getDiagramEditPart();
        if (dep == null || dep.getDiagramEditDomain() == null) {
            Display.getDefault().timerExec(500, () -> forceRefreshVisuals(ep));
            return;
        }
        dep.refresh();
        for (Object c : dep.getChildren()) {
            if (c instanceof EditPart) {
                ((EditPart) c).refresh();
                if (c instanceof org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart) {
                    org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart gep = 
                        (org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart) c;
                    gep.getSourceConnections().forEach(conn -> {
                        if (conn instanceof EditPart) ((EditPart) conn).refresh();
                    });
                }
            }
        }
    }

    private MethodDeclaration findSelectedMethod(CompilationUnit root, int offset, int length) {
        ASTNode n = NodeFinder.perform(root, offset, length);
        ASTNode temp = n;
        while (temp != null && !(temp instanceof MethodDeclaration)) { temp = temp.getParent(); }
        if (temp instanceof MethodDeclaration) return (MethodDeclaration) temp;
        if (n instanceof TypeDeclaration || n instanceof CompilationUnit) {
            final MethodDeclaration[] found = new MethodDeclaration[1];
            n.accept(new ASTVisitor() {
                public boolean visit(MethodDeclaration node) {
                    if (node.getStartPosition() >= offset && (node.getStartPosition() + node.getLength()) <= (offset + length)) {
                        found[0] = node; return false;
                    }
                    return true;
                }
            });
            return found[0];
        }
        return null;
    }
    /**
     * Desativa políticas nativas de edição do GMF (como arrastar e excluir componentes) 
     * fixando os nós em um modo estático de somente visualização pós-layout.
     */
    private void disableAllEditPolicies(EditPart ep) {
        if (ep == null) return;
        ep.removeEditPolicy(EditPolicy.COMPONENT_ROLE); ep.removeEditPolicy(EditPolicy.LAYOUT_ROLE); ep.removeEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
        for (Object c : ep.getChildren()) if (c instanceof EditPart) disableAllEditPolicies((EditPart) c);
    }
}