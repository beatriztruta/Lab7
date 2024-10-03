package org.example.queue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
            System.out.println("Pedido processado: " + nome + " (" + quantidade + " itens)");
        } finally {
            lock.writeLock().unlock();
        }
    }
}
