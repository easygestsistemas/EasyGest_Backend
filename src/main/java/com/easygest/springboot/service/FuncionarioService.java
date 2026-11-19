package com.easygest.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easygest.springboot.model.Funcionario;
import com.easygest.springboot.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    // LISTAR
    public List<Funcionario> listar() {
        return funcionarioRepository.findAll();
    }

    // SALVAR
    public Funcionario salvar(Funcionario funcionario) {
        return funcionarioRepository.save(funcionario);
    }

    // BUSCAR POR ID
    public Funcionario buscarPorId(String codFuncionario) {

        return funcionarioRepository.findById(codFuncionario)
                .orElseThrow();
    }

    // ATUALIZAR
    public Funcionario atualizar(String codFuncionario, Funcionario funcionario) {

        Funcionario funcionarioExistente = buscarPorId(codFuncionario);

        funcionarioExistente.setNomeFuncionario(funcionario.getNomeFuncionario());
        funcionarioExistente.setCpfFuncionario(funcionario.getCpfFuncionario());
        funcionarioExistente.setCtpsFuncionario(funcionario.getCtpsFuncionario());
        funcionarioExistente.setCargoFuncionario(funcionario.getCargoFuncionario());
        funcionarioExistente.setSalarioFuncionario(funcionario.getSalarioFuncionario());
        funcionarioExistente.setDataAdmissaoFuncionario(funcionario.getDataAdmissaoFuncionario());
        funcionarioExistente.setDataDemissaoFuncionario(funcionario.getDataDemissaoFuncionario());

        return funcionarioRepository.save(funcionarioExistente);
    }

    // DELETAR
    public void deletar(String codFuncionario) {

        funcionarioRepository.deleteById(codFuncionario);
    }
}