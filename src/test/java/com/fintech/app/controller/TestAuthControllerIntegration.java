package com.fintech.app.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestAuthControllerIntegration {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void register_deberiaRegistrarUsuario() throws Exception {
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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("Usuario registrado correctamente"))
                .andExpect(jsonPath("$.usuario.usuarioId").value(1))
                .andExpect(jsonPath("$.usuario.email").value("lucio@gmail.com"));
    }

    @Test
    void register_deberiaFallarSiDatosInvalidos() throws Exception {
        String body = """
                {
                  "nombre":"",
                  "apellido":"Ameri",
                  "email":"correo-mal",
                  "password":"",
                  "dni":"12"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    void login_deberiaFuncionarSiCredencialesValidas() throws Exception {
        String registro = """
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
                        .content(registro))
                .andExpect(status().isCreated());

        String login = """
                {
                  "email":"lucio@gmail.com",
                  "password":"Abc1234!"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(login))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Login correcto"))
                .andExpect(jsonPath("$.usuario.email").value("lucio@gmail.com"));
    }

    @Test
    void login_deberiaFallarSiPasswordIncorrecta() throws Exception {
        String registro = """
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
                        .content(registro))
                .andExpect(status().isCreated());

        String login = """
                {
                  "email":"lucio@gmail.com",
                  "password":"incorrecta"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(login))
                .andExpect(status().isBadRequest());
    }
}
