package org.example.worker;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Pedido;
import org.example.model.Produto;
import org.example.queue.Estoque;
import org.example.queue.FilaDePedidos;
import org.example.model.Pagamento;
import org.example.schedule.RelatorioDeVendas;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class Worker implements Runnable {
    private final FilaDePedidos filaDePedidos;
    private final Estoque estoque;
    private final RelatorioDeVendas relatorioDeVendas;

    public Worker(FilaDePedidos filaDePedidos, Estoque estoque, RelatorioDeVendas relatorioDeVendas) {
        this.filaDePedidos = filaDePedidos;
        this.estoque = estoque;
        this.relatorioDeVendas = relatorioDeVendas;
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
                                log.info("Pedido rejeitado por falta de estoque: Cliente {}", pedido.getCliente().getId());
                                try {
                                    filaDePedidos.adicionarPedidoPendente(pedido);
                                    relatorioDeVendas.incrementarRejeitados();
                                } catch (InterruptedException e) {
                                    log.error("Erro ao adicionar pedido pendente: {}", e.getMessage());
                                }
                                break;
                            }
                        }

                        if (podeProcessar) {
                            for (Produto produto : pedido.getProdutos()) {
                                estoque.retirarProduto(produto.getNome(), produto.getQuantidade());
                            }
                            log.info("Pedido de Cliente {} foi processado com sucesso.", pedido.getCliente().getId());
                            double valorPedido = calcularValorPedido(pedido);
                            relatorioDeVendas.incrementarProcessados(valorPedido);
                        }
                    } else {
                        // Pagamento não confirmado
                        log.info("Pagamento não confirmado para o pedido de Cliente {}", pedido.getCliente().getId());
                        relatorioDeVendas.incrementarRejeitados();
                    }
                }).get();

            }
        } catch (Exception e) {
            log.error("Houve erro durante execução do worker: {}", e.getMessage());
        }
    }

    private double calcularValorPedido(Pedido pedido) {
        double valor = 0;
        for (Produto produto : pedido.getProdutos()) {
            double preco = obterPrecoProduto(produto.getNome());
            valor += produto.getQuantidade() * preco;
        }
        return valor;
    }

    private double obterPrecoProduto(String nomeProduto) {
        // Definir preços dos produtos
        switch (nomeProduto) {
            case "Produto A":
                return 10.0;
            case "Produto B":
                return 15.0;
            case "Produto C":
                return 20.0;
            default:
                return 0.0;
        }
    }
}
