package exception;

public class TransacaoExpiradaException extends PixException {
    public TransacaoExpiradaException() {
        super("PIX expirado e cancelado automaticamente.");
    }
}
