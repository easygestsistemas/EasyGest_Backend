package com.easygest.springboot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.easygest.springboot.model.ItemVenda;

public interface ItemVendaRepository
        extends MongoRepository<ItemVenda, String> {
}
