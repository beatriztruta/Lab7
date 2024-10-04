package org.example.queue;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class Estoque {
    private final Map<String, Integer> produtos;
    private final Map<String, Integer> popularidade; // Rastrear popularidade dos produtos
    private final ReentrantReadWriteLock lock;

    public Estoque() {
        this.produtos = new HashMap<>();
        this.popularidade = new HashMap<>();
        this.lock = new ReentrantReadWriteLock();
    }

    public void adicionarProduto(String nome, int quantidade) {
        lock.writeLock().lock();
        try {
            produtos.put(nome, produtos.getOrDefault(nome, 0) + quantidade);
            System.out.println("Reabastecido: " + nome + " (" + quantidade + " itens)");
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean verificarDisponibilidade(String nome, int quantidade) {
        lock.readLock().lock();
        try {
            return produtos.getOrDefault(nome, 0) >= quantidade;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void retirarProduto(String nome, int quantidade) {
        lock.writeLock().lock();
        try {
            produtos.put(nome, produtos.get(nome) - quantidade);
            popularidade.put(nome, popularidade.getOrDefault(nome, 0) + 1); // Incrementa a popularidade
            System.out.println("Pedido processado: " + nome + " (" + quantidade + " itens)");
        } finally {
            lock.writeLock().unlock();
        }
    }

    public String produtoMaisPopular() {
        return popularidade.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("Nenhum Produto");
    }
}