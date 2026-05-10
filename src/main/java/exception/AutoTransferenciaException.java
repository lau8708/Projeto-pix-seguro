package exception;

public class AutoTransferenciaException extends RuntimeException {
    public AutoTransferenciaException() {
        super("Não é possível enviar PIX para si mesmo.");
    }
}