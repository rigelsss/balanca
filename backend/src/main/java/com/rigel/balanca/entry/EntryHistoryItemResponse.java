package com.rigel.balanca.entry;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.UUID;

public record EntryHistoryItemResponse(
        UUID id,
        @JsonProperty("created_at")
        OffsetDateTime createdAt,
        @JsonProperty("mood_label")
        String moodLabel,
        @JsonProperty("reason_label")
        String reasonLabel,
        @JsonProperty("reason_text")
        String reasonText,
        @JsonProperty("first_of_day")
        boolean firstOfDay,
        @JsonProperty("away_long")
        boolean awayLong,
        @JsonProperty("ran_today")
        boolean ranToday,
        @JsonProperty("sleep_quality")
        Short sleepQuality,
        @JsonProperty("sleep_hours")
        Short sleepHours,
        @JsonProperty("batch_label")
        String batchLabel,
        @JsonProperty("measure_kind")
        String measureKind,
        @JsonProperty("stable_ok")
        Boolean stableOk,
        String notes
) {
}
