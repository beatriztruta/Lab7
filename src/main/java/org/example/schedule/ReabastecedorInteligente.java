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
        estoque.adicionarProduto(produtoPopular, QUANTIDADE_ITENS_REABASTECER);
        log.info("Reabastecimento Inteligente: Produto mais popular é {} ", produtoPopular);
    }

    public  void iniciarReabastecimento(Estoque estoque) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new ReabastecedorInteligente(estoque), 0, 10, TimeUnit.SECONDS);
    }
}
