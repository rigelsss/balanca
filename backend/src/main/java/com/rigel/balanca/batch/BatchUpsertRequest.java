package com.rigel.balanca.batch;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record BatchUpsertRequest(
        @NotBlank(message = "label e obrigatorio")
        String label,
        @JsonProperty("start_date")
        @NotNull(message = "start_date e obrigatorio")
        LocalDate startDate,
        @NotNull(message = "grams e obrigatorio")
        BigDecimal grams,
        @JsonProperty("batch_value")
        @NotNull(message = "batch_value e obrigatorio")
        BigDecimal batchValue,
        @JsonProperty("texture_option_id")
        UUID textureOptionId,
        @JsonProperty("smell_option_id")
        UUID smellOptionId,
        String notes,
        Boolean active
) {
}
