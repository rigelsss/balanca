package com.rigel.balanca.entry;

import com.rigel.balanca.batch.Batch;
import com.rigel.balanca.batch.BatchRepository;
import com.rigel.balanca.catalog.BatchSmellOption;
import com.rigel.balanca.catalog.BatchSmellOptionRepository;
import com.rigel.balanca.catalog.BatchTextureOption;
import com.rigel.balanca.catalog.BatchTextureOptionRepository;
import com.rigel.balanca.catalog.MoodOption;
import com.rigel.balanca.catalog.MoodOptionRepository;
import com.rigel.balanca.catalog.ReasonOption;
import com.rigel.balanca.catalog.ReasonOptionRepository;
import com.rigel.balanca.daylog.DayLog;
import com.rigel.balanca.daylog.DayLogRepository;
import com.rigel.balanca.serial.MeasurementResult;
import com.rigel.balanca.serial.SerialService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EntryService {

    private static final String APP_VERSION = "0.0.1";

    private final DayLogRepository dayLogRepository;
    private final UseEntryRepository useEntryRepository;
    private final MoodOptionRepository moodOptionRepository;
    private final ReasonOptionRepository reasonOptionRepository;
    private final BatchRepository batchRepository;
    private final BatchTextureOptionRepository batchTextureOptionRepository;
    private final BatchSmellOptionRepository batchSmellOptionRepository;
    private final SerialService serialService;

    public EntryService(DayLogRepository dayLogRepository,
                        UseEntryRepository useEntryRepository,
                        MoodOptionRepository moodOptionRepository,
                        ReasonOptionRepository reasonOptionRepository,
                        BatchRepository batchRepository,
                        BatchTextureOptionRepository batchTextureOptionRepository,
                        BatchSmellOptionRepository batchSmellOptionRepository,
                        SerialService serialService) {
        this.dayLogRepository = dayLogRepository;
        this.useEntryRepository = useEntryRepository;
        this.moodOptionRepository = moodOptionRepository;
        this.reasonOptionRepository = reasonOptionRepository;
        this.batchRepository = batchRepository;
        this.batchTextureOptionRepository = batchTextureOptionRepository;
        this.batchSmellOptionRepository = batchSmellOptionRepository;
        this.serialService = serialService;
    }

    @Transactional
    public CreateEntryResponse createEntry(CreateEntryRequest request) {
        LocalDate today = LocalDate.now();
        DayLog dayLog = dayLogRepository.findByDay(today)
                .orElseGet(() -> dayLogRepository.save(DayLog.create(today)));

        MoodOption mood = moodOptionRepository.findById(request.moodOptionId())
                .filter(MoodOption::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Humor invalido"));

        ReasonOption reason = reasonOptionRepository.findById(request.reasonOptionId())
                .filter(ReasonOption::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Motivo invalido"));

        String normalizedReasonText = normalize(request.reasonText());
        if (reason.isRequiresText() && normalizedReasonText == null) {
            throw new IllegalArgumentException("Este motivo exige complemento textual");
        }

        boolean firstOfDay = !dayLog.isHasFirstUse() && !useEntryRepository.existsByDayLog_DayAndFirstOfDayTrue(today);
        boolean ranTodayFinal = dayLog.isRanToday() || Boolean.TRUE.equals(request.ranToday());
        Short sleepQualitySnapshot = null;
        Short sleepHoursSnapshot = null;

        if (firstOfDay && dayLog.getSleepQuality() == null && dayLog.getSleepHours() == null) {
            if (request.sleepQuality() != null || request.sleepHours() != null) {
                validateSleep(request.sleepQuality(), request.sleepHours());
                dayLog.setSleep(request.sleepQuality(), request.sleepHours());
            }
        } else if (request.sleepQuality() != null || request.sleepHours() != null) {
            throw new IllegalArgumentException("Dados de sono nao podem ser enviados neste contexto");
        }

        if (Boolean.TRUE.equals(request.ranToday()) && !dayLog.isRanToday()) {
            dayLog.setRanToday(true);
        }

        sleepQualitySnapshot = dayLog.getSleepQuality();
        sleepHoursSnapshot = dayLog.getSleepHours();

        Batch batch = resolveBatch(request);

        MeasurementResult measurement = serialService.getMeasurementForSave(true);

        UseEntry entry = UseEntry.create(
                dayLog,
                mood,
                mood.getLabel(),
                reason,
                reason.getLabel(),
                normalizedReasonText,
                firstOfDay,
                Boolean.TRUE.equals(request.awayLong()),
                ranTodayFinal,
                sleepQualitySnapshot,
                sleepHoursSnapshot,
                batch,
                batch.getLabel(),
                measurement.measureKind(),
                measurement.stableOk(),
                measurement.stableSpanG(),
                measurement.weightG(),
                measurement.serialLine(),
                measurement.serialPort(),
                measurement.serialBaud(),
                APP_VERSION,
                normalize(request.notes())
        );

        UseEntry saved = useEntryRepository.save(entry);
        if (firstOfDay) {
            dayLog.markFirstUse();
        }
        dayLogRepository.save(dayLog);

        return new CreateEntryResponse(
                saved.getId(),
                saved.getCreatedAt(),
                "Registro salvo com sucesso",
                new MeasureStatusResponse(
                        measurement.measureKind(),
                        measurement.stableOk(),
                        measurement.publicLabel()
                )
        );
    }

    private Batch resolveBatch(CreateEntryRequest request) {
        String batchMode = normalize(request.batchMode());
        if (batchMode == null) {
            throw new IllegalArgumentException("batch_mode e obrigatorio");
        }

        if ("existing".equals(batchMode.toLowerCase(Locale.ROOT))) {
            if (request.batchId() == null) {
                throw new IllegalArgumentException("batch_id e obrigatorio quando batch_mode = existing");
            }
            return batchRepository.findById(request.batchId())
                    .filter(Batch::isActive)
                    .orElseThrow(() -> new IllegalArgumentException("Lote invalido"));
        }

        if ("new".equals(batchMode.toLowerCase(Locale.ROOT))) {
            NewBatchRequest newBatch = request.newBatch();
            if (newBatch == null) {
                throw new IllegalArgumentException("new_batch e obrigatorio quando batch_mode = new");
            }
            return batchRepository.save(createBatch(newBatch));
        }

        throw new IllegalArgumentException("batch_mode deve ser existing ou new");
    }

    private Batch createBatch(NewBatchRequest request) {
        String label = normalize(request.label());
        if (label == null) {
            throw new IllegalArgumentException("label do novo lote e obrigatorio");
        }
        if (request.startDate() == null) {
            throw new IllegalArgumentException("start_date do novo lote e obrigatorio");
        }
        if (request.grams() == null || request.grams().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("grams do novo lote deve ser maior que zero");
        }
        if (request.batchValue() == null || request.batchValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("batch_value do novo lote deve ser maior que zero");
        }

        BatchTextureOption textureOption = null;
        String textureSnapshot = null;
        if (request.textureOptionId() != null) {
            textureOption = batchTextureOptionRepository.findById(request.textureOptionId())
                    .filter(BatchTextureOption::isActive)
                    .orElseThrow(() -> new IllegalArgumentException("Textura invalida"));
            textureSnapshot = textureOption.getLabel();
        }

        BatchSmellOption smellOption = null;
        String smellSnapshot = null;
        if (request.smellOptionId() != null) {
            smellOption = batchSmellOptionRepository.findById(request.smellOptionId())
                    .filter(BatchSmellOption::isActive)
                    .orElseThrow(() -> new IllegalArgumentException("Cheiro invalido"));
            smellSnapshot = smellOption.getLabel();
        }

        BigDecimal valuePerGram = request.batchValue()
                .divide(request.grams(), 6, RoundingMode.HALF_UP);

        return Batch.create(
                label,
                request.startDate(),
                request.grams(),
                request.batchValue(),
                valuePerGram,
                textureOption,
                textureSnapshot,
                smellOption,
                smellSnapshot,
                normalize(request.notes())
        );
    }

    private void validateSleep(Short sleepQuality, Short sleepHours) {
        if (sleepQuality == null || sleepQuality < 1 || sleepQuality > 5) {
            throw new IllegalArgumentException("sleep_quality deve estar entre 1 e 5");
        }
        if (sleepHours == null || sleepHours < 0 || sleepHours > 24) {
            throw new IllegalArgumentException("sleep_hours deve estar entre 0 e 24");
        }
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        return normalized.isBlank() ? null : normalized;
    }
}
