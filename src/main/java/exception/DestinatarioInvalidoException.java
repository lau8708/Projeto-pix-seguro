package exception;

public class DestinatarioInvalidoException extends PixException {
    public DestinatarioInvalidoException() {
        super("Você não é o destinatário desta transação.");
    }
}