package com.easygest.springboot.dto;

import java.time.LocalDate;

/**
 * DTO de Cliente. Campo sensível ocultado: cpfCliente.
 */
public record ClienteDTO(
        String codCliente,
        String nomeCliente,
        String enderecoCliente,
        String telefoneCliente,
        String emailCliente,
        String statusCliente,
        LocalDate dataCadastro
) {
}
