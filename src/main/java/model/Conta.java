package model;

import java.math.BigDecimal;
import java.util.UUID;

public class Conta {

    private String id;
    private BigDecimal saldo;
    private BigDecimal saldoReservado;
    private Usuario usuario;

    public Conta(Usuario usuario) {
        this.id = UUID.randomUUID().toString();
        this.saldo = BigDecimal.ZERO;
        this.saldoReservado = BigDecimal.ZERO;
        this.usuario = usuario;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getSaldoReservado() {
        return saldoReservado;
    }

    public void setSaldoReservado(BigDecimal saldoReservado) {
        this.saldoReservado = saldoReservado;
    }

    public BigDecimal getSaldoDisponivel(){
        return saldo.subtract(saldoReservado);
    }

    public Usuario getUsuario() {
        return usuario;
    }
}