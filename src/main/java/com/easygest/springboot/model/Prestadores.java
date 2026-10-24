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
@Document(collection = "prestadores")
public class Prestadores {

    @Id
    private String codPrestador;
    private String razaoSocialPrestador;
    private String nomePrestador;
    private String cnpjPrestador;
    private String cpfPrestador;
    private String enderecoPrestador;
    private String telefonePrestador;
    private String emailPrestador;
    private LocalDate dataServicoPrestador;
    private LocalDate dataPagamentoPrestador;
    private BigDecimal valorPrestador;
}
