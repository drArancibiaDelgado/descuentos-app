package bo.edu.usfx.descuentos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import java.util.Map;

@RestControllerAdvice
public class ManejadorErrores {
    private static final Logger LOG = LoggerFactory.getLogger(ManejadorErrores.class);

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleCuerpoIlegible(Exception ex) {
        LOG.error("Cuerpo ilegible", ex);
        return ResponseEntity.badRequest().body(Map.of("error", "La solicitud no es válida"));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNoEncontrado(Exception ex) {
        LOG.error("Ruta no encontrada", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No encontrado"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleReglaNegocio(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleInesperado(Exception ex) {
        LOG.error("Error inesperado", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno"));
    }
}