package com.rigel.balanca.batch;

import com.rigel.balanca.catalog.BatchSmellOption;
import com.rigel.balanca.catalog.BatchTextureOption;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "batches")
public class Batch {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String label;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column
    private BigDecimal grams;

    @Column(name = "batch_value")
    private BigDecimal batchValue;

    @Column(name = "value_per_gram")
    private BigDecimal valuePerGram;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "texture_option_id")
    private BatchTextureOption textureOption;

    @Column(name = "texture_label_snapshot")
    private String textureLabelSnapshot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "smell_option_id")
    private BatchSmellOption smellOption;

    @Column(name = "smell_label_snapshot")
    private String smellLabelSnapshot;

    @Column
    private String notes;

    @Column(nullable = false)
    private boolean active;

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

    public LocalDate getStartDate() {
        return startDate;
    }

    public BigDecimal getGrams() {
        return grams;
    }

    public BigDecimal getBatchValue() {
        return batchValue;
    }

    public BigDecimal getValuePerGram() {
        return valuePerGram;
    }

    public String getTextureLabelSnapshot() {
        return textureLabelSnapshot;
    }

    public BatchTextureOption getTextureOption() {
        return textureOption;
    }

    public String getSmellLabelSnapshot() {
        return smellLabelSnapshot;
    }

    public BatchSmellOption getSmellOption() {
        return smellOption;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isActive() {
        return active;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public static Batch create(String label,
                               LocalDate startDate,
                               BigDecimal grams,
                               BigDecimal batchValue,
                               BigDecimal valuePerGram,
                               BatchTextureOption textureOption,
                               String textureLabelSnapshot,
                               BatchSmellOption smellOption,
                               String smellLabelSnapshot,
                               String notes) {
        Batch batch = new Batch();
        batch.id = UUID.randomUUID();
        batch.label = label;
        batch.startDate = startDate;
        batch.grams = grams;
        batch.batchValue = batchValue;
        batch.valuePerGram = valuePerGram;
        batch.textureOption = textureOption;
        batch.textureLabelSnapshot = textureLabelSnapshot;
        batch.smellOption = smellOption;
        batch.smellLabelSnapshot = smellLabelSnapshot;
        batch.notes = notes;
        batch.active = true;
        batch.createdAt = OffsetDateTime.now();
        batch.updatedAt = batch.createdAt;
        return batch;
    }

    public void update(String label,
                       LocalDate startDate,
                       BigDecimal grams,
                       BigDecimal batchValue,
                       BigDecimal valuePerGram,
                       BatchTextureOption textureOption,
                       String textureLabelSnapshot,
                       BatchSmellOption smellOption,
                       String smellLabelSnapshot,
                       String notes,
                       boolean active) {
        this.label = label;
        this.startDate = startDate;
        this.grams = grams;
        this.batchValue = batchValue;
        this.valuePerGram = valuePerGram;
        this.textureOption = textureOption;
        this.textureLabelSnapshot = textureLabelSnapshot;
        this.smellOption = smellOption;
        this.smellLabelSnapshot = smellLabelSnapshot;
        this.notes = notes;
        this.active = active;
        this.updatedAt = OffsetDateTime.now();
    }
}
