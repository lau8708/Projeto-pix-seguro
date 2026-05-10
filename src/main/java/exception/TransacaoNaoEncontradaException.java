package exception;

public class TransacaoNaoEncontradaException extends PixException {
    public TransacaoNaoEncontradaException() {
        super("Transação não encontrada.");
    }
}
