import repository.ContaRepository;
import repository.TransacaoRepository;
import repository.UsuarioRepository;
import services.AdminService;
import services.AuthService;
import services.PixService;
import services.UsuarioService;
import ui.MenuPrincipal;

public class Main {

    public static void main(String[] args) {

        // repositories — criados uma única vez
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        ContaRepository contaRepository = new ContaRepository();
        TransacaoRepository transacaoRepository = new TransacaoRepository();

        // services — recebem os repositories
        AuthService authService = new AuthService(usuarioRepository);
        UsuarioService usuarioService = new UsuarioService(usuarioRepository, contaRepository);
        PixService pixService = new PixService(contaRepository, transacaoRepository, authService);
        AdminService adminService = new AdminService(contaRepository, transacaoRepository, authService);

        // ui — recebe os services
        MenuPrincipal menu = new MenuPrincipal(authService, usuarioService, pixService, adminService);
        menu.iniciar();
    }
}