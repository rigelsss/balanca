package com.rigel.balanca.entry;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EntryQueryServiceTest {

    @Test
    void shouldReturnMappedPage() {
        UseEntryRepository repository = mock(UseEntryRepository.class);
        UseEntry entry = mock(UseEntry.class);
        when(entry.getId()).thenReturn(java.util.UUID.randomUUID());
        when(entry.getCreatedAt()).thenReturn(java.time.OffsetDateTime.now());
        when(entry.getMoodLabelSnapshot()).thenReturn("Ansioso");
        when(entry.getReasonLabelSnapshot()).thenReturn("Vontade");
        when(entry.getReasonText()).thenReturn("teste");
        when(entry.isFirstOfDay()).thenReturn(true);
        when(entry.isAwayLong()).thenReturn(false);
        when(entry.isRanTodaySnapshot()).thenReturn(true);
        when(entry.getSleepQualitySnapshot()).thenReturn((short) 4);
        when(entry.getSleepHoursSnapshot()).thenReturn((short) 7);
        when(entry.getBatchLabelSnapshot()).thenReturn("Lote A");
        when(entry.getMeasureKind()).thenReturn("S");
        when(entry.getStableOk()).thenReturn(true);
        when(entry.getNotes()).thenReturn("ok");

        when(repository.findAll(any(org.springframework.data.jpa.domain.Specification.class), eq(PageRequest.of(0, 20, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt")))))
                .thenReturn(new PageImpl<>(List.of(entry), PageRequest.of(0, 20), 1));

        EntryQueryService service = new EntryQueryService(repository);
        EntryHistoryResponse response = service.listEntries(null, null, null, null, null, null, null);

        assertEquals(1, response.items().size());
        assertEquals("Ansioso", response.items().getFirst().moodLabel());
        assertEquals(1, response.total());
    }
}
