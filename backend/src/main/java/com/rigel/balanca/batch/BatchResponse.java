package com.rigel.balanca.batch;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record BatchResponse(
        UUID id,
        String label,
        @JsonProperty("start_date")
        LocalDate startDate,
        BigDecimal grams,
        @JsonProperty("batch_value")
        BigDecimal batchValue,
        @JsonProperty("value_per_gram")
        BigDecimal valuePerGram,
        @JsonProperty("texture_option_id")
        UUID textureOptionId,
        @JsonProperty("texture_label")
        String textureLabel,
        @JsonProperty("smell_option_id")
        UUID smellOptionId,
        @JsonProperty("smell_label")
        String smellLabel,
        String notes,
        boolean active,
        @JsonProperty("created_at")
        OffsetDateTime createdAt
) {
}
