package com.easygest.springboot.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.easygest.springboot.dto.ClienteDTO;
import com.easygest.springboot.model.Cliente;
import com.easygest.springboot.service.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @GetMapping
    public List<ClienteDTO> listar() {
        return service.listar().stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{codCliente}")
    public ClienteDTO buscarPorId(@PathVariable String codCliente) {
        return toDto(service.buscarPorId(codCliente));
    }

    @PostMapping
    public ClienteDTO salvar(@Valid @RequestBody Cliente cliente) {
        return toDto(service.salvar(cliente));
    }

    @PutMapping("/{codCliente}")
    public ClienteDTO atualizar(
            @PathVariable String codCliente,
            @Valid @RequestBody Cliente cliente) {
        return toDto(service.atualizar(codCliente, cliente));
    }

    @DeleteMapping("/{codCliente}")
    public void deletar(@PathVariable String codCliente) {
        service.deletar(codCliente);
    }

    private ClienteDTO toDto(Cliente c) {
        return new ClienteDTO(
                c.getCodCliente(),
                c.getNomeCliente(),
                c.getEnderecoCliente(),
                c.getTelefoneCliente(),
                c.getEmailCliente(),
                c.getStatusCliente(),
                c.getDataCadastro()
        );
    }
}
