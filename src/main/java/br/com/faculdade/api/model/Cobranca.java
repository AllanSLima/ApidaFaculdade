package br.com.faculdade.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cobrancas")
public class Cobranca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ra_aluno", referencedColumnName = "ra", nullable = false)
    private Aluno aluno;

    @Column(name = "linha_digitavel", nullable = false, unique = true, length = 100)
    private String linhaDigitavel;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(name = "data_geracao", nullable = false)
    private LocalDateTime dataGeracao;

    // Construtores
    public Cobranca() {
        this.dataGeracao = LocalDateTime.now();
        this.status = "EM_ABERTO";
    }

    public Cobranca(Aluno aluno, String linhaDigitavel, BigDecimal valor, LocalDate dataVencimento) {
        this.aluno = aluno;
        this.linhaDigitavel = linhaDigitavel;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.dataGeracao = LocalDateTime.now();
        this.status = "EM_ABERTO";
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public String getLinhaDigitavel() {
        return linhaDigitavel;
    }

    public void setLinhaDigitavel(String linhaDigitavel) {
        this.linhaDigitavel = linhaDigitavel;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(LocalDateTime dataGeracao) {
        this.dataGeracao = dataGeracao;
    }
}
