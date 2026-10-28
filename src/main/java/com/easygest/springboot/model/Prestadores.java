package com.easygest.springboot.model;


import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.Email;
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
@Document(collection = "prestadores")
public class Prestadores {

    @Id
    private String codPrestador;
    @NotBlank(message = "A razão social do prestador é obrigatória.")
    private String razaoSocialPrestador;
    private String nomePrestador;
    private String cnpjPrestador;
    private String cpfPrestador;
    private String enderecoPrestador;
    private String telefonePrestador;
    @Email(message = "O e-mail do prestador deve ser válido.")
    private String emailPrestador;
    private LocalDate dataServicoPrestador;
    private LocalDate dataPagamentoPrestador;
    @PositiveOrZero(message = "O valor do prestador não pode ser negativo.")
    private BigDecimal valorPrestador;
}
