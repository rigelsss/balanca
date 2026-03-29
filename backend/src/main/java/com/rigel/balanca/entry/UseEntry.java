package com.rigel.balanca.entry;

import com.rigel.balanca.batch.Batch;
import com.rigel.balanca.catalog.MoodOption;
import com.rigel.balanca.catalog.ReasonOption;
import com.rigel.balanca.daylog.DayLog;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "use_entries")
public class UseEntry {

    @Id
    private UUID id;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "day_log_id", nullable = false)
    private DayLog dayLog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mood_option_id")
    private MoodOption moodOption;

    @Column(name = "mood_label_snapshot")
    private String moodLabelSnapshot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reason_option_id")
    private ReasonOption reasonOption;

    @Column(name = "reason_label_snapshot")
    private String reasonLabelSnapshot;

    @Column(name = "reason_text")
    private String reasonText;

    @Column(name = "first_of_day", nullable = false)
    private boolean firstOfDay;

    @Column(name = "away_long", nullable = false)
    private boolean awayLong;

    @Column(name = "ran_today_snapshot", nullable = false)
    private boolean ranTodaySnapshot;

    @Column(name = "sleep_quality_snapshot")
    private Short sleepQualitySnapshot;

    @Column(name = "sleep_hours_snapshot")
    private Short sleepHoursSnapshot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @Column(name = "batch_label_snapshot")
    private String batchLabelSnapshot;

    @Column(name = "measure_kind", nullable = false)
    private String measureKind;

    @Column(name = "stable_ok")
    private Boolean stableOk;

    @Column(name = "stable_span_g")
    private BigDecimal stableSpanG;

    @Column(name = "weight_g", nullable = false)
    private BigDecimal weightG;

    @Column(name = "serial_line")
    private String serialLine;

    @Column(name = "serial_port")
    private String serialPort;

    @Column(name = "serial_baud")
    private Integer serialBaud;

    @Column(name = "app_version")
    private String appVersion;

    @Column(name = "notes")
    private String notes;

    public static UseEntry create(DayLog dayLog,
                                  MoodOption moodOption,
                                  String moodLabelSnapshot,
                                  ReasonOption reasonOption,
                                  String reasonLabelSnapshot,
                                  String reasonText,
                                  boolean firstOfDay,
                                  boolean awayLong,
                                  boolean ranTodaySnapshot,
                                  Short sleepQualitySnapshot,
                                  Short sleepHoursSnapshot,
                                  Batch batch,
                                  String batchLabelSnapshot,
                                  String measureKind,
                                  Boolean stableOk,
                                  BigDecimal stableSpanG,
                                  BigDecimal weightG,
                                  String serialLine,
                                  String serialPort,
                                  Integer serialBaud,
                                  String appVersion,
                                  String notes) {
        UseEntry entry = new UseEntry();
        entry.id = UUID.randomUUID();
        entry.createdAt = OffsetDateTime.now();
        entry.dayLog = dayLog;
        entry.moodOption = moodOption;
        entry.moodLabelSnapshot = moodLabelSnapshot;
        entry.reasonOption = reasonOption;
        entry.reasonLabelSnapshot = reasonLabelSnapshot;
        entry.reasonText = reasonText;
        entry.firstOfDay = firstOfDay;
        entry.awayLong = awayLong;
        entry.ranTodaySnapshot = ranTodaySnapshot;
        entry.sleepQualitySnapshot = sleepQualitySnapshot;
        entry.sleepHoursSnapshot = sleepHoursSnapshot;
        entry.batch = batch;
        entry.batchLabelSnapshot = batchLabelSnapshot;
        entry.measureKind = measureKind;
        entry.stableOk = stableOk;
        entry.stableSpanG = stableSpanG;
        entry.weightG = weightG;
        entry.serialLine = serialLine;
        entry.serialPort = serialPort;
        entry.serialBaud = serialBaud;
        entry.appVersion = appVersion;
        entry.notes = notes;
        return entry;
    }

    public UUID getId() {
        return id;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public String getMoodLabelSnapshot() {
        return moodLabelSnapshot;
    }

    public String getReasonLabelSnapshot() {
        return reasonLabelSnapshot;
    }

    public String getReasonText() {
        return reasonText;
    }

    public boolean isFirstOfDay() {
        return firstOfDay;
    }

    public boolean isAwayLong() {
        return awayLong;
    }

    public boolean isRanTodaySnapshot() {
        return ranTodaySnapshot;
    }

    public Short getSleepQualitySnapshot() {
        return sleepQualitySnapshot;
    }

    public Short getSleepHoursSnapshot() {
        return sleepHoursSnapshot;
    }

    public String getBatchLabelSnapshot() {
        return batchLabelSnapshot;
    }

    public String getMeasureKind() {
        return measureKind;
    }

    public Boolean getStableOk() {
        return stableOk;
    }

    public String getNotes() {
        return notes;
    }
}
