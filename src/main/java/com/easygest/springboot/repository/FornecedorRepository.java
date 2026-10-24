package com.easygest.springboot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.easygest.springboot.model.Fornecedor;

public interface FornecedorRepository extends MongoRepository<Fornecedor, String>{


}
