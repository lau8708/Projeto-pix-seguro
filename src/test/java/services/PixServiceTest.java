package services;

import exception.*;
import model.Conta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.ContaRepository;
import repository.TransacaoRepository;
import repository.UsuarioRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PixServiceTest {

    private PixService pixService;
    private AuthService authService;
    private UsuarioService usuarioService;
    private ContaService contaService;
    private UsuarioRepository usuarioRepository;
    private ContaRepository contaRepository;
    private TransacaoRepository transacaoRepository;

    @BeforeEach
    void setUp(){
        usuarioRepository = new UsuarioRepository();
        contaRepository = new ContaRepository();
        transacaoRepository = new TransacaoRepository();

        authService = new AuthService(usuarioRepository);
        usuarioService = new UsuarioService(usuarioRepository, contaRepository);
        pixService = new PixService(contaRepository, transacaoRepository, authService);
        contaService = new ContaService(contaRepository, authService);

        // cadastra dois usuários para os testes
        usuarioService.cadastrar("João Silva", "123.456.789-00", "Senha@123");
        usuarioService.cadastrar("Maria Costa", "987.654.321-00", "Senha@123");

        // faz login como João e deposita saldo
        authService.login("123.456.789-00", "Senha@123");
        contaService.depositar(new BigDecimal("500.00"));
        authService.logout();
    }

    // Testes de envio de PIX

    @Test
    void deveEnviarPixComSucesso(){
        // Arrange
        authService.login("123.456.789-00", "Senha@123");

        // Act
        pixService.enviarPix("987.654.321-00", new BigDecimal("100.00"));

        // Assert - saldo reservado deve aumentar
        Conta contaJoao = contaRepository.buscarPorCpf("123.456.789-00");
        assertEquals(0, contaJoao.getSaldoReservado().compareTo(new BigDecimal("100.00")));
    }

    @Test
    void pixEnviadoDeveGerarTransacaoPendente(){
        // Arrange
        authService.login("123.456.789-00", "Senha@123");

        // Act
        pixService.enviarPix("987.654.321-00", new BigDecimal("100.00"));

        // Assert - Maria deve ter um PIX pendente
        authService.logout();
        authService.login("987.654.321-00", "Senha@123");
        assertFalse(pixService.listarPendentes().isEmpty());
    }

    @Test
    void deveLancarExcecaoQuandoSaldoInsuficiente(){
        // Arrange
        authService.login("123.456.789-00", "Senha@123");

        // Act e Assert
        assertThrows(SaldoInsuficienteException.class, () -> pixService.enviarPix("987.654.321-00", new BigDecimal("1000.00")));
    }

    @Test
    void deveLancarExcecaoQuandoDestinatarioNaoExiste(){
        // Arrange
        authService.login("123.456.789-00", "Senha@123");

        // Act e Assert
        assertThrows(ContaNaoEncontradaException.class, () -> pixService.enviarPix("000.000.000-00", new BigDecimal("100.00")));
    }

    @Test
    void deveLancarExcecaoQuandoValorNegativo(){
        // Arrange
        authService.login("123.456.789-00", "Senha@123");

        // Act e Assert
        assertThrows(ValorInvalidoException.class, () -> pixService.enviarPix("987.654.321-00", new BigDecimal("-50.00")));
    }

    @Test
    void deveLancarExcecaoQuandoEnviarPixParaSiMesmo(){
        // Arrange
        authService.login("123.456.789-00", "Senha@123");

        // Act e Assert
        assertThrows(AutoTransferenciaException.class, () -> pixService.enviarPix("123.456.789-00", new BigDecimal("100.00")));
    }

    // Testes de aceitar PIX
    @Test
    void deveAceitarPixComSucesso(){
        // Arrange - João envia PIX para Maria
        authService.login("123.456.789-00", "Senha@123");
        pixService.enviarPix("987.654.321-00", new BigDecimal("100.00"));
        authService.logout();

        // Maria faz login e aceita
        authService.login("987.654.321-00", "Senha@123");
        String idTransacao = pixService.listarPendentes().get(0).getId();

        // Act
        pixService.aceitarPix(idTransacao);

        // Assert - Maria deve ter recebido o valor
        Conta contaMaria = contaRepository.buscarPorCpf("987.654.321-00");
        assertEquals(0, contaMaria.getSaldo().compareTo(new BigDecimal("100.00")));
    }

    @Test
    void aoAceitarPixSaldoRemetenteDeveSerDebitado(){
        // Arrange
        authService.login("123.456.789-00", "Senha@123");
        pixService.enviarPix("987.654.321-00", new BigDecimal("100.00"));
        authService.logout();

        authService.login("987.654.321-00", "Senha@123");
        String idTransacao = pixService.listarPendentes().get(0).getId();

        // Act - Maria aceita PIX de João
        pixService.aceitarPix(idTransacao);

        // Assert - João deve ter perdido o valor
        Conta contaJoao = contaRepository.buscarPorCpf("123.456.789-00");
        assertEquals(0, contaJoao.getSaldo().compareTo(new BigDecimal("400.00")));
    }

    @Test
    void deveLancarExcecaoQuandoNaoEhDestinatario() {
        // Arrange — João envia PIX para Maria
        authService.login("123.456.789-00", "Senha@123");
        pixService.enviarPix("987.654.321-00", new BigDecimal("100.00"));

        // João tenta aceitar o próprio PIX que enviou
        String idTransacao = transacaoRepository.listarTodas().iterator().next().getId();

        // Act & Assert
        assertThrows(DestinatarioInvalidoException.class, () -> pixService.aceitarPix(idTransacao));
    }

    @Test
    void deveLancarExcecaoAoAceitarTransacaoInexistente(){
        // Arrange
        authService.login("123.456.789-00", "Senha@123");

        // Act e Assert
        assertThrows(TransacaoNaoEncontradaException.class, () -> pixService.aceitarPix("id-inexistente"));
    }

    // Testes de rejeitar PIX

    @Test
    void deveRejeitarPixComSucesso(){
        // Arrange
        authService.login("123.456.789-00", "Senha@123");
        pixService.enviarPix("987.654.321-00", new BigDecimal("100.00"));
        authService.logout();

        authService.login("987.654.321-00", "Senha@123");
        String idTransacao = pixService.listarPendentes().get(0).getId();

        // Act - Maria rejeita PIX de João
        pixService.rejeitarPix(idTransacao);

        // Assert
        Conta contaJoao = contaRepository.buscarPorCpf("123.456.789-00");
        assertEquals(0, contaJoao.getSaldo().compareTo(new BigDecimal("500.00")));
    }

    @Test
    void aoRejeitarPixReservaDeveSerLiberada(){
        // Arrange
        authService.login("123.456.789-00", "Senha@123");
        pixService.enviarPix("987.654.321-00", new BigDecimal("100.00"));
        authService.logout();

        authService.login("987.654.321-00", "Senha@123");
        String idTransacao = pixService.listarPendentes().get(0).getId();

        // Act
        pixService.rejeitarPix(idTransacao);

        // Assert
        Conta contaJoao = contaRepository.buscarPorCpf("123.456.789-00");
        assertEquals(0, contaJoao.getSaldoReservado().compareTo(BigDecimal.ZERO));
    }

    @Test
    void deveLancarExcecaoAoRejeitarTransacaoInexistente(){
        // Arrange
        authService.login("123.456.789-00", "Senha@123");

        // Act e Assert
        assertThrows(TransacaoNaoEncontradaException.class, () -> pixService.rejeitarPix("id-inexistente"));
    }

    // TESTE DO GOLPE BLOQUEADO //

    @Test
    void golpistaNaoDeveReceberDinheiroSeVitimaRejeitar(){
        // Arrange - golpista envia dinheiro para a vítima
        authService.login("123.456.789-00", "Senha@123");
        pixService.enviarPix("987.654.321-00", new BigDecimal("100.00"));
        authService.logout();

        // Vítima ver PIX desconhecido e rejeita
        authService.login("987.654.321-00", "Senha@123");
        String idTransacao = pixService.listarPendentes().get(0).getId();
        pixService.rejeitarPix(idTransacao);
        authService.logout();

        // Assert - vítima não perdeu nada
        Conta contaVitima = contaRepository.buscarPorCpf("987.654.321-00");
        assertEquals(0, contaVitima.getSaldo().compareTo(BigDecimal.ZERO));

        // Assert - golpista não recebeu nada
        Conta contaGolpista = contaRepository.buscarPorCpf("123.456.789-00");
        assertEquals(0, contaGolpista.getSaldo().compareTo(new BigDecimal("500.00")));
        assertEquals(0, contaGolpista.getSaldoReservado().compareTo(BigDecimal.ZERO));
    }
}