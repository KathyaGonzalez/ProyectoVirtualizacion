package kathya.bilena.springboot;

import kathya.bilena.springboot.controller.UsuarioController;
import kathya.bilena.springboot.model.Usuario;
import kathya.bilena.springboot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

@SpringBootApplication
public class SpringbootBackendApplication extends Thread implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBackendApplication.class, args);
	}

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private UsuarioController usuarioController;
	@Override
	public void run(String... args) throws Exception{
		int opcion = 1;
		while (opcion!= 3) {
			System.out.println("------------------------------------------------------------------------------------------------------");
			System.out.println("                                               Menú");
			System.out.println("------------------------------------------------------------------------------------------------------");
			System.out.println("1. Cargar datos");
			System.out.println("2. Mostrar datos");
			System.out.println("3. Salir");
			Scanner entrada = new Scanner(System.in);
			System.out.printf("Ingrese la opción que desea ejecutar: ");
			opcion = entrada.nextInt();
			if (opcion == 1) {
				System.out.println("------------------------------------------------------------------------------------------------------");
				System.out.println("                                             CARGAR DATOS");
				System.out.println("------------------------------------------------------------------------------------------------------");
				try{
					int peticiones = 2;
					URL url = new URL("https://random-data-api.com/api/v2/users?size=" + peticiones);
					HttpURLConnection conn= (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.connect();
					int responseCode = conn.getResponseCode();
					if (responseCode != 200){
						throw new RuntimeException("Ocurrió un error: "+ responseCode);
					}else{
						StringBuilder informationString = new StringBuilder();
						Scanner scanner = new Scanner(url.openStream());
						while(scanner.hasNext()){
							informationString.append(scanner.nextLine());
						}
						scanner.close();
						JSONArray jsonArray = new JSONArray(informationString.toString());
						for(int x=0; x< peticiones; x++){
							JSONObject jsonObject = jsonArray.getJSONObject(x);
							String nombre = jsonObject.getString("first_name") + " " + jsonObject.getString("last_name");
							System.out.println("nombre: " + nombre);
							String correo = jsonObject.getString("email");
							System.out.println("correo: " + correo);
							String empleo = jsonObject.getJSONObject("employment").getString("title");
							System.out.println("empleo: " + empleo);
							Usuario usuario = new Usuario();
							usuario.setNombre(nombre);
							usuario.setCorreo(correo);
							usuario.setEmpleo(empleo);
							usuarioRepository.save(usuario);
							Thread.sleep(1000);
							System.out.println("------------------------------------------------------------------------------------------------------");
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			} else if (opcion == 2) {
				System.out.println("------------------------------------------------------------------------------------------------------");
				System.out.println("                                               MOSTRAR DATOS");
				System.out.println("------------------------------------------------------------------------------------------------------");
				System.out.println("          Nombre                     Correo                              Empleo          ");
				for (int i = 0; i < usuarioController.getAllUsuarios().size(); i++){
					String nombre = usuarioController.getAllUsuarios().get(i).getNombre();
					String espacio = "          ";
					if (nombre.length() < 20) {
							for (int j = 0; j < (20 - nombre.length()); j++){
								espacio += " ";
							}
					}
					String correo = usuarioController.getAllUsuarios().get(i).getCorreo();
					String espacioCorreo = "          ";
					if (correo.length() < 30) {
						for (int j = 0; j < (30 - correo.length()); j++){
							espacioCorreo += " ";
						}
					}
					System.out.println(nombre + espacio + correo + espacioCorreo + usuarioController.getAllUsuarios().get(i).getEmpleo());
				}
			} else {
				System.out.println("------------------------------------------------------------------------------------------------------");
				System.out.println("                                             SALIR");
				System.out.println("------------------------------------------------------------------------------------------------------");
				System.exit(0);
			}
		}
	}
}