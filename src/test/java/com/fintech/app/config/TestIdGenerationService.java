package com.fintech.app.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestIdGenerationService {

    @Test
    void deberiaGenerarIdsIncrementales() {
        IdGeneratorService generator = new IdGeneratorService();

        assertEquals(1, generator.nextUsuarioId());
        assertEquals(2, generator.nextUsuarioId());

        assertEquals(1, generator.nextCuentaId());
        assertEquals(2, generator.nextCuentaId());

        assertEquals(1, generator.nextMovimientoId());
        assertEquals(2, generator.nextMovimientoId());
    }
}
