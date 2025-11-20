package br.com.faculdade.api.controller;

import br.com.faculdade.api.model.Aluno;
import br.com.faculdade.api.service.AlunoService;
import br.com.faculdade.api.util.JwtUtil;
import br.com.faculdade.api.dto.LoginRequest;
import br.com.faculdade.api.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private JwtUtil jwtUtil;

    // GET - Listar todos os alunos
    @GetMapping
    public ResponseEntity<List<Aluno>> findAll() {
        List<Aluno> alunos = alunoService.findAll();
        return ResponseEntity.ok(alunos);
    }

    // GET - Buscar aluno por RA
    @GetMapping("/{ra}")
    public ResponseEntity<?> findByRa(@PathVariable String ra) {
        Optional<Aluno> aluno = alunoService.findByRa(ra);
        return aluno.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST - Criar novo aluno
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Aluno aluno) {
        try {
            Aluno novoAluno = alunoService.save(aluno);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoAluno);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // PUT - Atualizar aluno por RA
    @PutMapping("/{ra}")
    public ResponseEntity<?> update(@PathVariable String ra, @RequestBody Aluno aluno) {
        Optional<Aluno> alunoAtualizado = alunoService.update(ra, aluno);
        return alunoAtualizado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - Soft delete por RA (desativar aluno)
    @DeleteMapping("/{ra}")
    public ResponseEntity<?> softDelete(@PathVariable String ra) {
        boolean deletado = alunoService.softDelete(ra);
        if (deletado) {
            return ResponseEntity.ok("Aluno desativado com sucesso");
        }
        return ResponseEntity.notFound().build();
    }

    // PUT - Reativar aluno por RA
    @PutMapping("/reativar/{ra}")
    public ResponseEntity<?> reativar(@PathVariable String ra) {
        boolean reativado = alunoService.reativar(ra);
        if (reativado) {
            return ResponseEntity.ok("Aluno reativado com sucesso");
        }
        return ResponseEntity.notFound().build();
    }

    // POST - Autenticar aluno (login)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<Aluno> aluno = alunoService.autenticar(loginRequest.getRa(), loginRequest.getSenha());

        if (aluno.isPresent()) {
            Aluno alunoAutenticado = aluno.get();

            // Gerar token JWT
            String token = jwtUtil.generateToken(alunoAutenticado.getRa(), alunoAutenticado.getNome());

            LoginResponse response = new LoginResponse(
                    alunoAutenticado.getRa(),
                    alunoAutenticado.getNome(),
                    alunoAutenticado.getEmail(),
                    token,
                    "Login realizado com sucesso");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("RA ou senha inválidos, ou aluno inativo");
        }
    }

}
