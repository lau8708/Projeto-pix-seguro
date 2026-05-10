package services;

import exception.CpfInvalidoException;
import exception.CpfJaCadastradoException;
import exception.SenhaInvalidaException;
import model.Conta;
import model.Usuario;
import model.enums.PerfilUsuario;
import repository.ContaRepository;
import repository.UsuarioRepository;

import java.util.regex.Pattern;

public class UsuarioService {

    private static final Pattern CPF_REGEX = Pattern.compile("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$");
    private static final Pattern SENHA_REGEX = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!*_.])(?=\\S+$).{8,}$");

    private UsuarioRepository usuarioRepository;
    private ContaRepository contaRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, ContaRepository contaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.contaRepository = contaRepository;
    }

    private boolean validarCpf(String cpf){
        if (cpf == null){
            return false;
        }

        return CPF_REGEX.matcher(cpf).matches();
    }

    private boolean validarSenha(String senha){
        if (senha == null){
            return false;
        }

        return SENHA_REGEX.matcher(senha).matches();
    }

    public void cadastrar(String nome, String cpf, String senha){
        if (!validarCpf(cpf)){
            throw new CpfInvalidoException();
        }

        if (!validarSenha(senha)){
            throw new SenhaInvalidaException();
        }

        if (usuarioRepository.existeCpf(cpf)){
            throw new CpfJaCadastradoException();
        }

        PerfilUsuario perfilUsuario;

        if (usuarioRepository.estaVazio()){
            perfilUsuario = PerfilUsuario.ADMIN;
        }
        else {
            perfilUsuario = PerfilUsuario.CLIENTE;
        }

        Usuario usuario = new Usuario(nome, cpf, senha, perfilUsuario);
        Conta conta = new Conta(usuario);

        usuarioRepository.salvar(usuario);
        contaRepository.salvar(conta);
    }
}