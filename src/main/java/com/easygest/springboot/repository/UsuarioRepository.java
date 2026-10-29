package com.easygest.springboot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.easygest.springboot.model.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
}
