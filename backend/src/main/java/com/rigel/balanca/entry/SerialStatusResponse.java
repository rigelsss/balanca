package com.rigel.balanca.entry;

public record SerialStatusResponse(
        boolean connected,
        String port,
        Integer baud,
        String status,
        String lastMeasureKind,
        Boolean stableOk,
        String lastMessage
) {
}
