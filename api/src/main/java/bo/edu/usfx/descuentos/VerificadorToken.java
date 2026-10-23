package bo.edu.usfx.descuentos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.MessageDigest;

@Component
public class VerificadorToken {
    
    // Lee el token directamente de las variables de entorno, no de application.properties
    @Value("${ADMIN_TOKEN:#{null}}")
    private String tokenConfigurado;

    public boolean esValido(String tokenProporcionado) {
        // Si no hay token configurado en el servidor, falla cerrado (403)
        if (tokenConfigurado == null || tokenConfigurado.length() < 16) {
            throw new IllegalStateException("El servidor no tiene un token configurado de forma segura");
        }
        // Si el cliente no envió token, falla (401)
        if (tokenProporcionado == null) {
            return false;
        }
        // Comparación en tiempo constante
        return MessageDigest.isEqual(
                tokenConfigurado.getBytes(),
                tokenProporcionado.getBytes()
        );
    }
}