package br.com.faculdade.api.controller;

import br.com.faculdade.api.model.Cobranca;
import br.com.faculdade.api.service.CobrancaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cobrancas")
public class CobrancaController {

    @Autowired
    private CobrancaService cobrancaService;

    // GET - Listar todas as cobranças
    @GetMapping
    public ResponseEntity<List<Cobranca>> findAll() {
        List<Cobranca> cobrancas = cobrancaService.findAll();
        return ResponseEntity.ok(cobrancas);
    }

    // GET - Buscar cobranças por RA do aluno
    @GetMapping("/aluno/{ra}")
    public ResponseEntity<List<Cobranca>> findByAlunoRa(@PathVariable String ra) {
        List<Cobranca> cobrancas = cobrancaService.findByAlunoRa(ra);
        return ResponseEntity.ok(cobrancas);
    }

    // GET - Buscar cobrança por linha digitável
    @GetMapping("/linha/{linhaDigitavel}")
    public ResponseEntity<?> findByLinhaDigitavel(@PathVariable String linhaDigitavel) {
        Optional<Cobranca> cobranca = cobrancaService.findByLinhaDigitavel(linhaDigitavel);
        return cobranca.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST - Criar nova cobrança
    @PostMapping
    public ResponseEntity<?> create(
            @RequestParam String ra,
            @RequestParam BigDecimal valor,
            @RequestParam String dataVencimento // Formato: dd/MM/yyyy
    ) {
        LocalDate vencimento;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            vencimento = LocalDate.parse(dataVencimento, formatter);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Data de vencimento inválida. Use o formato dd/MM/yyyy");
        }

        Optional<Cobranca> cobrancaCriada = cobrancaService.criarCobranca(ra, valor, vencimento);
        if (cobrancaCriada.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(cobrancaCriada.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aluno não encontrado");
        }
    }

    // PUT - Atualizar status da cobrança
    @PutMapping("/status/{linhaDigitavel}")
    public ResponseEntity<?> atualizarStatus(
            @PathVariable String linhaDigitavel,
            @RequestParam String status) {
        Optional<Cobranca> cobrancaAtualizada = cobrancaService.atualizarStatus(linhaDigitavel, status);
        return cobrancaAtualizada.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
