package com.easygest.springboot.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
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

    @NotBlank(message = "O nome do produto é obrigatório.")
    private String nomeProduto;
    private String tipoProduto;
    @PositiveOrZero(message = "O peso não pode ser negativo.")
    private Float pesoProduto;
    @PositiveOrZero(message = "O custo não pode ser negativo.")
    private Float custoProduto;
    @PositiveOrZero(message = "O preço não pode ser negativo.")
    private Float precoProduto;
    private String statusProduto;
    private LocalDate dataCompraProduto;
    private LocalDate dataValidadeProduto;
    private LocalDate dataVendaProduto;
    private LocalDate dataFabricacaoProduto;
    @PositiveOrZero(message = "A quantidade em estoque não pode ser negativa.")
    private BigDecimal quantidadeAtual;
    @PositiveOrZero(message = "O estoque mínimo não pode ser negativo.")
    private Integer estoqueMinimo;

    private Fornecedor fornecedor;

}