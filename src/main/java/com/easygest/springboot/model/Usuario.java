package com.easygest.springboot.model;

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
@Document(collection = "usuarios")
public class Usuario {
    
    @Id
    private String codUsuario;
    @NotBlank(message = "O nome do usuário é obrigatório.")
    private String nomeUsuario;
    @NotBlank(message = "O tipo do usuário é obrigatório.")
    private String tipoUsuario;
}
