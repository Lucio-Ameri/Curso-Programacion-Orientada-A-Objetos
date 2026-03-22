package com.fintech.app.controller;

import com.fintech.app.config.IdGeneratorService;
import com.fintech.app.dto.mapper.ApiMapper;
import com.fintech.app.dto.request.CrearCuentaRequest;
import com.fintech.app.dto.request.OperacionDineroRequest;
import com.fintech.app.dto.response.CuentaResponse;
import com.fintech.app.dto.response.MovimientoResponse;
import com.fintech.app.model.Cuenta;
import com.fintech.app.model.Dinero;
import com.fintech.app.service.CuentaService;
import com.fintech.app.service.MovimientoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;
    private final MovimientoService movimientoService;
    private final IdGeneratorService idGeneratorService;

    public CuentaController(CuentaService cuentaService,
                            MovimientoService movimientoService,
                            IdGeneratorService idGeneratorService) {
        this.cuentaService = cuentaService;
        this.movimientoService = movimientoService;
        this.idGeneratorService = idGeneratorService;
    }

    @PostMapping
    public ResponseEntity<CuentaResponse> crearCuenta(@Valid @RequestBody CrearCuentaRequest request) {
        Cuenta nuevaCuenta = new Cuenta(
                idGeneratorService.nextCuentaId(),
                request.usuarioId(),
                request.tipoCuenta()
        );

        Cuenta cuentaRegistrada = cuentaService.registrarCuenta(nuevaCuenta);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiMapper.toCuentaResponse(cuentaRegistrada));
    }

    @GetMapping("/{cuentaId}")
    public ResponseEntity<CuentaResponse> buscarCuentaPorId(@PathVariable int cuentaId) {
        Cuenta cuenta = cuentaService.buscarPorID(cuentaId)
                .orElseThrow(() -> new IllegalArgumentException("NO EXISTE UNA CUENTA CON ESE ID"));

        return ResponseEntity.ok(ApiMapper.toCuentaResponse(cuenta));
    }

    @GetMapping
    public ResponseEntity<List<CuentaResponse>> listarCuentas() {
        List<CuentaResponse> response = cuentaService.listarCuentas()
                .stream()
                .map(ApiMapper::toCuentaResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CuentaResponse>> listarPorUsuario(@PathVariable int usuarioId) {
        List<CuentaResponse> response = cuentaService.listarPorUsuarioID(usuarioId)
                .stream()
                .map(ApiMapper::toCuentaResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{cuentaId}/depositos")
    public ResponseEntity<CuentaResponse> depositar(@PathVariable int cuentaId,
                                                    @Valid @RequestBody OperacionDineroRequest request) {
        Dinero dinero = new Dinero(request.monto(), request.moneda());

        Cuenta cuentaActualizada = cuentaService.depositarDinero(
                cuentaId,
                dinero,
                idGeneratorService.nextMovimientoId()
        );

        return ResponseEntity.ok(ApiMapper.toCuentaResponse(cuentaActualizada));
    }

    @PostMapping("/{cuentaId}/retiros")
    public ResponseEntity<CuentaResponse> retirar(@PathVariable int cuentaId,
                                                  @Valid @RequestBody OperacionDineroRequest request) {
        Dinero dinero = new Dinero(request.monto(), request.moneda());

        Cuenta cuentaActualizada = cuentaService.retirarDinero(
                cuentaId,
                dinero,
                idGeneratorService.nextMovimientoId()
        );

        return ResponseEntity.ok(ApiMapper.toCuentaResponse(cuentaActualizada));
    }

    @GetMapping("/{cuentaId}/movimientos")
    public ResponseEntity<List<MovimientoResponse>> listarMovimientos(@PathVariable int cuentaId) {
        List<MovimientoResponse> response = movimientoService.listarPorCuentaID(cuentaId)
                .stream()
                .map(ApiMapper::toMovimientoResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{cuentaId}")
    public ResponseEntity<Void> eliminarCuenta(@PathVariable int cuentaId) {
        cuentaService.eliminarPorID(cuentaId);
        return ResponseEntity.noContent().build();
    }
}
