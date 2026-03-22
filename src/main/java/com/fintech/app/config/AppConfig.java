package com.fintech.app.config;

import com.fintech.app.repository.RepositorioCuenta;
import com.fintech.app.repository.RepositorioMovimiento;
import com.fintech.app.repository.RepositorioUsuario;
import com.fintech.app.repository.memory.RepositorioCuentaEnMemoria;
import com.fintech.app.repository.memory.RepositorioMovimientoEnMemoria;
import com.fintech.app.repository.memory.RepositorioUsuarioEnMemoria;
import com.fintech.app.service.CuentaService;
import com.fintech.app.service.MovimientoService;
import com.fintech.app.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public RepositorioUsuario repositorioUsuario() {
        return new RepositorioUsuarioEnMemoria();
    }

    @Bean
    public RepositorioCuenta repositorioCuenta() {
        return new RepositorioCuentaEnMemoria();
    }

    @Bean
    public RepositorioMovimiento repositorioMovimiento() {
        return new RepositorioMovimientoEnMemoria();
    }

    @Bean
    public UsuarioService usuarioService(RepositorioUsuario repositorioUsuario) {
        return new UsuarioService(repositorioUsuario);
    }

    @Bean
    public CuentaService cuentaService(
            RepositorioCuenta repositorioCuenta,
            RepositorioUsuario repositorioUsuario,
            RepositorioMovimiento repositorioMovimiento) {
        return new CuentaService(repositorioCuenta, repositorioUsuario, repositorioMovimiento);
    }

    @Bean
    public MovimientoService movimientoService(
            RepositorioMovimiento repositorioMovimiento,
            RepositorioCuenta repositorioCuenta) {
        return new MovimientoService(repositorioMovimiento, repositorioCuenta);
    }

    @Bean
    public IdGeneratorService idGeneratorService() {
        return new IdGeneratorService();
    }
}
