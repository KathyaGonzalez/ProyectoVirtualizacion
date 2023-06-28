package kathya.bilena.springboot;

import kathya.bilena.springboot.model.Usuario;
import kathya.bilena.springboot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootBackendApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBackendApplication.class, args);
	}

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public void run(String... args) throws Exception {
		Usuario usuario = new Usuario();
		usuario.setNombre("Andres");
		usuario.setCorreo("pruebaAndres@gmail.com");
		usuario.setEmpleo("AdminAndres");
		usuarioRepository.save(usuario);
	}
}
