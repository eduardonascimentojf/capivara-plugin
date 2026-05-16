package br.ufjf.capivara.listener;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.eclemma.core.CoverageTools;
import org.eclipse.eclemma.core.ICoverageSession;
import org.eclipse.eclemma.core.ISessionListener;
import org.eclipse.eclemma.core.analysis.IJavaModelCoverage;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IParent;
import org.eclipse.jdt.core.JavaCore;
import org.jacoco.core.analysis.ICounter;
import org.jacoco.core.analysis.ICoverageNode;
import org.jacoco.core.analysis.ISourceNode;

import br.ufjf.capivara.util.CapivaraCoverageCache;

@SuppressWarnings("restriction")
public class CapivaraCoverageListener implements ISessionListener {

    @Override
    public void sessionAdded(ICoverageSession session) {
        scheduleAnalysis(500); // Delay para garantir carregamento
    }

    @Override
    public void sessionActivated(ICoverageSession session) {
        scheduleAnalysis(100);
    }

    @Override
    public void sessionRemoved(ICoverageSession session) {
         
    }

    private void scheduleAnalysis(int delay) {
        Job job = new Job("Atualizando Cache Capivara") {
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                analyzeAndSave();
                return Status.OK_STATUS;
            }
        };
        job.setSystem(true); 
        job.schedule(delay);
    }

    private void analyzeAndSave() {
        try {
            Object rawCoverage = CoverageTools.getJavaModelCoverage();
            if (rawCoverage instanceof IJavaModelCoverage) {
                IJavaModelCoverage coverage = (IJavaModelCoverage) rawCoverage;
                
                CapivaraCoverageCache.getInstance().limpar();
                
                IJavaModel javaModel = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
                if (javaModel != null) {
                    for (IJavaElement project : javaModel.getChildren()) {
                        processElementRecursively(project, coverage);
                    }
                }
                
               
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processElementRecursively(IJavaElement element, IJavaModelCoverage coverage) {
        ICoverageNode node = coverage.getCoverageFor(element);
        if (node == null) return;

        if (element instanceof ICompilationUnit) {
            extractLinesFromFile((ICompilationUnit) element, coverage);
            return; 
        }

        if (element instanceof IParent) {
            try {
                for (IJavaElement child : ((IParent) element).getChildren()) {
                    processElementRecursively(child, coverage);
                }
            } catch (Exception e) { }
        }
    }

    private void extractLinesFromFile(ICompilationUnit file, IJavaModelCoverage coverage) {
        ICoverageNode node = coverage.getCoverageFor(file);
        if (node == null) return;
        
        String fullPath = file.getResource().getFullPath().toString();
        Map<Integer, Integer> linhasComStatus = new HashMap<>();

        if (node instanceof ISourceNode) {
            ISourceNode source = (ISourceNode) node;
            int first = source.getFirstLine();
            int last = source.getLastLine();
            
            if (first != -1) {
                for (int i = first; i <= last; i++) {
                    int statusJaCoCo = source.getLine(i).getStatus();
                    int meuStatus = 0;
                    
                    switch (statusJaCoCo) {
                        case ICounter.FULLY_COVERED:
                            meuStatus = 1; // VERDE
                            break;
                        case ICounter.PARTLY_COVERED:
                            meuStatus = 2; // AMARELO
                            break;
                        case ICounter.NOT_COVERED:
                            meuStatus = 3; // VERMELHO
                            break;
                        default:
                            meuStatus = 0; // IGNORAR
                    }
                    
                    if (meuStatus != 0) {
                        linhasComStatus.put(i, meuStatus);
                    }
                }
            }
        }
        
        if (!linhasComStatus.isEmpty()) {
            CapivaraCoverageCache.getInstance().adicionarCobertura(fullPath, linhasComStatus);
        }
    }
}