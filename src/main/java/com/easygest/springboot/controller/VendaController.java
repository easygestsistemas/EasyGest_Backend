package com.easygest.springboot.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.easygest.springboot.model.Venda;
import com.easygest.springboot.service.VendaService;

@RestController
@RequestMapping("/vendas")
@CrossOrigin(origins = "*")
public class VendaController {

    @Autowired
    private VendaService service;

    @GetMapping
    public List<Venda> listar() {
        return service.listar();
    }

    @PostMapping
    public Venda salvar(@Valid @RequestBody Venda venda) {
        return service.salvar(venda);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable String id) {
        service.deletar(id);
    }
}