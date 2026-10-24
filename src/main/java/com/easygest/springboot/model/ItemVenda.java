package com.easygest.springboot.model;

import java.math.BigDecimal;

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
@Document(collection = "itens_venda")
public class ItemVenda {

    @Id
    private String codItemVenda;
    private Venda venda;
    private Produto produto;
    private Integer quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal subtotal;
}