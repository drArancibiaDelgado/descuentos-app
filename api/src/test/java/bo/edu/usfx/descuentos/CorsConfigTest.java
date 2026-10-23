package bo.edu.usfx.descuentos;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CorsConfigTest {

    @Test
    void rechazaComodinEnCors() {
        CorsConfig config = new CorsConfig();
        ReflectionTestUtils.setField(config, "corsOrigins", "*");

        assertThrows(IllegalStateException.class, () -> {
            config.corsConfigurer();
        });
    }
}