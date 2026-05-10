package exception;

public class TransacaoNaoPendenteException extends PixException {
    public TransacaoNaoPendenteException() {
        super("Esta transação não está mais pendente.");
    }
}
