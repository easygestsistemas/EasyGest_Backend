package com.easygest.springboot.controller;

import java.util.List;

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

    @GetMapping("/{codVenda}")
    public Venda buscarPorId(@PathVariable String codVenda) {
        return service.buscarPorId(codVenda);
    }

    @PostMapping
    public Venda salvar(@RequestBody Venda venda) {
        return service.salvar(venda);
    }

    @PutMapping("/{codVenda}")
    public Venda atualizar(
            @PathVariable String codVenda,
            @RequestBody Venda venda) {
        return service.atualizar(codVenda, venda);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable String id) {
        service.deletar(id);
    }
}