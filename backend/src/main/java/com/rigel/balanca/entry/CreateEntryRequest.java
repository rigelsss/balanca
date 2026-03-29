package com.rigel.balanca.entry;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateEntryRequest(
        @JsonProperty("mood_option_id")
        @NotNull(message = "mood_option_id e obrigatorio")
        UUID moodOptionId,
        @JsonProperty("reason_option_id")
        @NotNull(message = "reason_option_id e obrigatorio")
        UUID reasonOptionId,
        @JsonProperty("reason_text")
        String reasonText,
        @JsonProperty("away_long")
        Boolean awayLong,
        @JsonProperty("ran_today")
        Boolean ranToday,
        @JsonProperty("sleep_quality")
        Short sleepQuality,
        @JsonProperty("sleep_hours")
        Short sleepHours,
        @JsonProperty("batch_mode")
        @NotNull(message = "batch_mode e obrigatorio")
        String batchMode,
        @JsonProperty("batch_id")
        UUID batchId,
        @JsonProperty("new_batch")
        @Valid
        NewBatchRequest newBatch,
        @JsonProperty("notes")
        String notes
) {
}
