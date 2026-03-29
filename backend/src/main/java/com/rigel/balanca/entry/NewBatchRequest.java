package com.rigel.balanca.entry;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record NewBatchRequest(
        String label,
        @JsonProperty("start_date")
        LocalDate startDate,
        BigDecimal grams,
        @JsonProperty("batch_value")
        BigDecimal batchValue,
        @JsonProperty("texture_option_id")
        UUID textureOptionId,
        @JsonProperty("smell_option_id")
        UUID smellOptionId,
        String notes
) {
}
