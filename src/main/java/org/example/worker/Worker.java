package org.example.worker;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Pagamento;
import org.example.model.Pedido;
import org.example.model.Produto;
import org.example.queue.Estoque;
import org.example.queue.FilaDePedidos;

import java.util.concurrent.CompletableFuture;

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
                CompletableFuture<Boolean> pagamentoFuturo = Pagamento.processarPagamento(pedido);
                
                pagamentoFuturo.thenAccept((pagamentoConfirmado) -> {
                    if (pagamentoConfirmado) {
                        boolean podeProcessar = true;

                        for (Produto produto : pedido.getProdutos()) {
                            if (!estoque.verificarDisponibilidade(produto.getNome(), produto.getQuantidade())) {
                                podeProcessar = false;
                                log.info("Pedido rejeitado por falta de estoque: {}", pedido.getCliente());
                                break;
                            }
                        }

                        if (podeProcessar) {
                            for (Produto produto : pedido.getProdutos()) {
                                estoque.retirarProduto(produto.getNome(), produto.getQuantidade());
                            }
                            log.info("Pedido de {} foi processado com sucesso.", pedido.getCliente().getId());
                        }
                    }
                }).get(); 

            }
        } catch (Exception e) {
            log.error("Houve erro durante execucão do worker", e.getMessage());
        }
    }
}
