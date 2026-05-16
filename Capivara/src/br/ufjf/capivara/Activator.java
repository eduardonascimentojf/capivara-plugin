package br.ufjf.capivara;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.eclipse.eclemma.core.CoverageTools;
import br.ufjf.capivara.listener.CapivaraCoverageListener;

/**
 * Ativador do ciclo de vida do plugin Capivara e ponto de entrada na inicialização do Eclipse.
 * Implementa {@link IStartup} para permitir a execução de rotinas logo na abertura do workbench.
 */
public class Activator extends AbstractUIPlugin implements IStartup { 

    public static final String PLUGIN_ID = "br.ufjf.capivara";
    private static Activator plugin;
    private CapivaraCoverageListener coverageListener;

    public Activator() {}

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @SuppressWarnings("restriction")
	@Override
    public void earlyStartup() {
        if (coverageListener == null) {
            coverageListener = new CapivaraCoverageListener();
            CoverageTools.getSessionManager().addSessionListener(coverageListener);
            System.out.println("[Capivara] Listener funcionando");
        }
    }

    @SuppressWarnings("restriction")
    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        if (coverageListener != null) {
            CoverageTools.getSessionManager().removeSessionListener(coverageListener);
            coverageListener = null;
        }
        super.stop(context);
    }

    public static Activator getDefault() {
        return plugin;
    }
}