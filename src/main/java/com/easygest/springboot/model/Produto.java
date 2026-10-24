package com.easygest.springboot.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "produtos")
public class Produto {

    @Id
    private String codProduto;

    private String nomeProduto;
    private String tipoProduto;
    private Float pesoProduto;
    private Float custoProduto;
    private Float precoProduto;
    private String statusProduto;
    private LocalDate dataCompraProduto;
    private LocalDate dataValidadeProduto;
    private LocalDate dataVendaProduto;
    private LocalDate dataFabricacaoProduto;
    private BigDecimal quantidadeAtual;
    private Integer estoqueMinimo;

    private Fornecedor fornecedor;

}