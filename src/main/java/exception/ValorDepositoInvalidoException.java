package exception;

public class ValorDepositoInvalidoException extends PixException {
    public ValorDepositoInvalidoException() {
        super("O valor do depósito deve ser maior que zero.");
    }
}