package services;

import exception.CpfInvalidoException;
import exception.CpfJaCadastradoException;
import exception.SenhaInvalidaException;
import model.enums.PerfilUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.ContaRepository;
import repository.UsuarioRepository;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioServiceTest {

    private UsuarioService usuarioService;
    private UsuarioRepository usuarioRepository;
    private ContaRepository contaRepository;

    @BeforeEach
    void setUp(){
        usuarioRepository = new UsuarioRepository();
        contaRepository = new ContaRepository();
        usuarioService = new UsuarioService(usuarioRepository, contaRepository);
    }

    // Testes de cadastro

    @Test
    void deveCadastrarUsuarioComSucesso(){
        // Arrange
        String nome = "João Silva";
        String cpf = "123.456.789-00";
        String senha = "Senha@123";

        // Act
        usuarioService.cadastrar(nome, cpf, senha);

        // Assert
        assertTrue(usuarioRepository.existeCpf(cpf));
    }

    @Test
    void primeiroUsuarioCadastradoDeveSerAdmin(){
        // Arrange e Act
        usuarioService.cadastrar("João Silva", "123.456.789-00", "Senha@123");

        // Assert
        assertEquals(PerfilUsuario.ADMIN, usuarioRepository.buscarPorCpf("123.456.789-00").getPerfilUsuario());
    }

    @Test
    void demaisUsuariosCadastradosDevemSerClientes(){
        // Arrange
        usuarioService.cadastrar("João Silva", "123.456.789-00", "Senha@123");

        // Act
        usuarioService.cadastrar("Maria Costa", "987.654.321-00", "Senha@123");

        // Assert
        assertEquals(PerfilUsuario.CLIENTE, usuarioRepository.buscarPorCpf("987.654.321-00").getPerfilUsuario());
    }

    @Test
    void deveCriarContaAoCadastrarUsuario(){
        // Arrange e Act
        usuarioService.cadastrar("João Silva", "123.456.789-00", "Senha@123");

        // Assert
        assertNotNull(contaRepository.buscarPorCpf("123.456.789-00"));
    }

    // Testes de validação

    @Test
    void deveLancarExcecaoQuandoCpfInvalido(){
        assertThrows(CpfInvalidoException.class, () -> usuarioService.cadastrar("João Silva", "cpf-invalido", "Senha@123"));
    }

    @Test
    void deveLancarExcecaoQuandoCpfNulo(){
        assertThrows(CpfInvalidoException.class, () -> usuarioService.cadastrar("João Silva", null, "Senha@123"));
    }

    @Test
    void deveLancarExcecaoQuandoSenhaInvalida(){
        assertThrows(SenhaInvalidaException.class, () -> usuarioService.cadastrar("João Silva", "123.456.789-00", "senhasimples"));
    }

    @Test
    void deveLancarExcecaoQuandoSenhaNula(){
        assertThrows(SenhaInvalidaException.class, () -> usuarioService.cadastrar("João Silva", "123.456.789-00", null));
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaCadastrado(){
        // Arrange
        usuarioService.cadastrar("João Silva", "123.456.789-00", "Senha@123");

        // Act e Assert
        assertThrows(CpfJaCadastradoException.class, () -> usuarioService.cadastrar("Outro Nome", "123.456.789-00", "Senha@123"));
    }
}