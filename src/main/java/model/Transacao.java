package model;

import model.enums.StatusTransacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transacao {

    private String id;
    private Conta contaOrigem;
    private Conta contaDestino;
    private BigDecimal valor;
    private StatusTransacao statusTransacao;
    private LocalDateTime dataHora;

    public Transacao(Conta contaOrigem, Conta contaDestino, BigDecimal valor) {
        this.id = UUID.randomUUID().toString();
        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
        this.valor = valor;
        this.statusTransacao = StatusTransacao.PENDENTE;
        this.dataHora = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public Conta getContaOrigem() {
        return contaOrigem;
    }

    public Conta getContaDestino() {
        return contaDestino;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public StatusTransacao getStatusTransacao() {
        return statusTransacao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setStatusTransacao(StatusTransacao statusTransacao) {
        this.statusTransacao = statusTransacao;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public boolean isExpirada(){
        return LocalDateTime.now().isAfter(dataHora.plusHours(24));
    }
}