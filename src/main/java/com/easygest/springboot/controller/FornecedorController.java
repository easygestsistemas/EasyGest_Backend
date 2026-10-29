package com.easygest.springboot.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.easygest.springboot.model.Fornecedor;
import com.easygest.springboot.service.FornecedorService;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

    @Autowired
    private FornecedorService service;

    @GetMapping
    public List<Fornecedor> listar() {
        return service.listar();
    }

    @GetMapping("/{codFornecedor}")
    public Fornecedor buscarPorId(
            @PathVariable String codFornecedor) {

        return service.buscarPorId(codFornecedor);
    }

    @PostMapping
    public Fornecedor salvar(
            @Valid @RequestBody Fornecedor fornecedor) {

        return service.salvar(fornecedor);
    }

    @PutMapping("/{codFornecedor}")
    public Fornecedor atualizar(
            @PathVariable String codFornecedor,
            @Valid @RequestBody Fornecedor fornecedor) {

        return service.atualizar(
                codFornecedor,
                fornecedor);
    }

    @DeleteMapping("/{codFornecedor}")
    public void deletar(
            @PathVariable String codFornecedor) {

        service.deletar(codFornecedor);
    }
}
