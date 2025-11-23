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
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;

// Imports do seu domínio
import br.ufjf.capivara.analyzer.CodeAnalyzer;
import br.ufjf.capivara.analyzer.GFCVisitor;
import br.ufjf.capivara.model.Edge;
import gfc.Flowchart;
import gfc.GfcFactory;
import gfc.Node;
import gfc.diagram.part.GfcDiagramEditorUtil;

public class AnalyzeCodeForGmfHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        // 1. Obter Janela e Seleção
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
        if (window == null) return null;
        IWorkbenchPage page = window.getActivePage();
        IEditorPart activeEditor = page.getActiveEditor();
        if (activeEditor == null) return null;

        ISelection selection = activeEditor.getSite().getSelectionProvider().getSelection();
        if (!(selection instanceof ITextSelection)) return null;
        String selectedText = ((ITextSelection) selection).getText();
        if (selectedText == null || selectedText.trim().isEmpty()) return null;
        
        // 2. Parsear Código Java
        CodeAnalyzer analyzer = new CodeAnalyzer();
        // Envolvemos em uma classe Wrapper para o parser aceitar snippets soltos
        CompilationUnit astRoot = analyzer.parse("class Wrapper {\n" + selectedText + "\n}");
        MethodDeclaration methodNode = findMethod(astRoot);
        
        if (methodNode == null) {
            System.err.println("Nenhum método Java válido encontrado na seleção.");
            return null;
        }

        // 3. Visitar AST e Gerar Grafo Abstrato
        GFCVisitor visitor = new GFCVisitor();
        visitor.setup(astRoot);
        methodNode.accept(visitor);
        
        // 4. Criar Modelo GMF (Flowchart)
        GfcFactory factory = GfcFactory.eINSTANCE;
        Flowchart flowchartModel = factory.createFlowchart();
        Map<Integer, Node> createdNodes = new HashMap<>(); 
        Map<Integer, String> nodeTypes = visitor.getNodeTypes();
        
        // Criar Nós
        for (Map.Entry<Integer, String> entry : nodeTypes.entrySet()) {
            Integer nodeId = entry.getKey();
            String nodeType = entry.getValue();
            String nodeLabel = visitor.getNodeLabels().get(nodeId);
            Node newNode = null;
            
            switch (nodeType) {
                case "ENTRY": newNode = factory.createEntryNode(); break;
                case "EXIT": newNode = factory.createExitNode(); break;
                case "DECISION": newNode = factory.createDecisionNode(); break;
                case "LOOP_DECISION": newNode = factory.createLoopDecisionNode(); break;
                case "PROCESSING": newNode = factory.createProcessingNode(); break;
            }
            
            if (newNode != null) {
                newNode.setLabel(nodeLabel);
                newNode.setId(nodeId); 
                flowchartModel.getNodes().add(newNode);
                createdNodes.put(nodeId, newNode);
            }
        }
        
        // Criar Arestas
        Map<Integer, List<Edge>> graphEdges = visitor.getGraphEdges();
        for (Map.Entry<Integer, List<Edge>> entry : graphEdges.entrySet()) {
            Node fromNode = createdNodes.get(entry.getKey());
            for (br.ufjf.capivara.model.Edge oldEdge : entry.getValue()) {
                Node toNode = createdNodes.get(oldEdge.getDestinationNodeId());
                if (fromNode != null && toNode != null) {
                    gfc.Edge newEdge = factory.createEdge();
                    newEdge.setSource(fromNode);
                    newEdge.setTarget(toNode);
                    newEdge.setLabel(oldEdge.getLabel());
                    flowchartModel.getEdges().add(newEdge);
                }
            }
        }

        // 5. Preparar Arquivos (.gfc e .gfc_diagram)
        IResource iresource = activeEditor.getEditorInput().getAdapter(IResource.class);
        if (iresource == null) return null;
        IProject project = iresource.getProject();
        
        String fileName = "capivara";
        IFile modelFile = project.getFile(fileName + ".gfc");
        IFile diagramFile = project.getFile(fileName + ".gfc_diagram");

        URI modelURI = URI.createPlatformResourceURI(modelFile.getFullPath().toString(), true);
        URI diagramURI = URI.createPlatformResourceURI(diagramFile.getFullPath().toString(), true);
        
        // Cria o diagrama físico (XML do GMF)
        GfcDiagramEditorUtil.createDiagram(diagramURI, modelURI, new NullProgressMonitor());
        
        // Salva o modelo semântico
        Resource modelResource = new ResourceSetImpl().createResource(modelURI);
        modelResource.getContents().add(flowchartModel);
        try {
            modelResource.save(Collections.EMPTY_MAP);
        } catch (Exception e) { e.printStackTrace(); return null; }

        // ==================================================================================
        // CONFIGURAÇÃO DO ELK (LAYOUT TOP-DOWN)
        // Injetamos a configuração diretamente no arquivo do diagrama antes de abrir.
        // ==================================================================================
        try {
            Resource diagramResource = new ResourceSetImpl().createResource(diagramURI);
            diagramResource.load(Collections.EMPTY_MAP);
            
            if (!diagramResource.getContents().isEmpty()) {
                // O objeto raiz do arquivo .gfc_diagram é um Diagram (Notation)
                Diagram diagram = (Diagram) diagramResource.getContents().get(0);
                
                // Limpa configs antigas
                EAnnotation existing = diagram.getEAnnotation("org.eclipse.elk.core");
                if (existing != null) {
                    diagram.getEAnnotations().remove(existing);
                }
                
                // Cria a anotação de configuração
                EAnnotation elkConfig = EcoreFactory.eINSTANCE.createEAnnotation();
                elkConfig.setSource("org.eclipse.elk.core");
                
                // ---------------------------------------------------------
                // AQUI ESTÁ O SEGREDO PARA PARECER COM GRAPHVIZ
                // ---------------------------------------------------------
                
                // Algoritmo: 'layered' é a implementação Java do Sugiyama (mesmo do Dot)
                // Se tiver o Graphviz instalado no SO, pode tentar "org.eclipse.elk.graphviz.dot"
                elkConfig.getDetails().put("org.eclipse.elk.algorithm", "org.eclipse.elk.layered");
                
                // Direção: De cima para baixo (Top-Down)
                elkConfig.getDetails().put("org.eclipse.elk.direction", "DOWN"); 
                
                // Espaçamento: Ajuste para ficar visualmente agradável
                elkConfig.getDetails().put("org.eclipse.elk.spacing.nodeNode", "60.0"); // Espaço lateral
                elkConfig.getDetails().put("org.eclipse.elk.layered.spacing.edgeNodeBetweenLayers", "50.0"); // Espaço vertical
                
                // Roteamento de Arestas:
                // ORTHOGONAL = Linhas retas (Melhor suporte no GMF padrão)
                // SPLINES = Linhas curvas (Igual Graphviz, mas pode falhar se o GMF não suportar)
                elkConfig.getDetails().put("org.eclipse.elk.edgeRouting", "ORTHOGONAL"); 

                // Adiciona ao diagrama e salva
                diagram.getEAnnotations().add(elkConfig);
                diagramResource.save(Collections.EMPTY_MAP);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Não interrompe o fluxo se falhar o layout, apenas loga
        }

        // 6. Abrir Editor e Tornar ReadOnly
        try {
            IEditorPart editorPart = IDE.openEditor(page, diagramFile, true);
            makeEditorReadOnly(editorPart);
        } catch (PartInitException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Configura o editor para modo somente leitura e remove ferramentas de edição.
     */
    private void makeEditorReadOnly(IEditorPart editorPart) {
        if (!(editorPart instanceof DiagramEditor)) return;

        final DiagramEditor diagramEditor = (DiagramEditor) editorPart;
        Display display = Display.getDefault();

        // Executa na thread de UI assim que possível
        display.asyncExec(() -> {
            try {
                // 1. Tenta remover a Paleta (Toolbox)
                try {
                    if (diagramEditor.getDiagramGraphicalViewer() != null &&
                        diagramEditor.getDiagramGraphicalViewer().getEditDomain() != null) {
                        diagramEditor.getDiagramGraphicalViewer().getEditDomain().setPaletteRoot(null);
                    }
                } catch (Throwable t) { 
                    // Ignora se falhar, apenas visual
                }

                // 2. Desativa interações de edição (Arrastar, Deletar, Criar)
                DiagramEditPart diagramEditPart = diagramEditor.getDiagramEditPart();
                if (diagramEditPart != null) {
                    disableAllEditPolicies(diagramEditPart);
                    
                    // OPCIONAL: Se o layout não aplicar sozinho na abertura, 
                    // descomente as linhas abaixo para forçar um "Arrange All" ao abrir:
                    /*
                    org.eclipse.gef.Request request = new org.eclipse.gef.requests.GroupRequest(org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants.REQ_ARRANGE_DEFERRED);
                    ((org.eclipse.gef.requests.GroupRequest) request).setEditParts(diagramEditPart.getChildren());
                    diagramEditPart.performRequest(request);
                    */
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Remove recursivamente as políticas de edição (EditPolicies) para travar o diagrama.
     */
    private void disableAllEditPolicies(EditPart editPart) {
        if (editPart == null) return;

        // Remove capacidades de edição
        editPart.removeEditPolicy(EditPolicy.COMPONENT_ROLE);      // Deletar
        editPart.removeEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE); // Criar arestas
        editPart.removeEditPolicy(EditPolicy.LAYOUT_ROLE);         // Mover manualmente (Layout livre)
        editPart.removeEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);   // Seleção/Resize

        // Recursão para filhos
        for (Object child : editPart.getChildren()) {
            if (child instanceof EditPart) {
                disableAllEditPolicies((EditPart) child);
            }
        }
    }

    private MethodDeclaration findMethod(CompilationUnit cu) {
        final MethodDeclaration[] result = new MethodDeclaration[1];
        cu.accept(new ASTVisitor() {
            @Override
            public boolean visit(MethodDeclaration node) {
                if (result[0] == null) result[0] = node;
                return false;
            }
        });
        return result[0];
    }
}