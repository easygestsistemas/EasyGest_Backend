package com.easygest.springboot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.easygest.springboot.model.Funcionario;

public interface FuncionarioRepository extends MongoRepository<Funcionario, String>{


}
