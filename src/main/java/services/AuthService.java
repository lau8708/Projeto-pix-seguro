package services;

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
            throw new RuntimeException("Usuário não encontrado");
        }

        if (!usuario.verificarSenha(senha)){
            throw new RuntimeException("Senha incorreta");
        }

        usuarioLogado = usuario;

    }

    public void logout(){
        usuarioLogado = null;
    }

    public Usuario getUsuarioLogado(){
        if (usuarioLogado == null){
            throw new RuntimeException("Nenhum usuário logado");
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