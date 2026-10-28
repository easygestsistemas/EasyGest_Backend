package com.easygest.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easygest.springboot.model.ItemVenda;
import com.easygest.springboot.service.ItemVendaService;

@RestController
@RequestMapping("/itens-venda")
@CrossOrigin(origins = "*")
public class ItemVendaController {

    @Autowired
    private ItemVendaService service;

    @GetMapping
    public List<ItemVenda> listar() {
        return service.listar();
    }

    @GetMapping("/{codItemVenda}")
    public ItemVenda buscarPorId(@PathVariable String codItemVenda) {
        return service.buscarPorId(codItemVenda);
    }

    @PostMapping
    public ItemVenda salvar(
            @RequestBody ItemVenda itemVenda) {

        return service.salvar(itemVenda);
    }

    @PutMapping("/{codItemVenda}")
    public ItemVenda atualizar(
            @PathVariable String codItemVenda,
            @RequestBody ItemVenda itemVenda) {
        return service.atualizar(codItemVenda, itemVenda);
    }

    @DeleteMapping("/{codItemVenda}")
    public void deletar(@PathVariable String codItemVenda) {

        service.deletar(codItemVenda);
    }
}