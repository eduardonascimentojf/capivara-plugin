package br.ufjf.capivara.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Cache de cobertura de código do ecossistema Capivara.
 * <p>
 * Esta classe utiliza o padrão <b>Singleton</b> para garantir uma única instância
 * de cache centralizado durante a execução, armazenando o status de cobertura
 * de linhas de código associadas aos seus respectivos arquivos.
 * </p>
 */
public class CapivaraCoverageCache {

    private static CapivaraCoverageCache instance;
    
    // Mapa: CaminhoArquivo -> (Linha -> Status)
    // Status: 0=Ignorado, 1=Verde, 2=Amarelo, 3=Vermelho
    private Map<String, Map<Integer, Integer>> mapaDeCobertura;

    private CapivaraCoverageCache() {
        this.mapaDeCobertura = new HashMap<>();
    }

    public static CapivaraCoverageCache getInstance() {
        if (instance == null) {
            instance = new CapivaraCoverageCache();
        }
        return instance;
    }

    public void limpar() {
        this.mapaDeCobertura.clear();
    }
    
    public boolean temDados() {
        return !mapaDeCobertura.isEmpty();
    }

    public void adicionarCobertura(String caminhoArquivo, Map<Integer, Integer> linhasComStatus) {
        if (caminhoArquivo == null || linhasComStatus == null) return;
        
        Map<Integer, Integer> arquivoMap = mapaDeCobertura.computeIfAbsent(caminhoArquivo, k -> new HashMap<>());
        arquivoMap.putAll(linhasComStatus);
    }

    // Retorna o status (0, 1, 2 ou 3)
    public int getStatusLinha(String caminhoArquivo, int linha) {
        if (caminhoArquivo == null) return 0;
        
        Map<Integer, Integer> linhas = mapaDeCobertura.get(caminhoArquivo);
        if (linhas != null) {
            return linhas.getOrDefault(linha, 0);
        }
        return 0;
    }
}