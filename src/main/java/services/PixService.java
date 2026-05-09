package services;

import model.Conta;
import model.Transacao;
import model.enums.StatusTransacao;
import repository.ContaRepository;
import repository.TransacaoRepository;

import java.math.BigDecimal;
import java.util.List;

public class PixService {

    private ContaRepository contaRepository;
    private TransacaoRepository transacaoRepository;
    private AuthService authService;

    public PixService(ContaRepository contaRepository,
                      TransacaoRepository transacaoRepository,
                      AuthService authService) {
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
        this.authService = authService;
    }

    public void enviarPix(String cpfDestino, BigDecimal valor) {

        // busca a conta de quem está logado como remetente
        Conta contaOrigem = contaRepository
                .buscarPorCpf(authService.getUsuarioLogado().getCpf());

        // verifica se o destinatario existe
        Conta contaDestino = contaRepository.buscarPorCpf(cpfDestino);
        if (contaDestino == null) {
            throw new RuntimeException("Conta destinatária não encontrada.");
        }

        // impede pix para si mesmo
        if (contaOrigem.getId().equals(contaDestino.getId())) {
            throw new RuntimeException("Não é possível enviar PIX para si mesmo.");
        }

        // verifica se o valor é maior que zero
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O valor do PIX deve ser maior que zero.");
        }

        // verifica se tem saldo disponivel suficiente
        if (contaOrigem.getSaldoDisponivel().compareTo(valor) < 0) {
            throw new RuntimeException("Saldo insuficiente.");
        }

        // reserva o saldo do remetente
        contaOrigem.setSaldoReservado(
                contaOrigem.getSaldoReservado().add(valor)
        );

        // cria a transacao com status PENDENTE
        Transacao transacao = new Transacao(contaOrigem, contaDestino, valor);
        transacaoRepository.salvar(transacao);
    }

    public void aceitarPix(String idTransacao) {

        // busca a transacao pelo id
        Transacao transacao = transacaoRepository.buscarPorId(idTransacao);
        if (transacao == null) {
            throw new RuntimeException("Transação não encontrada.");
        }

        // garante que só o destinatário pode aceitar
        Conta contaLogada = contaRepository
                .buscarPorCpf(authService.getUsuarioLogado().getCpf());
        if (!transacao.getContaDestino().getId().equals(contaLogada.getId())) {
            throw new RuntimeException("Você não é o destinatário desta transação.");
        }

        // impede aceitar pix expirado
        if (transacao.isExpirada()) {
            transacao.setStatusTransacao(StatusTransacao.REJEITADA);
            transacaoRepository.salvar(transacao);
            throw new RuntimeException("PIX expirado e cancelado automaticamente.");
        }

        // impede aceitar pix que nao esta pendente
        if (transacao.getStatusTransacao() != StatusTransacao.PENDENTE) {
            throw new RuntimeException("Esta transação não está mais pendente.");
        }

        BigDecimal valor = transacao.getValor();
        Conta contaOrigem = transacao.getContaOrigem();
        Conta contaDestino = transacao.getContaDestino();

        // debita o saldo e libera a reserva do remetente
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valor));
        contaOrigem.setSaldoReservado(contaOrigem.getSaldoReservado().subtract(valor));

        // credita o saldo do destinatario
        contaDestino.setSaldo(contaDestino.getSaldo().add(valor));

        // atualiza o status da transacao
        transacao.setStatusTransacao(StatusTransacao.ACEITA);
        transacaoRepository.salvar(transacao);
    }

    public void rejeitarPix(String idTransacao) {

        // busca a transacao pelo id
        Transacao transacao = transacaoRepository.buscarPorId(idTransacao);
        if (transacao == null) {
            throw new RuntimeException("Transação não encontrada.");
        }

        // garante que só o destinatário pode rejeitar
        Conta contaLogada = contaRepository
                .buscarPorCpf(authService.getUsuarioLogado().getCpf());
        if (!transacao.getContaDestino().getId().equals(contaLogada.getId())) {
            throw new RuntimeException("Você não é o destinatário desta transação.");
        }

        // impede rejeitar pix que nao esta pendente
        if (transacao.getStatusTransacao() != StatusTransacao.PENDENTE) {
            throw new RuntimeException("Esta transação não está mais pendente.");
        }

        // libera a reserva do remetente devolvendo o valor
        Conta contaOrigem = transacao.getContaOrigem();
        contaOrigem.setSaldoReservado(
                contaOrigem.getSaldoReservado().subtract(transacao.getValor())
        );

        // atualiza o status da transacao
        transacao.setStatusTransacao(StatusTransacao.REJEITADA);
        transacaoRepository.salvar(transacao);
    }

    public List<Transacao> listarPendentes() {

        // busca a conta do usuario logado
        Conta contaLogada = contaRepository
                .buscarPorCpf(authService.getUsuarioLogado().getCpf());

        // retorna os pix pendentes onde o logado é o destinatario
        return transacaoRepository.listarPendentesDestino(contaLogada);
    }

    public List<Transacao> listarHistorico() {

        // busca a conta do usuario logado
        Conta contaLogada = contaRepository
                .buscarPorCpf(authService.getUsuarioLogado().getCpf());

        // retorna todas as transacoes que envolvem a conta do logado
        return transacaoRepository.listarPorConta(contaLogada);
    }
}