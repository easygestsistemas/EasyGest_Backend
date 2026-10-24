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
@Document(collection = "funcionarios")
public class Funcionario {
    
    @Id
    private String codFuncionario;
    private String nomeFuncionario;
    private String cpfFuncionario;
    private String ctpsFuncionario;
    private String cargoFuncionario;
    private BigDecimal salarioFuncionario;
    private LocalDate dataAdmissaoFuncionario;
    private LocalDate dataDemissaoFuncionario;
    private Usuario usuario;

}

