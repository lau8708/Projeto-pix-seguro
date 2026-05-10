package exception;

public class ValorSaqueInvalidoException extends PixException {
    public ValorSaqueInvalidoException() {
        super("Saldo insuficiente para realizar o saque.");
    }
}