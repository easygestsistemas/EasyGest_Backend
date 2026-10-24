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
@Document(collection = "compras")
public class Compra {

    @Id
    private String codCompra;

    private Integer pedido;

    private String item;

    private LocalDate dataCompra;

    private LocalDate dataRecebimento;

    private BigDecimal valor;

    private Integer quantidade;

    private Fornecedor fornecedor;
}