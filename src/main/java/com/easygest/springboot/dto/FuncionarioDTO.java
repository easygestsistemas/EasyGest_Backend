package com.easygest.springboot.dto;

import java.time.LocalDate;

/**
 * DTO de Funcionario. Campos sensíveis ocultados: cpfFuncionario, ctpsFuncionario, salarioFuncionario.
 */
public record FuncionarioDTO(
        String codFuncionario,
        String nomeFuncionario,
        String cargoFuncionario,
        LocalDate dataAdmissaoFuncionario,
        LocalDate dataDemissaoFuncionario
) {
}
