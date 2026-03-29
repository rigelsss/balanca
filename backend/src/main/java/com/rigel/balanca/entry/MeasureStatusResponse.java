package com.rigel.balanca.entry;

public record MeasureStatusResponse(
        String measureKind,
        Boolean stableOk,
        String label
) {
}
