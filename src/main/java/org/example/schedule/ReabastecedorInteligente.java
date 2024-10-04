import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReabastecedorInteligente implements Runnable {
    private final Estoque estoque;

    public ReabastecedorInteligente(Estoque estoque) {
        this.estoque = estoque;
    }

    @Override
    public void run() {
        String produtoPopular = estoque.produtoMaisPopular();
        estoque.adicionarProduto(produtoPopular, 20); // Reabastece com mais itens o produto mais popular
        System.out.println("Reabastecimento Inteligente: Produto mais popular é " + produtoPopular);
    }

    public static void iniciarReabastecimento(Estoque estoque) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new ReabastecedorInteligente(estoque), 0, 10, TimeUnit.SECONDS);
    }
}
