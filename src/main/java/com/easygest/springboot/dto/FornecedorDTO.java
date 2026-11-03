package com.easygest.springboot.dto;

import java.time.LocalDate;

/**
 * DTO de Fornecedor. Campo sensível ocultado: cnpjFornecedor.
 */
public record FornecedorDTO(
        String codFornecedor,
        String razaoSocialFornecedor,
        String nomeFornecedor,
        String enderecoFornecedor,
        String telefoneFornecedor,
        String emailFornecedor,
        LocalDate dataCadastroFornecedor
) {
}
