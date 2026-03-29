package com.rigel.balanca.entry;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateEntryResponse(
        UUID id,
        OffsetDateTime createdAt,
        String message,
        MeasureStatusResponse measureStatus
) {
}
