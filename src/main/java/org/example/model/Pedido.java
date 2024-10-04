package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Pedido implements Comparable<Pedido> {
    private String cliente;
    private Produto[] produtos;
    private int prioridade; 

    public Pedido(String cliente, Produto[] produtos, int prioridade) {
        this.cliente = cliente;
        this.produtos = produtos;
        this.prioridade = prioridade; 
    }

    public String getCliente() {
        return cliente;
    }

    public Produto[] getProdutos() {
        return produtos;
    }

    public int getPrioridade() {
        return prioridade;
    }

    @Override
    public int compareTo(Pedido outroPedido) {
        return Integer.compare(outroPedido.getPrioridade(), this.prioridade);
    }
}
