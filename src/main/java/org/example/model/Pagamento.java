import java.util.concurrent.CompletableFuture;

public class Pagamento {
    public static CompletableFuture<Boolean> processarPagamento(Pedido pedido) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
                System.out.println("Pagamento do pedido de " + pedido.getCliente() + " confirmado.");
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        });
    }
}
