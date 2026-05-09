package repository;

import model.Usuario;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UsuarioRepository {

    private Map<String, Usuario> usuarios = new HashMap<>();

    public void salvar(Usuario usuario){
        usuarios.put(usuario.getCpf(), usuario);
    }

    public Usuario buscarPorCpf(String cpf){
        return usuarios.get(cpf);
    }

    public boolean existeCpf(String cpf){
        return usuarios.containsKey(cpf);
    }

    public Collection<Usuario> listarTodos(){
        return usuarios.values();
    }

    public boolean estaVazio(){
        return usuarios.isEmpty();
    }
}