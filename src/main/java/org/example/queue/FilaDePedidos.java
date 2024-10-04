package org.example.queue;

import org.example.model.Pedido;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class FilaDePedidos {
    private PriorityBlockingQueue<Pedido> fila;

    public FilaDePedidos(int capacidade) {
        this.fila = new PriorityBlockingQueue<>(capacidade);
    }

    public void adicionarPedido(Pedido pedido) throws InterruptedException {
        fila.put(pedido); 
    }

    public Pedido retirarPedido() throws InterruptedException {
        return fila.take(); 
    }
}