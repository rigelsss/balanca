package com.rigel.balanca.serial;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SerialService {

    private final boolean mockEnabled;

    public SerialService(@Value("${app.measurement.mock-enabled:true}") boolean mockEnabled) {
        this.mockEnabled = mockEnabled;
    }

    public SerialSnapshot getSafeSnapshot() {
        if (mockEnabled) {
            return new SerialSnapshot(
                    true,
                    "mock",
                    0,
                    "stable",
                    "S",
                    true,
                    "Leitura estavel disponivel em modo mock"
            );
        }
        return new SerialSnapshot(
                false,
                null,
                null,
                "disconnected",
                null,
                null,
                "Balanca desconectada"
        );
    }

    public MeasurementResult getMeasurementForSave(boolean requireStableMeasurement) {
        if (mockEnabled) {
            return new MeasurementResult(
                    "S",
                    true,
                    new BigDecimal("0.000500"),
                    new BigDecimal("1.234000"),
                    "[MOCK] stable=YES",
                    "mock",
                    0,
                    "Leitura estavel confirmada"
            );
        }

        throw new IllegalArgumentException(requireStableMeasurement
                ? "Nao foi possivel salvar sem uma leitura estavel da balanca"
                : "Nao foi possivel salvar sem leitura valida da balanca");
    }
}
