package exception;

public class SenhaInvalidaException extends PixException {
    public SenhaInvalidaException() {
        super("Senha inválida. A senha deve ter no mínimo 8 caracteres, uma letra maiúscula, uma letra minúscula, um número e não pode haver espaços em branco.");
    }
}
