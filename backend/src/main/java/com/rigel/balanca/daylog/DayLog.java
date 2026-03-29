package com.rigel.balanca.daylog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "day_logs")
public class DayLog {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private LocalDate day;

    @Column(name = "has_first_use", nullable = false)
    private boolean hasFirstUse;

    @Column(name = "ran_today", nullable = false)
    private boolean ranToday;

    @Column(name = "sleep_quality")
    private Short sleepQuality;

    @Column(name = "sleep_hours")
    private Short sleepHours;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public static DayLog create(LocalDate day) {
        DayLog log = new DayLog();
        log.id = UUID.randomUUID();
        log.day = day;
        log.hasFirstUse = false;
        log.ranToday = false;
        log.createdAt = OffsetDateTime.now();
        log.updatedAt = log.createdAt;
        return log;
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getDay() {
        return day;
    }

    public boolean isHasFirstUse() {
        return hasFirstUse;
    }

    public boolean isRanToday() {
        return ranToday;
    }

    public Short getSleepQuality() {
        return sleepQuality;
    }

    public Short getSleepHours() {
        return sleepHours;
    }

    public void markFirstUse() {
        this.hasFirstUse = true;
        this.updatedAt = OffsetDateTime.now();
    }

    public void setRanToday(boolean ranToday) {
        this.ranToday = ranToday;
        this.updatedAt = OffsetDateTime.now();
    }

    public void setSleep(Short sleepQuality, Short sleepHours) {
        this.sleepQuality = sleepQuality;
        this.sleepHours = sleepHours;
        this.updatedAt = OffsetDateTime.now();
    }
}
