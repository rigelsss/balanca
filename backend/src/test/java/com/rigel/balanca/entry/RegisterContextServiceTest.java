package com.rigel.balanca.entry;

import com.rigel.balanca.batch.BatchService;
import com.rigel.balanca.catalog.CatalogOptionView;
import com.rigel.balanca.catalog.CatalogService;
import com.rigel.balanca.catalog.ReasonOptionView;
import com.rigel.balanca.daylog.DayLog;
import com.rigel.balanca.daylog.DayLogRepository;
import com.rigel.balanca.serial.SerialService;
import com.rigel.balanca.serial.SerialSnapshot;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterContextServiceTest {

    @Test
    void shouldBuildFirstUseContextWhenDayHasNoEntries() {
        DayLogRepository dayLogRepository = mock(DayLogRepository.class);
        UseEntryRepository useEntryRepository = mock(UseEntryRepository.class);
        CatalogService catalogService = mock(CatalogService.class);
        BatchService batchService = mock(BatchService.class);
        SerialService serialService = mock(SerialService.class);

        LocalDate today = LocalDate.now();
        DayLog dayLog = DayLog.create(today);

        when(dayLogRepository.findByDay(today)).thenReturn(Optional.of(dayLog));
        when(useEntryRepository.existsByDayLog_DayAndFirstOfDayTrue(today)).thenReturn(false);
        when(catalogService.listMoodOptions()).thenReturn(List.of(new CatalogOptionView(UUID.randomUUID(), "Ansioso", 1, true)));
        when(catalogService.listReasonOptions()).thenReturn(List.of(new ReasonOptionView(UUID.randomUUID(), "Outros", 1, true, true)));
        when(catalogService.listBatchTextureOptions()).thenReturn(List.of());
        when(catalogService.listBatchSmellOptions()).thenReturn(List.of());
        when(batchService.listActiveBatches()).thenReturn(List.of());
        when(serialService.getSafeSnapshot()).thenReturn(new SerialSnapshot(false, null, null, "disconnected", null, null, "Balanca desconectada"));

        RegisterContextService service = new RegisterContextService(
                dayLogRepository,
                useEntryRepository,
                catalogService,
                batchService,
                serialService
        );

        RegisterContextResponse response = service.getContext();

        assertTrue(response.firstUseAvailable());
        assertTrue(response.firstUseWillBeAuto());
        assertTrue(response.showSleepFields());
        assertFalse(response.sleepAlreadyRecorded());
        assertEquals("O que esta te levando a usar agora?", response.reasonPrompt());
        assertEquals("disconnected", response.serial().status());
        assertEquals(1, response.catalogs().moods().size());
    }
}
