package com.rigel.balanca.catalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "mood_options")
public class MoodOption {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String label;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public UUID getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public boolean isActive() {
        return active;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public static MoodOption create(String label, boolean active, int sortOrder) {
        MoodOption option = new MoodOption();
        option.id = UUID.randomUUID();
        option.label = label;
        option.active = active;
        option.sortOrder = sortOrder;
        option.createdAt = OffsetDateTime.now();
        option.updatedAt = option.createdAt;
        return option;
    }

    public void update(String label, boolean active, int sortOrder) {
        this.label = label;
        this.active = active;
        this.sortOrder = sortOrder;
        this.updatedAt = OffsetDateTime.now();
    }
}
