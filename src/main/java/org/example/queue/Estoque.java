package org.example.queue;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class Estoque {
    private final Map<String, Integer> produtos;
    private final ReentrantReadWriteLock lock;

    public Estoque() {
        this.produtos = new HashMap<>();
        this.lock = new ReentrantReadWriteLock();
    }

    public void adicionarProduto(String nome, int quantidade) {
        lock.writeLock().lock();
        try {
            produtos.put(nome, produtos.getOrDefault(nome, 0) + quantidade);
            log.info("Reabastecido produto: {} ({} itens)", nome, quantidade);
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
            log.info("Pedido processado:  {} ({} itens)", nome, quantidade);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
