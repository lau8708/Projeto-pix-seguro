package exception;

public class UsuarioNaoEncontradoException extends PixException {
    public UsuarioNaoEncontradoException() {
        super("Usuário não encontrado.");
    }
}
