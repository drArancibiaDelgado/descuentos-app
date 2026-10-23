package bo.edu.usfx.descuentos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SeguridadAdminDeshabilitadoTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void sinTokenConfiguradoResponde403() throws Exception {
        mvc.perform(get("/api/admin/estadisticas").header("X-Admin-Token", "CualquierToken"))
                .andExpect(status().isForbidden());
    }
}