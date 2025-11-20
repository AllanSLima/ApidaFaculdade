package br.com.faculdade.api.repository;

import br.com.faculdade.api.model.Cobranca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CobrancaRepository extends JpaRepository<Cobranca, Long> {
    List<Cobranca> findByAluno_Ra(String ra);
    Optional<Cobranca> findByLinhaDigitavel(String linhaDigitavel);
    boolean existsByLinhaDigitavel(String linhaDigitavel);
}
