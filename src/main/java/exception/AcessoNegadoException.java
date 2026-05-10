package exception;

public class AcessoNegadoException extends PixException {
    public AcessoNegadoException() {
        super("Acesso negado. Esta funcionalidade é restrita ao administrador.");
    }
}
