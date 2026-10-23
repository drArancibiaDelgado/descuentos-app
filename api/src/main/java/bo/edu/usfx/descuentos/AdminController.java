package bo.edu.usfx.descuentos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final VerificadorToken verificador;

    public AdminController(VerificadorToken verificador) {
        this.verificador = verificador;
    }

    @GetMapping("/estadisticas")
    public Estadisticas obtenerEstadisticas(@RequestHeader(value = "X-Admin-Token", required = false) String token) {
        try {
            if (!verificador.esValido(token)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token ausente o incorrecto");
            }
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Servidor sin configurar");
        }
        return new Estadisticas(0); // Simula el conteo de cálculos
    }
}