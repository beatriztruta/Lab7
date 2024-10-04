package org.example.queue;

import org.example.model.Pedido;

import java.util.concurrent.PriorityBlockingQueue;

public class FilaDePedidos {
    private final PriorityBlockingQueue<Pedido> fila;
    private final PriorityBlockingQueue<Pedido> filaPendentes;

    public FilaDePedidos(int capacidade) {
        this.fila = new PriorityBlockingQueue<>(capacidade);
        this.filaPendentes = new PriorityBlockingQueue<>(capacidade);
    }

    public void adicionarPedido(Pedido pedido) throws InterruptedException {
        fila.put(pedido);
    }

    public Pedido retirarPedido() throws InterruptedException {
        Pedido pedido = fila.poll();
        if (pedido == null) {
            pedido = filaPendentes.poll();
        }
        if (pedido == null) {
            pedido = fila.take();
        }
        return pedido;
    }

    public void adicionarPedidoPendente(Pedido pedido) throws InterruptedException {
        filaPendentes.put(pedido);
    }
}
