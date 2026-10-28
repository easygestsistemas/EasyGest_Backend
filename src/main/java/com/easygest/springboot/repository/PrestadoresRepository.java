package com.easygest.springboot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.easygest.springboot.model.Prestadores;

public interface PrestadoresRepository extends MongoRepository<Prestadores, String>{


}
