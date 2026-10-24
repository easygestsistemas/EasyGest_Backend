package com.easygest.springboot.model;

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
@Document(collection = "fornecedores")
public class Fornecedor {

    @Id
    private String codFornecedor;
    private String razaoSocialFornecedor;
    private String nomeFornecedor;
    private String cnpjFornecedor;
    private String enderecoFornecedor;
    private String telefoneFornecedor;
    private String emailFornecedor;
    private LocalDate dataCadastroFornecedor;
}