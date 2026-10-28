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
@Document(collection = "funcionarios")
public class Funcionario {
    
    @Id
    private String codFuncionario;
    @NotBlank(message = "O nome do funcionário é obrigatório.")
    private String nomeFuncionario;
    private String cpfFuncionario;
    private String ctpsFuncionario;
    @NotBlank(message = "O cargo do funcionário é obrigatório.")
    private String cargoFuncionario;
    @PositiveOrZero(message = "O salário não pode ser negativo.")
    private BigDecimal salarioFuncionario;
    private LocalDate dataAdmissaoFuncionario;
    private LocalDate dataDemissaoFuncionario;
    private Usuario usuario;

}
