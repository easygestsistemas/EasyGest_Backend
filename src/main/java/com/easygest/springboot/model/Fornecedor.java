package com.easygest.springboot.model;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@Document(collection = "fornecedores")
public class Fornecedor {

    @Id
    private String codFornecedor;
    @NotBlank(message = "A razão social do fornecedor é obrigatória.")
    private String razaoSocialFornecedor;
    private String nomeFornecedor;
    private String nomeFantasiaFornecedor;
    private String cnpjFornecedor;
    private String enderecoFornecedor;
    private String telefoneFornecedor;
    @Email(message = "O e-mail do fornecedor deve ser válido.")
    private String emailFornecedor;
    private String categoriaFornecedor;
    private LocalDate dataCadastroFornecedor;
}