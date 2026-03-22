package com.fintech.app.controller;

import com.fintech.app.config.IdGeneratorService;
import com.fintech.app.dto.mapper.ApiMapper;
import com.fintech.app.dto.request.LoginRequest;
import com.fintech.app.dto.request.RegistroUsuarioRequest;
import com.fintech.app.dto.response.AuthResponse;
import com.fintech.app.model.Usuario;
import com.fintech.app.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final IdGeneratorService idGeneratorService;

    public AuthController(UsuarioService usuarioService, IdGeneratorService idGeneratorService) {
        this.usuarioService = usuarioService;
        this.idGeneratorService = idGeneratorService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegistroUsuarioRequest request) {
        Usuario nuevoUsuario = new Usuario(
                idGeneratorService.nextUsuarioId(),
                request.nombre(),
                request.apellido(),
                request.email(),
                request.password(),
                request.dni()
        );

        Usuario usuarioRegistrado = usuarioService.registrarUsuario(nuevoUsuario);

        AuthResponse response = new AuthResponse(
                "Usuario registrado correctamente",
                ApiMapper.toUsuarioResponse(usuarioRegistrado)
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Usuario usuarioAutenticado = usuarioService.autenticar(request.email(), request.password());

        AuthResponse response = new AuthResponse(
                "Login correcto",
                ApiMapper.toUsuarioResponse(usuarioAutenticado)
        );

        return ResponseEntity.ok(response);
    }
}
