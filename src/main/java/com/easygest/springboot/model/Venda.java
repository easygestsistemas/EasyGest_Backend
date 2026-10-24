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
@Document(collection = "vendas")
public class Venda {

    @Id
    private String codVenda;
    private LocalDate dataVenda;
    private BigDecimal valorTotal;
    private String status;
    private Cliente cliente;
}
