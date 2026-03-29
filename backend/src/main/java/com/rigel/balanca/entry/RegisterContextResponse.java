package com.rigel.balanca.entry;

import java.time.LocalDate;

public record RegisterContextResponse(
        LocalDate today,
        boolean firstUseWillBeAuto,
        boolean firstUseAvailable,
        boolean showSleepFields,
        boolean sleepAlreadyRecorded,
        boolean ranTodayLocked,
        boolean ranTodayValue,
        String reasonPrompt,
        boolean requireStableMeasurement,
        RegisterCatalogsResponse catalogs,
        SerialStatusResponse serial
) {
}
