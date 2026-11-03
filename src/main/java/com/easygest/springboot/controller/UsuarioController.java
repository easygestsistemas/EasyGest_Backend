package com.easygest.springboot.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.easygest.springboot.dto.UsuarioDTO;
import com.easygest.springboot.model.Usuario;
import com.easygest.springboot.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public List<UsuarioDTO> listar() {
        return service.listar().stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{codUsuario}")
    public UsuarioDTO buscarPorId(
            @PathVariable String codUsuario) {
        return toDto(service.buscarPorId(codUsuario));
    }

    @PostMapping
    public UsuarioDTO salvar(
            @Valid @RequestBody Usuario usuario) {
        return toDto(service.salvar(usuario));
    }

    @PutMapping("/{codUsuario}")
    public UsuarioDTO atualizar(
            @PathVariable String codUsuario,
            @Valid @RequestBody Usuario usuario) {
        return toDto(service.atualizar(codUsuario, usuario));
    }

    @DeleteMapping("/{codUsuario}")
    public void deletar(
            @PathVariable String codUsuario) {
        service.deletar(codUsuario);
    }

    private UsuarioDTO toDto(Usuario u) {
        return new UsuarioDTO(
                u.getCodUsuario(),
                u.getNomeUsuario(),
                u.getTipoUsuario()
        );
    }
}
