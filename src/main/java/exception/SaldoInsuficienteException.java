package exception;

public class SaldoInsuficienteException extends PixException {
    public SaldoInsuficienteException() {
        super("Saldo insuficiente para realizar o PIX.");
    }
}
