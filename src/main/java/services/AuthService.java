package services;

import exception.AcessoNegadoException;
import exception.SenhaInvalidaException;
import exception.UsuarioNaoEncontradoException;
import model.Usuario;
import model.enums.PerfilUsuario;
import repository.UsuarioRepository;

public class AuthService {

    private UsuarioRepository usuarioRepository;
    private Usuario usuarioLogado;

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void login(String cpf, String senha){
        Usuario usuario = usuarioRepository.buscarPorCpf(cpf);

        if (usuario == null){
            throw new UsuarioNaoEncontradoException();
        }

        if (!usuario.verificarSenha(senha)){
            throw new SenhaInvalidaException();
        }

        usuarioLogado = usuario;

    }

    public void logout(){
        usuarioLogado = null;
    }

    public Usuario getUsuarioLogado(){
        if (usuarioLogado == null){
            throw new AcessoNegadoException();
        }
        return usuarioLogado;
    }

    public boolean isLogado(){
        return usuarioLogado != null;
    }

    public boolean isAdmin(){
        if (!isLogado()){
            return false;
        }
        return usuarioLogado.getPerfilUsuario() == PerfilUsuario.ADMIN;
    }
}