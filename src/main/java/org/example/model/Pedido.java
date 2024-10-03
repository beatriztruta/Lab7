package org.example.model;


public class Pedido {
    private String cliente;
    private Produto[] produtos;

    public Pedido(String cliente, Produto[] produtos) {
        this.cliente = cliente;
        this.produtos = produtos;
    }

    public String getCliente() {
        return cliente;
    }

    public Produto[] getProdutos() {
        return produtos;
    }
}
