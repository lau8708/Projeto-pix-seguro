package ui;

import exception.PixException;
import services.AuthService;
import services.UsuarioService;

import java.util.Scanner;

public class AuthConsole {

    private Scanner scanner;
    private AuthService authService;
    private UsuarioService usuarioService;

    public AuthConsole(Scanner scanner, AuthService authService, UsuarioService usuarioService){
        this.scanner = scanner;
        this.authService = authService;
        this.usuarioService = usuarioService;
    }

    public void cadastrar() {
        System.out.println("\n=== CADASTRO ===");

        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();

        System.out.print("CPF (formato 000.000.000-00): ");
        String cpf = scanner.nextLine().trim();

        System.out.print("Senha (mín. 8 caracteres, letras maiúsculas, minúsculas, número e símbolo): ");
        String senha = scanner.nextLine().trim();

        try {
            usuarioService.cadastrar(nome, cpf, senha);
            System.out.println("Usuário cadastrado com sucesso!");
        } catch (PixException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    public void login() {
        System.out.println("\n=== LOGIN ===");

        System.out.print("CPF: ");
        String cpf = scanner.nextLine().trim();

        System.out.print("Senha: ");
        String senha = scanner.nextLine().trim();

        try {
            authService.login(cpf, senha);
            System.out.println("Login realizado com sucesso!");
        } catch (PixException e) {
            System.out.println("Erro ao fazer login: " + e.getMessage());
        }
    }
}