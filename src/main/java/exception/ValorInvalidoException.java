package exception;

public class ValorInvalidoException extends PixException {
    public ValorInvalidoException() {
        super("O valor do PIX deve ser maior que zero.");
    }
}