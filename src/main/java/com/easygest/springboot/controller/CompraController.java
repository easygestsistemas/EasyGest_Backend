package com.easygest.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easygest.springboot.model.Compra;
import com.easygest.springboot.service.CompraService;

@RestController
@RequestMapping("/compras")
public class CompraController {

    @Autowired
    private CompraService service;

    @GetMapping
    public List<Compra> listar() {
        return service.listar();
    }

    @GetMapping("/{codCompra}")
    public Compra buscarPorId(@PathVariable String codCompra) {
        return service.buscarPorId(codCompra);
    }

    @PostMapping
    public Compra salvar(@RequestBody Compra compra) {
        return service.salvar(compra);
    }

    @PutMapping("/{codCompra}")
    public Compra atualizar(
            @PathVariable String codCompra,
            @RequestBody Compra compra) {
        return service.atualizar(codCompra, compra);
    }

    @DeleteMapping("/{codCompra}")
    public void deletar(@PathVariable String codCompra) {
        service.deletar(codCompra);
    }
}
