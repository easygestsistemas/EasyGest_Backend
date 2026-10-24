package com.easygest.springboot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.easygest.springboot.model.Venda;

public interface VendaRepository
        extends MongoRepository<Venda, String> {
}