package SystemITR.JuanWhite2A;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JuanWhite2AApplication {

	public static void main(String[] args) {
        // Cargar variables del .env a System Properties
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );
        SpringApplication.run(JuanWhite2AApplication.class, args);
	}
}