package com.easygest.springboot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.easygest.springboot.model.Cliente;

public interface ClienteRepository extends MongoRepository<Cliente, String>{


}
