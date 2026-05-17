package exception;

public class AutoTransferenciaException extends PixException {
    public AutoTransferenciaException() {
        super("Não é possível enviar PIX para si mesmo.");
    }
}