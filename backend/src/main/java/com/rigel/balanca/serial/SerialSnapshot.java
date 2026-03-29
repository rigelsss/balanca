package com.rigel.balanca.serial;

public record SerialSnapshot(
        boolean connected,
        String port,
        Integer baud,
        String status,
        String lastMeasureKind,
        Boolean stableOk,
        String lastMessage
) {
}
