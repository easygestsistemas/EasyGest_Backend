package com.easygest.springboot.model;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
@Document(collection = "itens_venda")
public class ItemVenda {

    @Id
    private String codItemVenda;
    @NotNull(message = "A venda é obrigatória para o item.")
    private Venda venda;
    @NotNull(message = "O produto é obrigatório para o item.")
    private Produto produto;
    @NotNull(message = "A quantidade é obrigatória.")
    @Positive(message = "A quantidade deve ser maior que zero.")
    private Integer quantidade;
    @NotNull(message = "O valor unitário é obrigatório.")
    @PositiveOrZero(message = "O valor unitário não pode ser negativo.")
    private BigDecimal valorUnitario;
    private BigDecimal subtotal;
}