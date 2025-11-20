package br.com.faculdade.api.dto;

public class LoginResponse {
    private String ra;
    private String nome;
    private String email;
    private String token;
    private String mensagem;

    // Construtores
    public LoginResponse() {}

    public LoginResponse(String ra, String nome, String email, String token, String mensagem) {
        this.ra = ra;
        this.nome = nome;
        this.email = email;
        this.token = token;
        this.mensagem = mensagem;
    }

    // Getters e Setters
    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
