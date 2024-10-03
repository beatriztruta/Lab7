package org.example;

import org.example.model.Estoque;
import org.example.model.Pedido;
import org.example.model.Produto;
import org.example.queue.FilaDePedidos;
import org.example.schedule.Reabastecedor;
import org.example.schedule.RelatorioDeVendas;
import org.example.worker.Worker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App
{
    public static void main( String[] args ) {
        Estoque estoque = new Estoque();
        FilaDePedidos filaDePedidos = new FilaDePedidos(10);

        // Inicializa os workers para processar pedidos
        ExecutorService workers = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            workers.submit(new Worker(filaDePedidos, estoque));
        }

        // Inicia o reabastecimento automático e geração de relatórios
        Reabastecedor.iniciarReabastecimento(estoque);
        RelatorioDeVendas.iniciarRelatorio(estoque);

        // Simula clientes fazendo pedidos
        Produto[] produtos1 = { new Produto("Produto A", 2), new Produto("Produto B", 1) };
        Produto[] produtos2 = { new Produto("Produto A", 3) };

        try {
            filaDePedidos.adicionarPedido(new Pedido("Cliente 1", produtos1));
            filaDePedidos.adicionarPedido(new Pedido("Cliente 2", produtos2));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
