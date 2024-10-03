package org.example.schedule;

import lombok.extern.slf4j.Slf4j;
import org.example.queue.Estoque;
import org.example.utils.Constantes;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
public class RelatorioDeVendas extends Constantes implements Runnable {
    private final Estoque estoque;
    private int pedidosProcessados;
    private int pedidosRejeitados;

    public RelatorioDeVendas(Estoque estoque) {
        this.estoque = estoque;
        this.pedidosProcessados = 0;
        this.pedidosRejeitados = 0;
    }

    @Override
    public void run() {
        log.info("=-=-=-=Relatório de Vendas =-=-=-=");
        log.info("\"Pedidos processados: {}", pedidosProcessados);
        log.info("Pedidos rejeitados: {}", pedidosRejeitados);
    }

    public void incrementarProcessados() {
        pedidosProcessados++;
    }

    public void incrementarRejeitados() {
        pedidosRejeitados++;
    }

    public static void iniciarRelatorio(Estoque estoque) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new RelatorioDeVendas(estoque), 0, PERIODO_IINTERVALO_ATUALIZACAO_GERADOR_RELATORIO_VENDAS, INTERVALO_ATUALIZACAO_GERADOR_RELATORIO_VENDAS);
    }
}
