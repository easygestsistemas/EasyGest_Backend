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
@Document(collection = "clientes")
public class Cliente {

    @Id
    private String codCliente;
    @NotBlank(message = "O nome do cliente é obrigatório.")
    private String nomeCliente;
    private String cpfCliente;
    private String enderecoCliente;
    private String telefoneCliente;
    @Email(message = "O e-mail do cliente deve ser válido.")
    private String emailCliente;
    private String statusCliente;
    private LocalDate dataCadastro;
}
