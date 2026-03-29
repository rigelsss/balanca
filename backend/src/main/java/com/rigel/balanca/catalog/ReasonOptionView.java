package com.rigel.balanca.catalog;

import java.util.UUID;

public record ReasonOptionView(
        UUID id,
        String label,
        Integer sortOrder,
        Boolean active,
        Boolean requiresText
) {
}
