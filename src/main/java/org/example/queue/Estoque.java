package org.example.queue;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class Estoque {
    private final Map<String, Integer> produtos; // Estoque de produtos com nome e quantidade
    private final Map<String, Integer> popularidade; // Rastreia a popularidade de produtos (quantidade vendida)
    private final ReentrantReadWriteLock lock;

    public Estoque() {
        this.produtos = new HashMap<>();
        this.popularidade = new HashMap<>();
        this.lock = new ReentrantReadWriteLock();
    }

    /**
     * Adiciona produtos ao estoque.
     * @param nome Nome do produto.
     * @param quantidade Quantidade a ser adicionada.
     */
    public void adicionarProduto(String nome, int quantidade) {
        lock.writeLock().lock();
        try {
            produtos.put(nome, produtos.getOrDefault(nome, 0) + quantidade);
            log.info("Reabastecido: {} ({} itens adicionados). Estoque atual: {} itens", nome, quantidade, produtos.get(nome));
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Verifica se há disponibilidade suficiente de um produto no estoque.
     * @param nome Nome do produto.
     * @param quantidade Quantidade desejada.
     * @return true se houver quantidade suficiente, false caso contrário.
     */
    public boolean verificarDisponibilidade(String nome, int quantidade) {
        lock.readLock().lock();
        try {
            boolean disponivel = produtos.getOrDefault(nome, 0) >= quantidade;
            log.debug("Verificando disponibilidade de {}: {} itens (necessário: {}) - Disponível: {}", nome, produtos.getOrDefault(nome, 0), quantidade, disponivel);
            return disponivel;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Retira produtos do estoque e atualiza a popularidade.
     * @param nome Nome do produto.
     * @param quantidade Quantidade a ser retirada.
     */
    public void retirarProduto(String nome, int quantidade) {
        lock.writeLock().lock();
        try {
            if (produtos.getOrDefault(nome, 0) >= quantidade) {
                produtos.put(nome, produtos.get(nome) - quantidade);
                popularidade.put(nome, popularidade.getOrDefault(nome, 0) + quantidade); // Incrementa a popularidade pelo número de itens vendidos
                log.info("Pedido processado: {} ({} itens retirados). Estoque restante: {} itens", nome, quantidade, produtos.get(nome));
            } else {
                log.warn("Tentativa de retirar mais itens do que o disponível no estoque para o produto: {}", nome);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Retorna o nome do produto mais popular, baseado na quantidade de itens vendidos.
     * @return Nome do produto mais popular ou "Nenhum Produto" se não houver vendas.
     */
    public String produtoMaisPopular() {
        lock.readLock().lock();
        try {
            return popularidade.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null); // Retorna null se nenhum produto foi vendido
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Retorna o status atual do estoque para fins de relatório.
     * @return Uma string representando o estado atual do estoque.
     */
    public String statusEstoque() {
        lock.readLock().lock();
        try {
            StringBuilder status = new StringBuilder("Status atual do estoque:\n");
            produtos.forEach((nome, quantidade) -> status.append(nome).append(": ").append(quantidade).append(" itens\n"));
            return status.toString();
        } finally {
            lock.readLock().unlock();
        }
    }
}
