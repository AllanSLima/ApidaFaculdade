package br.com.faculdade.api.dto;

public class LoginRequest {
    private String ra;
    private String senha;

    // Construtores
    public LoginRequest() {
    }

    public LoginRequest(String ra, String senha) {
        this.ra = ra;
        this.senha = senha;
    }

    // Getters e Setters
    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
