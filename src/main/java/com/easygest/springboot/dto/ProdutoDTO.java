package com.easygest.springboot.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProdutoDTO(
        String codProduto,
        String nomeProduto,
        String tipoProduto,
        Float pesoProduto,
        Float custoProduto,
        Float precoProduto,
        String statusProduto,
        LocalDate dataValidadeProduto,
        BigDecimal quantidadeAtual,
        Integer estoqueMinimo
) {
}
