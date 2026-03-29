package com.rigel.balanca.entry;

import com.rigel.balanca.batch.BatchService;
import com.rigel.balanca.catalog.CatalogService;
import com.rigel.balanca.daylog.DayLog;
import com.rigel.balanca.daylog.DayLogRepository;
import com.rigel.balanca.serial.SerialService;
import com.rigel.balanca.serial.SerialSnapshot;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterContextService {

    private static final String FIRST_REASON_PROMPT = "O que esta te levando a usar agora?";
    private static final String NEXT_REASON_PROMPT = "O que esta te levando a usar agora? Por que mais?";

    private final DayLogRepository dayLogRepository;
    private final UseEntryRepository useEntryRepository;
    private final CatalogService catalogService;
    private final BatchService batchService;
    private final SerialService serialService;

    public RegisterContextService(DayLogRepository dayLogRepository,
                                  UseEntryRepository useEntryRepository,
                                  CatalogService catalogService,
                                  BatchService batchService,
                                  SerialService serialService) {
        this.dayLogRepository = dayLogRepository;
        this.useEntryRepository = useEntryRepository;
        this.catalogService = catalogService;
        this.batchService = batchService;
        this.serialService = serialService;
    }

    @Transactional
    public RegisterContextResponse getContext() {
        LocalDate today = LocalDate.now();
        DayLog dayLog = dayLogRepository.findByDay(today)
                .orElseGet(() -> dayLogRepository.save(DayLog.create(today)));

        boolean firstUseAlreadySaved = dayLog.isHasFirstUse() || useEntryRepository.existsByDayLog_DayAndFirstOfDayTrue(today);
        boolean firstUseAvailable = !firstUseAlreadySaved;
        boolean sleepAlreadyRecorded = dayLog.getSleepQuality() != null || dayLog.getSleepHours() != null;
        boolean showSleepFields = firstUseAvailable && !sleepAlreadyRecorded;
        boolean ranTodayLocked = dayLog.isRanToday();

        RegisterCatalogsResponse catalogs = new RegisterCatalogsResponse(
                catalogService.listMoodOptions(),
                catalogService.listReasonOptions(),
                catalogService.listBatchTextureOptions(),
                catalogService.listBatchSmellOptions(),
                batchService.listActiveBatches()
        );

        SerialSnapshot serialSnapshot = serialService.getSafeSnapshot();
        SerialStatusResponse serial = new SerialStatusResponse(
                serialSnapshot.connected(),
                serialSnapshot.port(),
                serialSnapshot.baud(),
                serialSnapshot.status(),
                serialSnapshot.lastMeasureKind(),
                serialSnapshot.stableOk(),
                serialSnapshot.lastMessage()
        );

        return new RegisterContextResponse(
                today,
                firstUseAvailable,
                firstUseAvailable,
                showSleepFields,
                sleepAlreadyRecorded,
                ranTodayLocked,
                dayLog.isRanToday(),
                firstUseAvailable ? FIRST_REASON_PROMPT : NEXT_REASON_PROMPT,
                true,
                catalogs,
                serial
        );
    }
}
