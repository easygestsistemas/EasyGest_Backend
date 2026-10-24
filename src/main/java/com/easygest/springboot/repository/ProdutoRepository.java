package com.easygest.springboot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.easygest.springboot.model.Produto;

public interface ProdutoRepository extends MongoRepository<Produto, String>{


}
