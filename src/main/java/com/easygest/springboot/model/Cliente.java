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
@Document(collection = "clientes")
public class Cliente {

    @Id
    private String codCliente;
    private String nomeCliente;
    private String cpfCliente;
    private String enderecoCliente;
    private String telefoneCliente;
    private String emailCliente;
    private String statusCliente;
    private LocalDate dataCadastro;
}
