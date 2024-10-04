package org.example.utils;

import java.util.concurrent.TimeUnit;

public abstract class Constantes {

    public static final TimeUnit INTERVALO_ATUALIZACAO_REABASTECEDOR = TimeUnit.SECONDS;

    public static final int PERIODO_INTERVALO_ATUALIZACAO_REABASTECEDOR = 10; // 10 segundos

    public static final TimeUnit INTERVALO_ATUALIZACAO_GERADOR_RELATORIO_VENDAS = TimeUnit.SECONDS;

    public static final int PERIODO_IINTERVALO_ATUALIZACAO_GERADOR_RELATORIO_VENDAS = 30; // 30 segundos

    public static final int QUANTIDADE_ITENS_REABASTECER = 20; // Reabastece com mais itens o produto mais popular
}
