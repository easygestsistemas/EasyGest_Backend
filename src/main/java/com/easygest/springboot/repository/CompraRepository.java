package com.easygest.springboot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.easygest.springboot.model.Compra;

public interface CompraRepository extends MongoRepository<Compra, String> {
}
