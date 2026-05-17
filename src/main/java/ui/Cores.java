package ui;

public class Cores {

    public static final String VERMELHO = "\u001B[31m";
    public static final String VERDE    = "\u001B[32m";
    public static final String RESET    = "\u001B[0m";

    public static String vermelho(String texto){
        return VERMELHO + texto + RESET;
    }

    public static String verde(String texto) {
        return VERDE + texto + RESET;
    }
}