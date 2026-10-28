package com.easygest.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easygest.springboot.model.Cliente;
import com.easygest.springboot.model.Venda;
import com.easygest.springboot.repository.ClienteRepository;
import com.easygest.springboot.repository.VendaRepository;

@Service
public class VendaService {

    @Autowired
    private VendaRepository repository;

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Venda> listar() {
        return repository.findAll();
    }

    public Venda salvar(Venda venda) {
        venda.setCliente(buscarCliente(venda));
        return repository.save(venda);
    }

    public Venda buscarPorId(String codVenda) {
        return repository.findById(codVenda)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada."));
    }

    public Venda atualizar(String codVenda, Venda venda) {
        Venda vendaExistente = buscarPorId(codVenda);

        vendaExistente.setDataVenda(venda.getDataVenda());
        vendaExistente.setValorTotal(venda.getValorTotal());
        vendaExistente.setStatus(venda.getStatus());
        vendaExistente.setCliente(buscarCliente(venda));

        return repository.save(vendaExistente);
    }

    public void deletar(String codVenda) {
        repository.deleteById(codVenda);
    }

    private Cliente buscarCliente(Venda venda) {
        if (venda.getCliente() == null || venda.getCliente().getCodCliente() == null) {
            throw new IllegalArgumentException("O código do cliente é obrigatório.");
        }

        return clienteRepository.findById(venda.getCliente().getCodCliente())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
    }
}