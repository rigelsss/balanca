package com.rigel.balanca.serial;

import java.math.BigDecimal;

public record MeasurementResult(
        String measureKind,
        Boolean stableOk,
        BigDecimal stableSpanG,
        BigDecimal weightG,
        String serialLine,
        String serialPort,
        Integer serialBaud,
        String publicLabel
) {
}
