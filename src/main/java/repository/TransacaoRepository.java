package repository;

import model.Conta;
import model.Transacao;
import model.enums.StatusTransacao;

import java.util.*;

public class TransacaoRepository {

    private Map<String, Transacao> transacoes = new HashMap<>();

    public void salvar(Transacao transacao){
        transacoes.put(transacao.getId(), transacao);
    }

    public Transacao buscarPorId(String id){
        return transacoes.get(id);
    }

    public List<Transacao> listarPendentesDestino(Conta contaDestino){
        List<Transacao> pendentes = new ArrayList<>();

        for (Transacao transacao : transacoes.values()){
            if (transacao.getContaDestino().getId().equals(contaDestino.getId())
            && transacao.getStatusTransacao() == StatusTransacao.PENDENTE
            && !transacao.isExpirada()){
                pendentes.add(transacao);
            }
        }
        return pendentes;
    }

    public List<Transacao> listarPorConta(Conta conta){
        List<Transacao> historico = new ArrayList<>();

        for (Transacao transacao : transacoes.values()){
            if (transacao.getContaOrigem().getId().equals(conta.getId())
            || transacao.getContaDestino().getId().equals(conta.getId())){
                historico.add(transacao);
            }
        }
        return historico;
    }

    public Collection<Transacao> listarTodas(){
        return transacoes.values();
    }
}