package org.example.worker;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Pedido;
import org.example.model.Produto;
import org.example.queue.Estoque;
import org.example.queue.FilaDePedidos;

@Slf4j
public class Worker implements Runnable {
    private final FilaDePedidos filaDePedidos;
    private final Estoque estoque;

    public Worker(FilaDePedidos filaDePedidos, Estoque estoque) {
        this.filaDePedidos = filaDePedidos;
        this.estoque = estoque;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Pedido pedido = filaDePedidos.retirarPedido();
                boolean podeProcessar = true;
                
                for (Produto produto : pedido.getProdutos()) {
                    if (!estoque.verificarDisponibilidade(produto.getNome(), produto.getQuantidade())) {
                        podeProcessar = false;
                        System.out.println("Pedido rejeitado: " + pedido.getCliente());
                        break;
                    }
                }

                if (podeProcessar) {
                    for (Produto produto : pedido.getProdutos()) {
                        estoque.retirarProduto(produto.getNome(), produto.getQuantidade());
                    }
                    System.out.println("Pedido de " + pedido.getCliente() + " foi processado com sucesso.");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
