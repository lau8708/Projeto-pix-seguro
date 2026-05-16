package ui;

import exception.PixException;
import model.Conta;
import services.ContaService;

import java.math.BigDecimal;
import java.util.Scanner;

public class ContaConsole {

    private Scanner scanner;
    private ContaService contaService;

    public ContaConsole(Scanner scanner, ContaService contaService) {
        this.scanner = scanner;
        this.contaService = contaService;
    }

    public void depositar() {
        System.out.println("\n=== DEPÓSITO ===");

        System.out.print("Valor do depósito (ex: 100.00): ");
        String valorStr = scanner.nextLine().trim();

        try {
            BigDecimal valor = new BigDecimal(valorStr);
            contaService.depositar(valor);
            Conta conta = contaService.buscarContaLogada();
            System.out.println("Depósito realizado com sucesso!");
            System.out.println("Saldo atual: R$ " + conta.getSaldo());
        } catch (PixException e) {
            System.out.println("Erro ao depositar: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Use o formato 100.00");
        }
    }

    public void sacar() {
        System.out.println("\n=== SAQUE ===");

        Conta conta = contaService.buscarContaLogada();
        System.out.println("Saldo disponível: R$ " + conta.getSaldoDisponivel());

        System.out.print("Valor do saque (ex: 100.00): ");
        String valorStr = scanner.nextLine().trim();

        try {
            BigDecimal valor = new BigDecimal(valorStr);
            contaService.sacar(valor);
            conta = contaService.buscarContaLogada();
            System.out.println("Saque realizado com sucesso!");
            System.out.println("Saldo atual: R$ " + conta.getSaldo());
        } catch (PixException e) {
            System.out.println("Erro ao sacar: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Use o formato 100.00");
        }
    }

    public void exibirSaldo(){
        Conta conta = contaService.buscarContaLogada();
        System.out.println("\n=== SALDO ===");
        System.out.println("Saldo total:      R$ " + conta.getSaldo());
        System.out.println("Saldo reservado:  R$ " + conta.getSaldoReservado());
        System.out.println("Saldo disponível: R$ " + conta.getSaldoDisponivel());
    }
}