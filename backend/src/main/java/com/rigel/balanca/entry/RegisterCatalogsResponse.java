package com.rigel.balanca.entry;

import com.rigel.balanca.batch.BatchOptionView;
import com.rigel.balanca.catalog.CatalogOptionView;
import com.rigel.balanca.catalog.ReasonOptionView;
import java.util.List;

public record RegisterCatalogsResponse(
        List<CatalogOptionView> moods,
        List<ReasonOptionView> reasons,
        List<CatalogOptionView> batchTextures,
        List<CatalogOptionView> batchSmells,
        List<BatchOptionView> batches
) {
}
