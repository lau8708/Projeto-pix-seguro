package services;

import exception.ValorDepositoInvalidoException;
import exception.ValorSaqueInvalidoException;
import exception.ValorInvalidoException;
import model.Conta;
import repository.ContaRepository;

import java.math.BigDecimal;

public class ContaService {

    private ContaRepository contaRepository;
    private AuthService authService;

    public ContaService(ContaRepository contaRepository, AuthService authService) {
        this.contaRepository = contaRepository;
        this.authService = authService;
    }

    public void depositar(BigDecimal valor) {

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorDepositoInvalidoException();
        }

        Conta conta = contaRepository
                .buscarPorCpf(authService.getUsuarioLogado().getCpf());

        conta.setSaldo(conta.getSaldo().add(valor));
    }

    public void sacar(BigDecimal valor) {

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException();
        }

        Conta conta = contaRepository
                .buscarPorCpf(authService.getUsuarioLogado().getCpf());

        if (conta.getSaldoDisponivel().compareTo(valor) < 0) {
            throw new ValorSaqueInvalidoException();
        }

        conta.setSaldo(conta.getSaldo().subtract(valor));
    }

    public Conta buscarContaLogada() {
        return contaRepository
                .buscarPorCpf(authService.getUsuarioLogado().getCpf());
    }
}