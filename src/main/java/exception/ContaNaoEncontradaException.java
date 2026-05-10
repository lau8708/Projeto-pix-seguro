package exception;

public class ContaNaoEncontradaException extends PixException {
    public ContaNaoEncontradaException() {
        super("Conta destinatária não encontrada.");
    }
}