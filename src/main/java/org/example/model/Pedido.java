package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Pedido implements Comparable<Pedido> {
    private String cliente;
    private Produto[] produtos;
    private int prioridade;
    private boolean pagamentoConfirmado; 
    
    public Pedido(String cliente, Produto[] produtos, int prioridade) {
        this.cliente = cliente;
        this.produtos = produtos;
        this.prioridade = prioridade;
        this.pagamentoConfirmado = false; 

    public String getCliente() {
        return cliente;
    }

    public Produto[] getProdutos() {
        return produtos;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public boolean isPagamentoConfirmado() {
        return pagamentoConfirmado;
    }

    public void confirmarPagamento() {
        this.pagamentoConfirmado = true;
    }

    @Override
    public int compareTo(Pedido outroPedido) {
        return Integer.compare(outroPedido.getPrioridade(), this.prioridade);
    }
}

