package com.rigel.balanca.catalog;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record CatalogUpsertRequest(
        @NotBlank(message = "label e obrigatorio")
        String label,
        Boolean active,
        @JsonProperty("sort_order")
        Integer sortOrder,
        @JsonProperty("requires_text")
        Boolean requiresText
) {
}
