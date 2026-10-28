package com.easygest.springboot.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easygest.springboot.model.ItemVenda;
import com.easygest.springboot.model.Produto;
import com.easygest.springboot.model.Venda;
import com.easygest.springboot.repository.ItemVendaRepository;
import com.easygest.springboot.repository.ProdutoRepository;
import com.easygest.springboot.repository.VendaRepository;

@Service
public class ItemVendaService {

    @Autowired
    private ItemVendaRepository repository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private VendaRepository vendaRepository;

    public List<ItemVenda> listar() {
        return repository.findAll();
    }

    public ItemVenda buscarPorId(String codItemVenda) {
        return repository.findById(codItemVenda)
                .orElseThrow(() -> new RuntimeException("Item de venda não encontrado."));
    }

    public ItemVenda salvar(ItemVenda itemVenda) {
        validarItem(itemVenda);
        Produto produto = buscarProduto(itemVenda);
        Venda venda = buscarVenda(itemVenda);
        BigDecimal subtotal = calcularSubtotal(itemVenda);

        BigDecimal estoqueAtual = valorOuZero(produto.getQuantidadeAtual());
        BigDecimal quantidade = BigDecimal.valueOf(itemVenda.getQuantidade());
        validarEstoque(estoqueAtual, quantidade);

        produto.setQuantidadeAtual(estoqueAtual.subtract(quantidade));
        venda.setValorTotal(valorOuZero(venda.getValorTotal()).add(subtotal));
        itemVenda.setProduto(produto);
        itemVenda.setVenda(venda);
        itemVenda.setSubtotal(subtotal);

        produtoRepository.save(produto);
        vendaRepository.save(venda);
        return repository.save(itemVenda);
    }

    public ItemVenda atualizar(String codItemVenda, ItemVenda itemVenda) {
        ItemVenda existente = buscarPorId(codItemVenda);
        validarItem(itemVenda);

        String codProdutoAnterior = obterCodigoProduto(existente);
        String codVendaAnterior = obterCodigoVenda(existente);
        Produto produtoAnterior = produtoRepository.findById(codProdutoAnterior)
                .orElseThrow(() -> new RuntimeException("Produto do item não encontrado."));
        Venda vendaAnterior = vendaRepository.findById(codVendaAnterior)
                .orElseThrow(() -> new RuntimeException("Venda do item não encontrada."));

        Produto produtoNovo = itemVenda.getProduto().getCodProduto().equals(codProdutoAnterior)
                ? produtoAnterior
                : buscarProduto(itemVenda);
        Venda vendaNova = itemVenda.getVenda().getCodVenda().equals(codVendaAnterior)
                ? vendaAnterior
                : buscarVenda(itemVenda);

        BigDecimal quantidadeAnterior = BigDecimal.valueOf(existente.getQuantidade());
        BigDecimal quantidadeNova = BigDecimal.valueOf(itemVenda.getQuantidade());
        BigDecimal estoqueAnterior = valorOuZero(produtoAnterior.getQuantidadeAtual());
        BigDecimal estoqueDisponivel = produtoNovo == produtoAnterior
                ? estoqueAnterior.add(quantidadeAnterior)
                : valorOuZero(produtoNovo.getQuantidadeAtual());
        validarEstoque(estoqueDisponivel, quantidadeNova);
        BigDecimal estoqueNovo = estoqueDisponivel.subtract(quantidadeNova);

        BigDecimal subtotalAnterior = existente.getSubtotal() != null
                ? existente.getSubtotal()
                : calcularSubtotal(existente);
        BigDecimal subtotalNovo = calcularSubtotal(itemVenda);
        BigDecimal totalAnterior = valorOuZero(vendaAnterior.getValorTotal());
        BigDecimal totalNovo = valorOuZero(vendaNova.getValorTotal());

        if (vendaNova == vendaAnterior) {
            vendaNova.setValorTotal(totalAnterior.subtract(subtotalAnterior).add(subtotalNovo));
        } else {
            vendaAnterior.setValorTotal(totalAnterior.subtract(subtotalAnterior));
            vendaNova.setValorTotal(totalNovo.add(subtotalNovo));
        }

        if (produtoNovo != produtoAnterior) {
            produtoAnterior.setQuantidadeAtual(estoqueAnterior.add(quantidadeAnterior));
        }
        produtoNovo.setQuantidadeAtual(estoqueNovo);

        existente.setQuantidade(itemVenda.getQuantidade());
        existente.setValorUnitario(itemVenda.getValorUnitario());
        existente.setSubtotal(subtotalNovo);
        existente.setProduto(produtoNovo);
        existente.setVenda(vendaNova);

        produtoRepository.save(produtoAnterior);
        if (produtoNovo != produtoAnterior) {
            produtoRepository.save(produtoNovo);
        }
        vendaRepository.save(vendaAnterior);
        if (vendaNova != vendaAnterior) {
            vendaRepository.save(vendaNova);
        }

        return repository.save(existente);
    }

    public void deletar(String codItemVenda) {
        ItemVenda itemVenda = buscarPorId(codItemVenda);
        BigDecimal quantidade = BigDecimal.valueOf(itemVenda.getQuantidade());
        BigDecimal subtotal = itemVenda.getSubtotal() != null
                ? itemVenda.getSubtotal()
                : calcularSubtotal(itemVenda);

        Produto produto = produtoRepository.findById(obterCodigoProduto(itemVenda))
                .orElseThrow(() -> new RuntimeException("Produto do item não encontrado."));
        Venda venda = vendaRepository.findById(obterCodigoVenda(itemVenda))
                .orElseThrow(() -> new RuntimeException("Venda do item não encontrada."));

        produto.setQuantidadeAtual(valorOuZero(produto.getQuantidadeAtual()).add(quantidade));
        venda.setValorTotal(valorOuZero(venda.getValorTotal()).subtract(subtotal));

        produtoRepository.save(produto);
        vendaRepository.save(venda);
        repository.delete(itemVenda);
    }

    private void validarItem(ItemVenda itemVenda) {
        if (itemVenda.getQuantidade() == null || itemVenda.getQuantidade() <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        if (itemVenda.getValorUnitario() == null
                || itemVenda.getValorUnitario().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor unitário deve ser maior ou igual a zero.");
        }
        obterCodigoProduto(itemVenda);
        obterCodigoVenda(itemVenda);
    }

    private Produto buscarProduto(ItemVenda itemVenda) {
        return produtoRepository.findById(obterCodigoProduto(itemVenda))
                .orElseThrow(() -> new RuntimeException("Produto não encontrado."));
    }

    private Venda buscarVenda(ItemVenda itemVenda) {
        return vendaRepository.findById(obterCodigoVenda(itemVenda))
                .orElseThrow(() -> new RuntimeException("Venda não encontrada."));
    }

    private String obterCodigoProduto(ItemVenda itemVenda) {
        if (itemVenda.getProduto() == null || itemVenda.getProduto().getCodProduto() == null) {
            throw new IllegalArgumentException("O código do produto é obrigatório.");
        }
        return itemVenda.getProduto().getCodProduto();
    }

    private String obterCodigoVenda(ItemVenda itemVenda) {
        if (itemVenda.getVenda() == null || itemVenda.getVenda().getCodVenda() == null) {
            throw new IllegalArgumentException("O código da venda é obrigatório.");
        }
        return itemVenda.getVenda().getCodVenda();
    }

    private BigDecimal calcularSubtotal(ItemVenda itemVenda) {
        return itemVenda.getValorUnitario()
                .multiply(BigDecimal.valueOf(itemVenda.getQuantidade()));
    }

    private BigDecimal valorOuZero(BigDecimal valor) {
        return valor != null ? valor : BigDecimal.ZERO;
    }

    private void validarEstoque(BigDecimal estoque, BigDecimal quantidade) {
        if (estoque.compareTo(quantidade) < 0) {
            throw new IllegalArgumentException("Estoque insuficiente para o produto.");
        }
    }
}