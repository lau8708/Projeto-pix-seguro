package exception;

public class ValorInvalidoException extends RuntimeException {
    public ValorInvalidoException() {
        super("O valor do PIX deve ser maior que zero.");
    }
}