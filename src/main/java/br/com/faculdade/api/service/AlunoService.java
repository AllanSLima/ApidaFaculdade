package br.com.faculdade.api.service;

import br.com.faculdade.api.model.Aluno;
import br.com.faculdade.api.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Listar todos os alunos
    public List<Aluno> findAll() {
        return alunoRepository.findAll();
    }

    // Buscar aluno por RA
    public Optional<Aluno> findByRa(String ra) {
        return alunoRepository.findByRa(ra);
    }

    // Criar novo aluno
    public Aluno save(Aluno aluno) {
        if (alunoRepository.existsByRa(aluno.getRa())) {
            throw new RuntimeException("RA já cadastrado");
        }
        aluno.setSenha(passwordEncoder.encode(aluno.getSenha())); // hash aqui
        aluno.setAtivo(true);
        return alunoRepository.save(aluno);
    }

    // Atualizar aluno por RA
    public Optional<Aluno> update(String ra, Aluno alunoAtualizado) {
        Optional<Aluno> alunoExistente = alunoRepository.findByRa(ra);
        if (alunoExistente.isPresent()) {
            Aluno aluno = alunoExistente.get();
            aluno.setNome(alunoAtualizado.getNome());
            aluno.setEmail(alunoAtualizado.getEmail());
            // Só re-hash se senha foi alterada (não vier nula ou vazia)
            if (alunoAtualizado.getSenha() != null && !alunoAtualizado.getSenha().isEmpty()) {
                aluno.setSenha(passwordEncoder.encode(alunoAtualizado.getSenha()));
            }
            aluno.setMensalidade(alunoAtualizado.getMensalidade());
            return Optional.of(alunoRepository.save(aluno));
        }
        return Optional.empty();
    }

    // Soft delete por RA (desativar aluno)
    public boolean softDelete(String ra) {
        Optional<Aluno> alunoExistente = alunoRepository.findByRa(ra);
        if (alunoExistente.isPresent()) {
            Aluno aluno = alunoExistente.get();
            aluno.setAtivo(false);
            alunoRepository.save(aluno);
            return true;
        }
        return false;
    }

    // Reativar aluno por RA
    public boolean reativar(String ra) {
        Optional<Aluno> alunoExistente = alunoRepository.findByRa(ra);
        if (alunoExistente.isPresent()) {
            Aluno aluno = alunoExistente.get();
            aluno.setAtivo(true);
            alunoRepository.save(aluno);
            return true;
        }
        return false;
    }

    // Autenticar aluno (login)
    public Optional<Aluno> autenticar(String ra, String senha) {
        Optional<Aluno> alunoOpt = alunoRepository.findByRa(ra);

        if (alunoOpt.isPresent() && alunoOpt.get().getAtivo()) {
            Aluno aluno = alunoOpt.get();
            if (passwordEncoder.matches(senha, aluno.getSenha())) {
                return Optional.of(aluno);
            }
        }
        return Optional.empty();
    }

}
