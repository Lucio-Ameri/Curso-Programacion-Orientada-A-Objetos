package com.fintech.app.config;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGeneratorService {
    private final AtomicInteger usuarioSequence = new AtomicInteger(0);
    private final AtomicInteger cuentaSequence = new AtomicInteger(0);
    private final AtomicInteger movimientoSequence = new AtomicInteger(0);

    public int nextUsuarioId() {
        return usuarioSequence.incrementAndGet();
    }

    public int nextCuentaId() {
        return cuentaSequence.incrementAndGet();
    }

    public int nextMovimientoId() {
        return movimientoSequence.incrementAndGet();
    }
}
