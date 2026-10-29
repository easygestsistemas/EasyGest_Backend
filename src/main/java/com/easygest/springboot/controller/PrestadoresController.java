package com.easygest.springboot.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.easygest.springboot.model.Prestadores;
import com.easygest.springboot.service.PrestadoresService;

@RestController
@RequestMapping("/prestadores")
public class PrestadoresController {

    @Autowired
    private PrestadoresService service;

    @GetMapping
    public List<Prestadores> listar() {
        return service.listar();
    }

    @GetMapping("/{codPrestador}")
    public Prestadores buscarPorId(
            @PathVariable String codPrestador) {

        return service.buscarPorId(codPrestador);
    }

    @PostMapping
    public Prestadores salvar(
            @Valid @RequestBody Prestadores prestador) {

        return service.salvar(prestador);
    }

    @PutMapping("/{codPrestador}")
    public Prestadores atualizar(
            @PathVariable String codPrestador,
            @Valid @RequestBody Prestadores prestador) {

        return service.atualizar(
                codPrestador,
                prestador);
    }

    @DeleteMapping("/{codPrestador}")
    public void deletar(
            @PathVariable String codPrestador) {

        service.deletar(codPrestador);
    }
}
