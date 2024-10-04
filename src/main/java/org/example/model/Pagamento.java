package org.example.model;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class Pagamento {
    public static CompletableFuture<Boolean> processarPagamento(Pedido pedido) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
                log.info("Pagamento do pedido de {} confirmado", pedido.getCliente().getId());
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        });
    }
}
