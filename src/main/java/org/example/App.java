package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Cliente;
import org.example.model.Pedido;
import org.example.model.Produto;
import org.example.queue.Estoque;
import org.example.queue.FilaDePedidos;
import org.example.schedule.ReabastecedorInteligente;
import org.example.schedule.RelatorioDeVendas;
import org.example.worker.Worker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class App {
    public static void main(String[] args) {
        // Inicializa o estoque com produtos
        Estoque estoque = new Estoque();
        estoque.adicionarProduto("Produto A", 10);
        estoque.adicionarProduto("Produto B", 20);
        estoque.adicionarProduto("Produto C", 15);

        // Inicializa a fila de pedidos
        FilaDePedidos filaDePedidos = new FilaDePedidos(10);

        // Cria o relatório de vendas
        RelatorioDeVendas relatorioDeVendas = new RelatorioDeVendas(estoque);
        relatorioDeVendas.iniciar(); // Inicia a geração de relatórios

        // Cria threads para processar pedidos
        ExecutorService workers = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            workers.submit(new Worker(filaDePedidos, estoque, relatorioDeVendas));
        }

        // Inicia reabastecimento inteligente
        ReabastecedorInteligente reabastecedorInteligente = new ReabastecedorInteligente(estoque);
        reabastecedorInteligente.iniciarReabastecimento();

        // Adiciona alguns pedidos para teste
        Produto[] produtos1 = {new Produto("Produto A", 2), new Produto("Produto B", 1)};
        Produto[] produtos2 = {new Produto("Produto A", 3)};
        Produto[] produtos3 = {new Produto("Produto C", 4)};

        try {
            filaDePedidos.adicionarPedido(new Pedido(new Cliente(1), produtos1, 2));
            filaDePedidos.adicionarPedido(new Pedido(new Cliente(2), produtos2, 5)); // Prioridade Alta
            filaDePedidos.adicionarPedido(new Pedido(new Cliente(3), produtos3, 1)); // Prioridade Baixa
        } catch (InterruptedException e) {
            log.error("Erro ao adicionar pedidos: ", e);
        }
    }
}
