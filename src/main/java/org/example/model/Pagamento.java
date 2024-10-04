package org.example.model;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class Pagamento {
    private static final Random random = new Random();

    public static CompletableFuture<Boolean> processarPagamento(Pedido pedido) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000); // Simulação de tempo de processamento
                boolean aprovado = random.nextInt(100) < 90; // 90% de chance de aprovação
                if (aprovado) {
                    log.info("Pagamento do pedido de Cliente {} confirmado", pedido.getCliente().getId());
                    return true;
                } else {
                    log.info("Pagamento do pedido de Cliente {} rejeitado", pedido.getCliente().getId());
                    return false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        });
    }
}
