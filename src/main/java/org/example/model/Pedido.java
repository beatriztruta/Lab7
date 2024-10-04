package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Pedido implements Comparable<Pedido> {
    private Cliente cliente;
    private Produto[] produtos;
    private int prioridade;
    private boolean pagamentoConfirmado; 
    
    public Pedido(Cliente cliente, Produto[] produtos, int prioridade) {
        this.cliente = cliente;
        this.produtos = produtos;
        this.prioridade = prioridade;
        this.pagamentoConfirmado = false;
    }

    @Override
    public int compareTo(Pedido outroPedido) {
        return Integer.compare(outroPedido.getPrioridade(), this.prioridade);
    }
}

