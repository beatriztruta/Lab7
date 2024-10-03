package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Pedido {
    private final Cliente cliente;
    private final Produto[] produtos;

}
