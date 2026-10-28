package com.easygest.springboot.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.easygest.springboot.model.Funcionario;
import com.easygest.springboot.service.FuncionarioService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService service;

    @GetMapping
    public List<Funcionario> listar() {
        return service.listar();
    }

    @GetMapping("/{codFuncionario}")
    public Funcionario buscarPorId(
            @PathVariable String codFuncionario) {

        return service.buscarPorId(codFuncionario);
    }

    @PostMapping
    public Funcionario salvar(
            @Valid @RequestBody Funcionario funcionario) {

        return service.salvar(funcionario);
    }

    @PutMapping("/{codFuncionario}")
    public Funcionario atualizar(
            @PathVariable String codFuncionario,
            @Valid @RequestBody Funcionario funcionario) {

        return service.atualizar(
                codFuncionario,
                funcionario);
    }

    @DeleteMapping("/{codFuncionario}")
    public void deletar(
            @PathVariable String codFuncionario) {

        service.deletar(codFuncionario);
    }
}
