package com.easygest.springboot.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.easygest.springboot.model.Produto;
import com.easygest.springboot.service.ProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public List<Produto> listar() {
        return produtoService.listarTodos();
    }

    @GetMapping("/{codProduto}")
    public Produto buscarPorCodigo(
            @PathVariable String codProduto) {

        return produtoService.buscarPorCod(codProduto);
    }

    @PostMapping
    public Produto criar(
            @Valid @RequestBody Produto produto) {

        return produtoService.salvar(produto);
    }

    @PutMapping("/{codProduto}")
    public Produto atualizar(
            @PathVariable String codProduto,
            @Valid @RequestBody Produto produto) {

        return produtoService.atualizar(
                codProduto,
                produto);
    }

    @DeleteMapping("/{codProduto}")
    public void excluir(
            @PathVariable String codProduto) {

        produtoService.excluir(codProduto);
    }
}
