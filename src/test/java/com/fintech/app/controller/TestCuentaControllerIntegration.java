package com.fintech.app.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestCuentaControllerIntegration {
    @Autowired
    private MockMvc mockMvc;

    private void registrarUsuario() throws Exception {
        String body = """
                {
                  "nombre":"Lucio",
                  "apellido":"Ameri",
                  "email":"lucio@gmail.com",
                  "password":"Abc1234!",
                  "dni":"12345678"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    void crearCuenta_deberiaFuncionar() throws Exception {
        registrarUsuario();

        String body = """
                {
                  "usuarioId":1,
                  "tipoCuenta":"CAJA_DE_AHORRO_EN_PESOS"
                }
                """;

        mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cuentaId").value(1))
                .andExpect(jsonPath("$.usuarioId").value(1));
    }

    @Test
    void depositar_deberiaActualizarSaldo() throws Exception {
        registrarUsuario();

        String cuenta = """
            {
              "usuarioId":1,
              "tipoCuenta":"CAJA_DE_AHORRO_EN_PESOS"
            }
            """;

        mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuenta))
                .andExpect(status().isCreated());

        String deposito = """
            {
              "monto":1000.0,
              "moneda":"ARS"
            }
            """;

        mockMvc.perform(post("/api/cuentas/1/depositos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deposito))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldo").value(1000.0))
                .andExpect(jsonPath("$.moneda").value("ARS"));
    }

    @Test
    void retirar_deberiaFallarSiNoHaySaldo() throws Exception {
        registrarUsuario();

        String cuenta = """
                {
                  "usuarioId":1,
                  "tipoCuenta":"CAJA_DE_AHORRO_EN_PESOS"
                }
                """;

        mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuenta))
                .andExpect(status().isCreated());

        String retiro = """
                {
                  "monto":100.0,
                  "moneda":"ARS"
                }
                """;

        mockMvc.perform(post("/api/cuentas/1/retiros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(retiro))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listarMovimientos_deberiaDevolverHistorial() throws Exception {
        registrarUsuario();

        String cuenta = """
                {
                  "usuarioId":1,
                  "tipoCuenta":"CAJA_DE_AHORRO_EN_PESOS"
                }
                """;

        mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuenta))
                .andExpect(status().isCreated());

        String deposito = """
                {
                  "monto":1000.0,
                  "moneda":"ARS"
                }
                """;

        mockMvc.perform(post("/api/cuentas/1/depositos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deposito))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/cuentas/1/movimientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void eliminarCuenta_deberiaResponder204() throws Exception {
        registrarUsuario();

        String cuenta = """
                {
                  "usuarioId":1,
                  "tipoCuenta":"CAJA_DE_AHORRO_EN_PESOS"
                }
                """;

        mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuenta))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/api/cuentas/1"))
                .andExpect(status().isNoContent());
    }
}
