package com.easygest.springboot.dto;

import java.time.LocalDate;

import com.easygest.springboot.model.Fornecedor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record FornecedorRequest(
        @NotBlank(message = "A razão social do fornecedor é obrigatória.")
        String nomeFornecedor,
        String nomeFantasiaFornecedor,
        String cnpjFornecedor,
        String telefoneFornecedor,
        @Email(message = "O e-mail do fornecedor deve ser válido.")
        String emailFornecedor,
        String categoriaFornecedor,
        LocalDate dataCadastro
) {
    public Fornecedor toModel() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setRazaoSocialFornecedor(nomeFornecedor);
        fornecedor.setNomeFornecedor(nomeFornecedor);
        fornecedor.setNomeFantasiaFornecedor(nomeFantasiaFornecedor);
        fornecedor.setCnpjFornecedor(cnpjFornecedor);
        fornecedor.setTelefoneFornecedor(telefoneFornecedor);
        fornecedor.setEmailFornecedor(emailFornecedor);
        fornecedor.setCategoriaFornecedor(categoriaFornecedor);
        fornecedor.setDataCadastroFornecedor(dataCadastro);
        return fornecedor;
    }
}
