package com.easygest.springboot.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.easygest.springboot.model.Cliente;
import com.easygest.springboot.service.ClienteService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @GetMapping
    public List<Cliente> listar() {
        return service.listar();
    }

    @GetMapping("/{codCliente}")
    public Cliente buscarPorId(@PathVariable String codCliente) {
        return service.buscarPorId(codCliente);
    }

    @PostMapping
    public Cliente salvar(@Valid @RequestBody Cliente cliente) {
        return service.salvar(cliente);
    }

    @PutMapping("/{codCliente}")
    public Cliente atualizar(
            @PathVariable String codCliente,
            @Valid @RequestBody Cliente cliente) {

        return service.atualizar(codCliente, cliente);
    }

    @DeleteMapping("/{codCliente}")
    public void deletar(@PathVariable String codCliente) {
        service.deletar(codCliente);
    }
}
