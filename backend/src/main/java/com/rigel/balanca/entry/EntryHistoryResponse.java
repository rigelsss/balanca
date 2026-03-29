package com.rigel.balanca.entry;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record EntryHistoryResponse(
        List<EntryHistoryItemResponse> items,
        int page,
        @JsonProperty("page_size")
        int pageSize,
        long total
) {
}
