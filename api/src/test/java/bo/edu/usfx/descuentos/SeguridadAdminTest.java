package bo.edu.usfx.descuentos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "ADMIN_TOKEN=TokenSuperSecretoParaPruebas123")
@AutoConfigureMockMvc
class SeguridadAdminTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void sinTokenResponde401() throws Exception {
        mvc.perform(get("/api/admin/estadisticas"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void conTokenIncorrectoResponde401() throws Exception {
        mvc.perform(get("/api/admin/estadisticas").header("X-Admin-Token", "TokenFalso"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void conTokenCorrectoResponde200() throws Exception {
        mvc.perform(get("/api/admin/estadisticas").header("X-Admin-Token", "TokenSuperSecretoParaPruebas123"))
                .andExpect(status().isOk());
    }
}