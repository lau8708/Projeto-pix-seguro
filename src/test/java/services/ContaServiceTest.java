package services;

import exception.ValorDepositoInvalidoException;
import exception.ValorInvalidoException;
import exception.ValorSaqueInvalidoException;
import model.Conta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.ContaRepository;
import repository.UsuarioRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ContaServiceTest {

    private ContaService contaService;
    private AuthService authService;
    private UsuarioService usuarioService;
    private ContaRepository contaRepository;
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp(){
        usuarioRepository = new UsuarioRepository();
        contaRepository = new ContaRepository();

        authService = new AuthService(usuarioRepository);
        usuarioService = new UsuarioService(usuarioRepository, contaRepository);
        contaService = new ContaService(contaRepository, authService);

        // cadastra e loga um usuário base
        usuarioService.cadastrar("João Silva", "123.456.789-00", "Senha@123");
        authService.login("123.456.789-00", "Senha@123");
    }

    // Testes de depósito

    @Test
    void deveDepositarComSucesso(){
        // Act
        contaService.depositar(new BigDecimal("300.00"));

        // Assert
        Conta conta = contaService.buscarContaLogada();
        assertEquals(0, conta.getSaldo().compareTo(new BigDecimal("300.00")));
    }

    @Test
    void deveSomarDepositosAoSaldoExistente(){
        // Arrange
        contaService.depositar(new BigDecimal("300.00"));

        // Act
        contaService.depositar(new BigDecimal("200.00"));

        // Assert
        Conta conta = contaService.buscarContaLogada();
        assertEquals(0, conta.getSaldo().compareTo(new BigDecimal("500.00")));
    }

    @Test
    void deveLancarExcecaoQuandoDepositoZero(){
        assertThrows(ValorDepositoInvalidoException.class, () -> contaService.depositar(BigDecimal.ZERO));
    }

    @Test
    void deveLancarExcecaoQuandoDepositoNegativo(){
        assertThrows(ValorDepositoInvalidoException.class, () -> contaService.depositar(new BigDecimal("-100.00")));
    }

    // Testes de saque

    @Test
    void deveSacarComSucesso(){
        // Arrange
        contaService.depositar(new BigDecimal("500.00"));

        // Act
        contaService.sacar(new BigDecimal("200.00"));

        // Assert
        Conta conta = contaService.buscarContaLogada();
        assertEquals(0, conta.getSaldo().compareTo(new BigDecimal("300.00")));
    }

    @Test
    void deveLancarExcecaoQuandoSaldoInsuficienteParaSaque(){
        // Arrange
        contaService.depositar(new BigDecimal("100.00"));

        // Act e Assert
        assertThrows(ValorSaqueInvalidoException.class, () -> contaService.sacar(new BigDecimal("200.00")));
    }

    @Test
    void deveLancarExcecaoQuandoSaqueZero(){
        assertThrows(ValorSaqueInvalidoException.class, () -> contaService.sacar(BigDecimal.ZERO));
    }

    @Test
    void deveLancarExcecaoQuandoSaqueNegativo(){
        assertThrows(ValorSaqueInvalidoException.class, () -> contaService.sacar(new BigDecimal("-100.00")));
    }

    @Test
    void saldoNaoDeveSerNegativoAposSaque(){
        // Arrange
        contaService.depositar(new BigDecimal("100.00"));

        // Act e Assert
        assertThrows(ValorSaqueInvalidoException.class, () -> contaService.sacar(new BigDecimal("101.00")));

        // saldo continua intacto após tentativa inválida
        Conta conta = contaService.buscarContaLogada();
        assertEquals(0, conta.getSaldo().compareTo(new BigDecimal("100.00")));
    }

    // Testes de saldo

    @Test
    void saldoInicialDeveSerZero(){
        Conta conta = contaService.buscarContaLogada();
        assertEquals(0, conta.getSaldo().compareTo(BigDecimal.ZERO));
    }

    @Test
    void buscarContaLogadaDeveBuscarContaCorreta(){
        Conta conta = contaService.buscarContaLogada();
        assertEquals("João Silva", conta.getUsuario().getNome());
    }
}