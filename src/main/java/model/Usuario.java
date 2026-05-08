package model;

import model.enums.PerfilUsuario;

public class Usuario {

    private String nome;
    private String cpf;
    private String senha;
    private PerfilUsuario perfilUsuario;

    public Usuario(String nome, String cpf, String senha, PerfilUsuario perfilUsuario) {
        this.nome = nome;
        this.cpf = cpf;
        this.senha = senha;
        this.perfilUsuario = perfilUsuario;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public PerfilUsuario getPerfilUsuario() {
        return perfilUsuario;
    }

    public boolean verificarSenha(String senhaDigitada){
        return senha.equals(senhaDigitada);
    }
}