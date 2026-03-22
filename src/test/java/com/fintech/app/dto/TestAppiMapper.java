package com.fintech.app.dto;

import com.fintech.app.dto.mapper.ApiMapper;
import com.fintech.app.dto.response.UsuarioResponse;
import com.fintech.app.model.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAppiMapper {

    @Test
    void toUsuarioResponse_deberiaMapearCorrectamente() {
        Usuario usuario = new Usuario(1, "Lucio", "Ameri", "lucio@gmail.com", "Abc1234!", "12345678");

        UsuarioResponse response = ApiMapper.toUsuarioResponse(usuario);

        assertEquals(1, response.usuarioId());
        assertEquals("Lucio", response.nombre());
        assertEquals("Ameri", response.apellido());
        assertEquals("lucio@gmail.com", response.email());
        assertEquals("12345678", response.dni());
    }
}
