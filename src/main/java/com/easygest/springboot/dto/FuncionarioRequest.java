package com.easygest.springboot.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.easygest.springboot.model.Funcionario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record FuncionarioRequest(
        @NotBlank(message = "O nome do funcionário é obrigatório.")
        String nomeFuncionario,
        String cpfFuncionario,
        String ctpsFuncionario,
        @NotBlank(message = "O cargo do funcionário é obrigatório.")
        String cargoFuncionario,
        @PositiveOrZero(message = "O salário não pode ser negativo.")
        BigDecimal salarioFuncionario,
        LocalDate dataAdmissaoFuncionario,
        LocalDate dataDemissaoFuncionario
) {
    public Funcionario toModel() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNomeFuncionario(nomeFuncionario);
        funcionario.setCpfFuncionario(cpfFuncionario);
        funcionario.setCtpsFuncionario(ctpsFuncionario);
        funcionario.setCargoFuncionario(cargoFuncionario);
        funcionario.setSalarioFuncionario(salarioFuncionario);
        funcionario.setDataAdmissaoFuncionario(dataAdmissaoFuncionario);
        funcionario.setDataDemissaoFuncionario(dataDemissaoFuncionario);
        return funcionario;
    }
}
