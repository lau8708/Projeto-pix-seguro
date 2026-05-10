package exception;

public class CpfInvalidoException extends PixException {
    public CpfInvalidoException() {
        super("CPF inválido. Use o formato 000.000.000-00.");
    }
}
