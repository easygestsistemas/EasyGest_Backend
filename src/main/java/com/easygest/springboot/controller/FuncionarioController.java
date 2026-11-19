package com.easygest.springboot.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.easygest.springboot.dto.FuncionarioDTO;
import com.easygest.springboot.dto.FuncionarioRequest;
import com.easygest.springboot.model.Funcionario;
import com.easygest.springboot.service.FuncionarioService;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService service;

    @GetMapping
    public List<FuncionarioDTO> listar() {
        return service.listar().stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{codFuncionario}")
    public FuncionarioDTO buscarPorId(
            @PathVariable String codFuncionario) {
        return toDto(service.buscarPorId(codFuncionario));
    }

    @PostMapping
    public FuncionarioDTO salvar(
            @Valid @RequestBody FuncionarioRequest request) {
        return toDto(service.salvar(request.toModel()));
    }

    @PutMapping("/{codFuncionario}")
    public FuncionarioDTO atualizar(
            @PathVariable String codFuncionario,
            @Valid @RequestBody Funcionario funcionario) {
        return toDto(service.atualizar(codFuncionario, funcionario));
    }

    @DeleteMapping("/{codFuncionario}")
    public void deletar(
            @PathVariable String codFuncionario) {
        service.deletar(codFuncionario);
    }

    private FuncionarioDTO toDto(Funcionario f) {
        return new FuncionarioDTO(
                f.getCodFuncionario(),
                f.getNomeFuncionario(),
                f.getCargoFuncionario(),
                f.getDataAdmissaoFuncionario(),
                f.getDataDemissaoFuncionario()
        );
    }
}
