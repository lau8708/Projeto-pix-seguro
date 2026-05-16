package services;

import exception.AcessoNegadoException;
import exception.SenhaInvalidaException;
import exception.UsuarioNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.ContaRepository;
import repository.UsuarioRepository;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    private AuthService authService;
    private UsuarioService usuarioService;
    private UsuarioRepository usuarioRepository;
    private ContaRepository contaRepository;

    @BeforeEach
    void setUp(){
        usuarioRepository = new UsuarioRepository();
        contaRepository = new ContaRepository();
        authService = new AuthService(usuarioRepository);
        usuarioService = new UsuarioService(usuarioRepository, contaRepository);

        // cadastra um usuário base para os testes de login
        usuarioService.cadastrar("João Silva", "123.456.789-00", "Senha@123");
    }

    // Testes de login

    @Test
    void deveFazerLoginComSucesso(){
        // Act
        authService.login("123.456.789-00", "Senha@123");

        // Assert
        assertTrue(authService.isLogado());
    }

    @Test
    void deveGuardarUsuarioLogadoAposLogin(){
        // Act
        authService.login("123.456.789-00", "Senha@123");

        // Assert
        assertEquals("João Silva", authService.getUsuarioLogado().getNome());
    }

    @Test
    void deveLancarExcecaoQuandoCpfNaoExiste(){
        assertThrows(UsuarioNaoEncontradoException.class, () -> authService.login("000.000.000-00", "Senha@123"));
    }

    @Test
    void deveLancarExcecaoQuandoSenhaErrada(){
        assertThrows(SenhaInvalidaException.class, () -> authService.login("123.456.789-00", "SenhaErrada@123"));
    }

    @Test
    void naoDeveEstarLogadoAntesDoLogin(){
        // Assert - sem nenhum login, isLogado deve ser false
        assertFalse(authService.isLogado());
    }

    // Testes de logout

    @Test
    void deveFazerLogoutComSucesso(){
        // Arrange
        authService.login("123.456.789-00", "Senha@123");

        // Act
        authService.logout();

        // Assert
        assertFalse(authService.isLogado());
    }

    @Test
    void deveLancarExcecaoAoObterUsuarioLogadoSemLogin(){
        assertThrows(AcessoNegadoException.class, () -> authService.getUsuarioLogado());
    }

    // Testes de Admin

    @Test
    void primeiroUsuarioDeveSerAdmin(){
        // Act
        authService.login("123.456.789-00", "Senha@123");

        // Assert
        assertTrue(authService.isAdmin());
    }

    @Test
    void segundoUsuarioNaoDeveSerAdmin(){
        // Arrange
        usuarioService.cadastrar("Maria Costa", "987.654.321-00", "Senha@123");

        // Act
        authService.login("987.654.321-00", "Senha@123");

        // Assert
        assertFalse(authService.isAdmin());
    }

    @Test
    void isAdminDeveRetornarFalseSemLogin(){
        // Assert - sem login, isAdmin deve retornar false sem lançar exceção
        assertFalse(authService.isAdmin());
    }
}