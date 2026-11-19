package com.easygest.springboot.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.easygest.springboot.dto.FornecedorDTO;
import com.easygest.springboot.dto.FornecedorRequest;
import com.easygest.springboot.model.Fornecedor;
import com.easygest.springboot.service.FornecedorService;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

    @Autowired
    private FornecedorService service;

    @GetMapping
    public List<FornecedorDTO> listar() {
        return service.listar().stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{codFornecedor}")
    public FornecedorDTO buscarPorId(
            @PathVariable String codFornecedor) {
        return toDto(service.buscarPorId(codFornecedor));
    }

    @PostMapping
    public FornecedorDTO salvar(
            @Valid @RequestBody FornecedorRequest request) {
        return toDto(service.salvar(request.toModel()));
    }

    @PutMapping("/{codFornecedor}")
    public FornecedorDTO atualizar(
            @PathVariable String codFornecedor,
            @Valid @RequestBody Fornecedor fornecedor) {
        return toDto(service.atualizar(codFornecedor, fornecedor));
    }

    @DeleteMapping("/{codFornecedor}")
    public void deletar(
            @PathVariable String codFornecedor) {
        service.deletar(codFornecedor);
    }

    private FornecedorDTO toDto(Fornecedor f) {
        return new FornecedorDTO(
                f.getCodFornecedor(),
                f.getRazaoSocialFornecedor(),
                f.getNomeFornecedor(),
                f.getEnderecoFornecedor(),
                f.getTelefoneFornecedor(),
                f.getEmailFornecedor(),
                f.getDataCadastroFornecedor()
        );
    }
}
