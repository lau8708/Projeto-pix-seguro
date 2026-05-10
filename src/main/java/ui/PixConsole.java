package ui;

import exception.PixException;
import model.Transacao;
import model.enums.StatusTransacao;
import services.PixService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class PixConsole {

    private Scanner scanner;
    private PixService pixService;

    public PixConsole(Scanner scanner, PixService pixService) {
        this.scanner = scanner;
        this.pixService = pixService;
    }

    public void enviarPix() {
        System.out.println("\n=== ENVIAR PIX ===");

        System.out.print("CPF do destinatário: ");
        String cpfDestino = scanner.nextLine().trim();

        System.out.print("Valor (ex: 100.00): ");
        String valorStr = scanner.nextLine().trim();

        try {
            BigDecimal valor = new BigDecimal(valorStr);
            pixService.enviarPix(cpfDestino, valor);
            System.out.println("PIX enviado! Aguardando confirmação do destinatário.");
        } catch (PixException e) {
            System.out.println("Erro ao enviar PIX: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Use o formato 100.00");
        }
    }

    public void exibirPendentes() {
        System.out.println("\n=== PIX PENDENTES ===");

        List<Transacao> pendentes = pixService.listarPendentes();

        if (pendentes.isEmpty()) {
            System.out.println("Nenhum PIX pendente.");
            return;
        }

        for (int i = 0; i < pendentes.size(); i++) {
            Transacao t = pendentes.get(i);
            System.out.println("\n[" + (i + 1) + "]");
            System.out.println("ID: " + t.getId());
            System.out.println("De: " + t.getContaOrigem().getUsuario().getNome()
                    + " (" + t.getContaOrigem().getUsuario().getCpf() + ")");
            System.out.println("Valor: R$ " + t.getValor());
            System.out.println("Data: " + t.getDataHora());
        }

        System.out.println("\n1. Aceitar um PIX");
        System.out.println("2. Rejeitar um PIX");
        System.out.println("0. Voltar");
        System.out.print("Escolha: ");

        String opcao = scanner.nextLine().trim();

        switch (opcao) {
            case "1" -> aceitarPix();
            case "2" -> rejeitarPix();
            case "0" -> {}
            default -> System.out.println("Opção inválida.");
        }
    }

    private void aceitarPix() {
        System.out.print("Digite o ID da transação: ");
        String id = scanner.nextLine().trim();

        try {
            pixService.aceitarPix(id);
            System.out.println("PIX aceito! Saldo atualizado.");
        } catch (PixException e) {
            System.out.println("Erro ao aceitar PIX: " + e.getMessage());
        }
    }

    private void rejeitarPix() {
        System.out.print("Digite o ID da transação: ");
        String id = scanner.nextLine().trim();

        try {
            pixService.rejeitarPix(id);
            System.out.println("PIX rejeitado. O valor foi devolvido ao remetente.");
        } catch (PixException e) {
            System.out.println("Erro ao rejeitar PIX: " + e.getMessage());
        }
    }

    public void exibirHistorico() {
        System.out.println("\n=== HISTÓRICO DE TRANSAÇÕES ===");

        List<Transacao> historico = pixService.listarHistorico();

        if (historico.isEmpty()) {
            System.out.println("Nenhuma transação encontrada.");
            return;
        }

        for (Transacao t : historico) {
            System.out.println("\n----------------------------");
            System.out.println("ID: " + t.getId());
            System.out.println("De: " + t.getContaOrigem().getUsuario().getNome());
            System.out.println("Para: " + t.getContaDestino().getUsuario().getNome());
            System.out.println("Valor: R$ " + t.getValor());
            System.out.println("Status: " + traduzirStatus(t.getStatusTransacao()));
            System.out.println("Data: " + t.getDataHora());
        }
    }

    private String traduzirStatus(StatusTransacao status) {
        return switch (status) {
            case PENDENTE -> "Pendente";
            case ACEITA -> "Aceita";
            case REJEITADA -> "Rejeitada";
        };
    }
}