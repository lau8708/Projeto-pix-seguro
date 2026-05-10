package ui;

import exception.PixException;
import model.Conta;
import model.Transacao;
import services.AdminService;

import java.util.Collection;

public class AdminConsole {

    private AdminService adminService;

    public AdminConsole(AdminService adminService) {
        this.adminService = adminService;
    }

    public void listarContas() {
        System.out.println("\n=== TODAS AS CONTAS ===");

        try {
            Collection<Conta> contas = adminService.listarTodasContas();

            if (contas.isEmpty()) {
                System.out.println("Nenhuma conta cadastrada.");
                return;
            }

            for (Conta c : contas) {
                System.out.println("\n----------------------------");
                System.out.println("Titular: " + c.getUsuario().getNome());
                System.out.println("CPF: " + c.getUsuario().getCpf());
                System.out.println("Perfil: " + c.getUsuario().getPerfilUsuario());
                System.out.println("Saldo: R$ " + c.getSaldo());
                System.out.println("Saldo Reservado: R$ " + c.getSaldoReservado());
                System.out.println("Saldo Disponível: R$ " + c.getSaldoDisponivel());
            }
        } catch (PixException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void listarTransacoes() {
        System.out.println("\n=== TODAS AS TRANSAÇÕES ===");

        try {
            Collection<Transacao> transacoes = adminService.listarTodasTransacoes();

            if (transacoes.isEmpty()) {
                System.out.println("Nenhuma transação encontrada.");
                return;
            }

            for (Transacao t : transacoes) {
                System.out.println("\n----------------------------");
                System.out.println("ID: " + t.getId());
                System.out.println("De: " + t.getContaOrigem().getUsuario().getNome()
                        + " (" + t.getContaOrigem().getUsuario().getCpf() + ")");
                System.out.println("Para: " + t.getContaDestino().getUsuario().getNome()
                        + " (" + t.getContaDestino().getUsuario().getCpf() + ")");
                System.out.println("Valor: R$ " + t.getValor());
                System.out.println("Status: " + t.getStatusTransacao());
                System.out.println("Data: " + t.getDataHora());
            }
        } catch (PixException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}