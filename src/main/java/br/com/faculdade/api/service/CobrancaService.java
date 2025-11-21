package br.com.faculdade.api.service;

import br.com.faculdade.api.model.Aluno;
import br.com.faculdade.api.model.Cobranca;
import br.com.faculdade.api.repository.CobrancaRepository;
import br.com.faculdade.api.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CobrancaService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_BOLETOS_URL = "https://apidosboletos.onrender.com/boletos";

    @Autowired
    private CobrancaRepository cobrancaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    // Listar todas as cobranças
    public List<Cobranca> findAll() {
        return cobrancaRepository.findAll();
    }

    // Buscar cobranças por RA do aluno
    public List<Cobranca> findByAlunoRa(String ra) {
        return cobrancaRepository.findByAluno_Ra(ra);
    }

    // Buscar cobrança por linha digitável
    public Optional<Cobranca> findByLinhaDigitavel(String linhaDigitavel) {
        return cobrancaRepository.findByLinhaDigitavel(linhaDigitavel);
    }

    public Optional<Cobranca> criarCobranca(String ra, BigDecimal valor, LocalDate dataVencimento) {
        Optional<Aluno> alunoOpt = alunoRepository.findByRa(ra);
        if (alunoOpt.isEmpty()) {
            return Optional.empty();
        }

        Aluno aluno = alunoOpt.get();

        String linhaDigitavel = gerarLinhaDigitavel();

        Cobranca cobranca = new Cobranca(aluno, linhaDigitavel, valor, dataVencimento);
        Cobranca salva = cobrancaRepository.save(cobranca);

        // Mapear dados para objeto dto ou map para envio API boletos
        var boletoObj = new java.util.HashMap<String, Object>();
        boletoObj.put("raAluno", aluno.getRa());
        boletoObj.put("linhaDigitavel", linhaDigitavel);
        boletoObj.put("valor", valor);
        boletoObj.put("status", cobranca.getStatus());
        boletoObj.put("vencimento", dataVencimento.toString());
        boletoObj.put("dataGeracao", cobranca.getDataGeracao().toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", "tokenSuperSecreto123"); // A mesma chave secreta usada no controller dos boletos
        HttpEntity<Object> request = new HttpEntity<>(boletoObj, headers);

        try {
            restTemplate.postForEntity(API_BOLETOS_URL, request, String.class);
        } catch (Exception e) {
            System.err.println("Erro ao enviar boleto para API dos boletos: " + e.getMessage());
        }

        return Optional.of(salva);
    }

    public Optional<Cobranca> atualizarStatus(String linhaDigitavel, String novoStatus) {
        Optional<Cobranca> cobrancaOpt = cobrancaRepository.findByLinhaDigitavel(linhaDigitavel);
        if (cobrancaOpt.isPresent()) {
            Cobranca cobranca = cobrancaOpt.get();
            cobranca.setStatus(novoStatus);
            Cobranca salva = cobrancaRepository.save(cobranca);

            try {
                String url = API_BOLETOS_URL + "/status/" + linhaDigitavel + "?status=" + novoStatus;

                HttpHeaders headers = new HttpHeaders();
                headers.set("X-API-KEY", "tokenSuperSecreto123"); // A mesma chave secreta da API dos boletos
                HttpEntity<?> entity = new HttpEntity<>(null, headers);

                restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
            } catch (Exception e) {
                System.err.println("Erro ao atualizar status na API dos boletos: " + e.getMessage());
            }

            return Optional.of(salva);
        }
        return Optional.empty();
    }

    // Gerar linha digitável aleatória (47 dígitos)
    private String gerarLinhaDigitavel() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 47; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

}
