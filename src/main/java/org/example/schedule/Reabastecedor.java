package org.example.schedule;

import org.example.queue.Estoque;
import org.example.utils.Constantes;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Reabastecedor extends Constantes implements Runnable {
    private final Estoque estoque;

    public Reabastecedor(Estoque estoque) {
        this.estoque = estoque;
    }

    @Override
    public void run() {
        estoque.adicionarProduto("Produto A", 10);
        estoque.adicionarProduto("Produto B", 5);
    }

    public static void iniciarReabastecimento(Estoque estoque) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new Reabastecedor(estoque), 0, VALOR_INTERVALO_ATUALIZACAO_REABASTECEDOR, INTERVALO_ATUALIZACAO_REABASTECEDOR);
    }
}
