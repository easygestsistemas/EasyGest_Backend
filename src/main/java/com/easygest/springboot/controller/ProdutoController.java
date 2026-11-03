package com.easygest.springboot.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.easygest.springboot.dto.ProdutoDTO;
import com.easygest.springboot.model.Produto;
import com.easygest.springboot.service.ProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public List<ProdutoDTO> listar() {
        return produtoService.listarTodos().stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{codProduto}")
    public ProdutoDTO buscarPorCodigo(
            @PathVariable String codProduto) {
        return toDto(produtoService.buscarPorCod(codProduto));
    }

    @PostMapping
    public ProdutoDTO criar(
            @Valid @RequestBody Produto produto) {
        return toDto(produtoService.salvar(produto));
    }

    @PutMapping("/{codProduto}")
    public ProdutoDTO atualizar(
            @PathVariable String codProduto,
            @Valid @RequestBody Produto produto) {
        return toDto(produtoService.atualizar(codProduto, produto));
    }

    private ProdutoDTO toDto(Produto p) {
        return new ProdutoDTO(
                p.getCodProduto(),
                p.getNomeProduto(),
                p.getTipoProduto(),
                p.getPesoProduto(),
                p.getCustoProduto(),
                p.getPrecoProduto(),
                p.getStatusProduto(),
                p.getDataValidadeProduto(),
                p.getQuantidadeAtual(),
                p.getEstoqueMinimo()
        );
    }

    @DeleteMapping("/{codProduto}")
    public void excluir(
            @PathVariable String codProduto) {

        produtoService.excluir(codProduto);
    }
}
