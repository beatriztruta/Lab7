package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Cliente;
import org.example.model.Pedido;
import org.example.model.Produto;
import org.example.queue.Estoque;
import org.example.queue.FilaDePedidos;
import org.example.schedule.Reabastecedor;
import org.example.schedule.RelatorioDeVendas;
import org.example.worker.Worker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class App {
    public static void main(String[] args) {
        Estoque estoque = new Estoque();
        FilaDePedidos filaDePedidos = new FilaDePedidos(10);

        ExecutorService workers = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            workers.submit(new Worker(filaDePedidos, estoque));
        }

        Reabastecedor.iniciarReabastecimento(estoque);
        RelatorioDeVendas.iniciarRelatorio(estoque);

        Produto[] produtos1 = { new Produto("Produto A", 2), new Produto("Produto B", 1) };
        Produto[] produtos2 = { new Produto("Produto A", 3) };
        Produto[] produtos3 = { new Produto("Produto C", 4) };

        try {
            filaDePedidos.adicionarPedido(new Pedido("Cliente 1", produtos1, 2)); // Prioridade 2
            filaDePedidos.adicionarPedido(new Pedido("Cliente 2", produtos2, 5)); // Prioridade 5 (Alta)
            filaDePedidos.adicionarPedido(new Pedido("Cliente 3", produtos3, 1)); // Prioridade 1 (Baixa)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
