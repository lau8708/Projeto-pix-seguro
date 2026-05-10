package services;

import exception.AcessoNegadoException;
import model.Conta;
import model.Transacao;
import repository.ContaRepository;
import repository.TransacaoRepository;

import java.util.Collection;

public class AdminService {

    private ContaRepository contaRepository;
    private TransacaoRepository transacaoRepository;
    private AuthService authService;

    public AdminService(ContaRepository contaRepository,
                        TransacaoRepository transacaoRepository,
                        AuthService authService) {
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
        this.authService = authService;
    }

    public Collection<Conta> listarTodasContas() {
        if (!authService.isAdmin()) {
            throw new AcessoNegadoException();
        }
        return contaRepository.listarTodas();
    }

    public Collection<Transacao> listarTodasTransacoes() {
        if (!authService.isAdmin()) {
            throw new AcessoNegadoException();
        }
        return transacaoRepository.listarTodas();
    }
}