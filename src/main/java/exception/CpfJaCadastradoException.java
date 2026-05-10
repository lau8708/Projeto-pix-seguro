package exception;

public class CpfJaCadastradoException extends PixException {
    public CpfJaCadastradoException() {
        super("CPF já cadastrado no sistema.");
    }
}
