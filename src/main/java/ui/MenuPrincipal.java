package ui;

import model.Usuario;
import services.*;

import java.util.Scanner;

public class MenuPrincipal {

    private Scanner scanner;
    private AuthService authService;
    private UsuarioService usuarioService;
    private PixService pixService;
    private AdminService adminService;

    private AuthConsole authConsole;
    private PixConsole pixConsole;
    private AdminConsole adminConsole;
    private ContaConsole contaConsole;

    public MenuPrincipal(AuthService authService, UsuarioService usuarioService,
                         PixService pixService, AdminService adminService, ContaService contaService) {
        this.scanner = new Scanner(System.in);
        this.authService = authService;
        this.usuarioService = usuarioService;
        this.pixService = pixService;
        this.adminService = adminService;

        this.authConsole = new AuthConsole(scanner, authService, usuarioService);
        this.pixConsole = new PixConsole(scanner, pixService);
        this.adminConsole = new AdminConsole(adminService);
        this.contaConsole = new ContaConsole(scanner, contaService);
    }

    public void iniciar(){
        System.out.println("=== Sistema Anti-Golpe PIX ===");

        while (true){
            if (!authService.isLogado()){
                exibirMenuInicial();
            } else if (authService.isAdmin()) {
                exibirMenuAdmin();
            }else {
                exibirMenuCliente();
            }
        }
    }

    private void exibirMenuInicial(){
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1- Cadastrar");
        System.out.println("2- Login");
        System.out.println("0- Sair");
        System.out.print("Escolha: ");

        String opcao = scanner.nextLine().trim();

        switch (opcao){
            case "1" -> authConsole.cadastrar();
            case "2" -> authConsole.login();
            case "0" -> {
                System.out.println("Encerrando o sitema.");
                System.exit(0);
            }
            default -> System.out.println("Opção inválida. Tente novamente");
        }
    }

    private void exibirMenuCliente(){
        String nome = authService.getUsuarioLogado().getNome();
        System.out.println("\n=== Bem Vindo, " + nome.toUpperCase() + "===");
        System.out.println("1. Depositar");
        System.out.println("2. Sacar");
        System.out.println("3- Enviar PIX");
        System.out.println("4- PIX pendentes");
        System.out.println("5- Histórico");
        System.out.println("0- Logout");
        System.out.print("Escolha: ");

        String opcao = scanner.nextLine().trim();

        switch (opcao){
            case "1" -> contaConsole.depositar();
            case "2" -> contaConsole.sacar();
            case "3" -> pixConsole.enviarPix();
            case "4" -> pixConsole.exibirPendentes();
            case "5" -> pixConsole.exibirHistorico();
            case "0" -> {
                authService.logout();
                System.out.println("Logout realizado com sucesso");
            }
            default -> System.out.println("Opção inválida. Tente novamente");
        }
    }

    private void exibirMenuAdmin(){
        String nome = authService.getUsuarioLogado().getNome();
        System.out.println("\n=== Painel Admin - " + nome.toUpperCase() + "===");
        System.out.println("1- Listar todas as contas");
        System.out.println("2- Listar todas as transações");
        System.out.println("0- Logout");
        System.out.print("Escolha: ");

        String opcao = scanner.nextLine().trim();

        switch (opcao){
            case "1" -> adminConsole.listarContas();
            case "2" -> adminConsole.listarTransacoes();
            case "0" -> {
                authService.logout();
                System.out.println("Logout realizado com sucesso.");
            }
            default -> System.out.println("Opção inválida. Tente novamente.");
        }
    }
}
