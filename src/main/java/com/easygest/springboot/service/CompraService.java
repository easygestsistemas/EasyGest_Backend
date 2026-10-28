package com.easygest.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easygest.springboot.model.Compra;
import com.easygest.springboot.model.Fornecedor;
import com.easygest.springboot.repository.CompraRepository;
import com.easygest.springboot.repository.FornecedorRepository;

@Service
public class CompraService {

    @Autowired
    private CompraRepository repository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    public List<Compra> listar() {
        return repository.findAll();
    }

    public Compra buscarPorId(String codCompra) {
        return repository.findById(codCompra)
                .orElseThrow(() -> new RuntimeException("Compra não encontrada."));
    }

    public Compra salvar(Compra compra) {
        associarFornecedor(compra);
        return repository.save(compra);
    }

    public Compra atualizar(String codCompra, Compra compra) {
        Compra compraExistente = buscarPorId(codCompra);

        compraExistente.setPedido(compra.getPedido());
        compraExistente.setItem(compra.getItem());
        compraExistente.setDataCompra(compra.getDataCompra());
        compraExistente.setDataRecebimento(compra.getDataRecebimento());
        compraExistente.setValor(compra.getValor());
        compraExistente.setQuantidade(compra.getQuantidade());

        if (compra.getFornecedor() != null) {
            compraExistente.setFornecedor(buscarFornecedor(compra.getFornecedor()));
        } else {
            compraExistente.setFornecedor(null);
        }

        return repository.save(compraExistente);
    }

    public void deletar(String codCompra) {
        repository.deleteById(codCompra);
    }

    private void associarFornecedor(Compra compra) {
        if (compra.getFornecedor() != null) {
            compra.setFornecedor(buscarFornecedor(compra.getFornecedor()));
        }
    }

    private Fornecedor buscarFornecedor(Fornecedor fornecedor) {
        return fornecedorRepository.findById(fornecedor.getCodFornecedor())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado."));
    }
}
