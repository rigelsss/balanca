package com.rigel.balanca.catalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "reason_options")
public class ReasonOption {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String label;

    @Column(name = "requires_text", nullable = false)
    private boolean requiresText;

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

    public boolean isRequiresText() {
        return requiresText;
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

    public static ReasonOption create(String label, boolean requiresText, boolean active, int sortOrder) {
        ReasonOption option = new ReasonOption();
        option.id = UUID.randomUUID();
        option.label = label;
        option.requiresText = requiresText;
        option.active = active;
        option.sortOrder = sortOrder;
        option.createdAt = OffsetDateTime.now();
        option.updatedAt = option.createdAt;
        return option;
    }

    public void update(String label, boolean requiresText, boolean active, int sortOrder) {
        this.label = label;
        this.requiresText = requiresText;
        this.active = active;
        this.sortOrder = sortOrder;
        this.updatedAt = OffsetDateTime.now();
    }
}
