package com.easygest.springboot.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.easygest.springboot.model.Fornecedor;
import com.easygest.springboot.model.Produto;
import com.easygest.springboot.repository.FornecedorRepository;
import com.easygest.springboot.repository.ProdutoRepository;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    // LISTAR TODOS
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    // SALVAR
    public Produto salvar(Produto produto) {

        produto.setFornecedor(resolverFornecedor(produto.getFornecedor()));
        verificarEstoque(produto);

        return produtoRepository.save(produto);
    }

    // BUSCAR POR CÓDIGO
    public Produto buscarPorCod(String codProduto) {

        return produtoRepository.findById(codProduto)
                .orElseThrow(() ->
                        new RuntimeException("Produto não encontrado."));
    }

    // ATUALIZAR
    public Produto atualizar(String codProduto, Produto novo) {

        Produto existente = buscarPorCod(codProduto);

        existente.setNomeProduto(novo.getNomeProduto());
        existente.setTipoProduto(novo.getTipoProduto());
        existente.setPesoProduto(novo.getPesoProduto());
        existente.setCustoProduto(novo.getCustoProduto());
        existente.setPrecoProduto(novo.getPrecoProduto());
        existente.setStatusProduto(novo.getStatusProduto());
        existente.setDataCompraProduto(novo.getDataCompraProduto());
        existente.setDataValidadeProduto(novo.getDataValidadeProduto());
        existente.setDataVendaProduto(novo.getDataVendaProduto());
        existente.setDataFabricacaoProduto(novo.getDataFabricacaoProduto());

        // CAMPOS DE ESTOQUE
        existente.setQuantidadeAtual(novo.getQuantidadeAtual());
        existente.setEstoqueMinimo(novo.getEstoqueMinimo());

        existente.setFornecedor(resolverFornecedor(novo.getFornecedor()));

        verificarEstoque(existente);

        return produtoRepository.save(existente);
    }

    private Fornecedor resolverFornecedor(Fornecedor fornecedor) {
        if (fornecedor == null) {
            return null;
        }

        String codFornecedor = fornecedor.getCodFornecedor();
        if (codFornecedor == null || codFornecedor.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "codFornecedor é obrigatório quando o campo fornecedor é informado.");
        }

        return fornecedorRepository.findById(codFornecedor)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Fornecedor não encontrado para codFornecedor: " + codFornecedor));
    }

    // EXCLUIR
    public void excluir(String codProduto) {

        produtoRepository.deleteById(codProduto);
    }

    // REGRA DE NEGÓCIO
    private void verificarEstoque(Produto produto) {

        if (produto.getQuantidadeAtual() != null
                && produto.getEstoqueMinimo() != null
                && produto.getQuantidadeAtual().compareTo(
                        BigDecimal.valueOf(produto.getEstoqueMinimo())) < 0) {

            System.out.println(
                    "ALERTA: Estoque baixo para o produto "
                            + produto.getNomeProduto());
        }
    }
}