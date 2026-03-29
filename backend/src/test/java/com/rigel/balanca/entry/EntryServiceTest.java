package com.rigel.balanca.entry;

import com.rigel.balanca.batch.Batch;
import com.rigel.balanca.batch.BatchRepository;
import com.rigel.balanca.catalog.MoodOption;
import com.rigel.balanca.catalog.MoodOptionRepository;
import com.rigel.balanca.catalog.ReasonOption;
import com.rigel.balanca.catalog.ReasonOptionRepository;
import com.rigel.balanca.catalog.BatchSmellOptionRepository;
import com.rigel.balanca.catalog.BatchTextureOptionRepository;
import com.rigel.balanca.daylog.DayLog;
import com.rigel.balanca.daylog.DayLogRepository;
import com.rigel.balanca.serial.MeasurementResult;
import com.rigel.balanca.serial.SerialService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EntryServiceTest {

    @Test
    void shouldRequireTextWhenReasonRequiresIt() {
        DayLogRepository dayLogRepository = mock(DayLogRepository.class);
        UseEntryRepository useEntryRepository = mock(UseEntryRepository.class);
        MoodOptionRepository moodOptionRepository = mock(MoodOptionRepository.class);
        ReasonOptionRepository reasonOptionRepository = mock(ReasonOptionRepository.class);
        BatchRepository batchRepository = mock(BatchRepository.class);
        BatchTextureOptionRepository batchTextureOptionRepository = mock(BatchTextureOptionRepository.class);
        BatchSmellOptionRepository batchSmellOptionRepository = mock(BatchSmellOptionRepository.class);
        SerialService serialService = mock(SerialService.class);

        LocalDate today = LocalDate.now();
        DayLog dayLog = DayLog.create(today);
        MoodOption mood = buildMood("Ansioso");
        ReasonOption reason = buildReason("Outros", true);
        Batch batch = Batch.create("Lote A", today, new BigDecimal("10"), new BigDecimal("100"), new BigDecimal("10"), null, null, null, null, null);

        when(dayLogRepository.findByDay(today)).thenReturn(Optional.of(dayLog));
        when(useEntryRepository.existsByDayLog_DayAndFirstOfDayTrue(today)).thenReturn(false);
        when(moodOptionRepository.findById(any())).thenReturn(Optional.of(mood));
        when(reasonOptionRepository.findById(any())).thenReturn(Optional.of(reason));
        when(batchRepository.findById(any())).thenReturn(Optional.of(batch));

        EntryService service = new EntryService(
                dayLogRepository,
                useEntryRepository,
                moodOptionRepository,
                reasonOptionRepository,
                batchRepository,
                batchTextureOptionRepository,
                batchSmellOptionRepository,
                serialService
        );

        CreateEntryRequest request = new CreateEntryRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "",
                false,
                false,
                null,
                null,
                "existing",
                UUID.randomUUID(),
                null,
                null
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.createEntry(request));
        assertEquals("Este motivo exige complemento textual", exception.getMessage());
    }

    private MoodOption buildMood(String label) {
        try {
            MoodOption mood = new MoodOption();
            var id = MoodOption.class.getDeclaredField("id");
            id.setAccessible(true);
            id.set(mood, UUID.randomUUID());
            var labelField = MoodOption.class.getDeclaredField("label");
            labelField.setAccessible(true);
            labelField.set(mood, label);
            var activeField = MoodOption.class.getDeclaredField("active");
            activeField.setAccessible(true);
            activeField.set(mood, true);
            return mood;
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException(exception);
        }
    }

    private ReasonOption buildReason(String label, boolean requiresText) {
        try {
            ReasonOption reason = new ReasonOption();
            var id = ReasonOption.class.getDeclaredField("id");
            id.setAccessible(true);
            id.set(reason, UUID.randomUUID());
            var labelField = ReasonOption.class.getDeclaredField("label");
            labelField.setAccessible(true);
            labelField.set(reason, label);
            var activeField = ReasonOption.class.getDeclaredField("active");
            activeField.setAccessible(true);
            activeField.set(reason, true);
            var requiresTextField = ReasonOption.class.getDeclaredField("requiresText");
            requiresTextField.setAccessible(true);
            requiresTextField.set(reason, requiresText);
            return reason;
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException(exception);
        }
    }
}
