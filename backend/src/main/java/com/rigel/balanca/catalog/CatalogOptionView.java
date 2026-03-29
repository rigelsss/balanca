package com.rigel.balanca.catalog;

import java.util.UUID;

public record CatalogOptionView(
        UUID id,
        String label,
        Integer sortOrder,
        Boolean active
) {
}
