package org.example.schedule;

import lombok.extern.slf4j.Slf4j;
import org.example.queue.Estoque;
import org.example.utils.Constantes;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ReabastecedorInteligente extends Constantes implements Runnable {
    private final Estoque estoque;

    public ReabastecedorInteligente(Estoque estoque) {
        this.estoque = estoque;
    }

    @Override
    public void run() {
        String produtoPopular = estoque.produtoMaisPopular();
        if (produtoPopular != null && !produtoPopular.isEmpty()) {
            estoque.adicionarProduto(produtoPopular, QUANTIDADE_ITENS_REABASTECER);
            log.info("Reabastecimento Inteligente: Produto mais popular é '{}', adicionando {} itens", produtoPopular, QUANTIDADE_ITENS_REABASTECER);
        } else {
            log.warn("Nenhum produto foi identificado como popular para reabastecimento.");
        }
    }

    public void iniciarReabastecimento() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this, 0, PERIODO_INTERVALO_ATUALIZACAO_REABASTECEDOR, INTERVALO_ATUALIZACAO_REABASTECEDOR);
    }
}
