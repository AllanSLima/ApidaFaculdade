package br.com.faculdade.api.repository;

import br.com.faculdade.api.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Optional<Aluno> findByRa(String ra);
    boolean existsByRa(String ra);
    // Optional<Aluno> findByRaAndSenha(String ra, String senha);
}
