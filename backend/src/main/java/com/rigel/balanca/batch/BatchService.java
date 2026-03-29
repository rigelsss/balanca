package com.rigel.balanca.batch;

import com.rigel.balanca.catalog.BatchSmellOption;
import com.rigel.balanca.catalog.BatchSmellOptionRepository;
import com.rigel.balanca.catalog.BatchTextureOption;
import com.rigel.balanca.catalog.BatchTextureOptionRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BatchService {

    private final BatchRepository batchRepository;
    private final BatchTextureOptionRepository batchTextureOptionRepository;
    private final BatchSmellOptionRepository batchSmellOptionRepository;

    public BatchService(BatchRepository batchRepository,
                        BatchTextureOptionRepository batchTextureOptionRepository,
                        BatchSmellOptionRepository batchSmellOptionRepository) {
        this.batchRepository = batchRepository;
        this.batchTextureOptionRepository = batchTextureOptionRepository;
        this.batchSmellOptionRepository = batchSmellOptionRepository;
    }

    public List<BatchOptionView> listActiveBatches() {
        return batchRepository.findByActiveTrueOrderByCreatedAtDescLabelAsc().stream()
                .map(batch -> new BatchOptionView(batch.getId(), batch.getLabel()))
                .toList();
    }

    public List<BatchResponse> listAllBatches() {
        return batchRepository.findAllByOrderByCreatedAtDescLabelAsc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public BatchResponse createBatch(BatchUpsertRequest request) {
        BatchTextureOption textureOption = resolveTexture(request.textureOptionId());
        BatchSmellOption smellOption = resolveSmell(request.smellOptionId());

        Batch batch = Batch.create(
                normalizeLabel(request.label()),
                request.startDate(),
                validatePositive(request.grams(), "grams"),
                validatePositive(request.batchValue(), "batch_value"),
                calculateValuePerGram(request.batchValue(), request.grams()),
                textureOption,
                textureOption != null ? textureOption.getLabel() : null,
                smellOption,
                smellOption != null ? smellOption.getLabel() : null,
                normalizeOptional(request.notes())
        );

        return toResponse(batchRepository.save(batch));
    }

    @Transactional
    public BatchResponse updateBatch(java.util.UUID id, BatchUpsertRequest request) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lote nao encontrado"));

        BatchTextureOption textureOption = resolveTexture(request.textureOptionId());
        BatchSmellOption smellOption = resolveSmell(request.smellOptionId());

        BigDecimal grams = validatePositive(request.grams(), "grams");
        BigDecimal batchValue = validatePositive(request.batchValue(), "batch_value");

        batch.update(
                normalizeLabel(request.label()),
                request.startDate(),
                grams,
                batchValue,
                calculateValuePerGram(batchValue, grams),
                textureOption,
                textureOption != null ? textureOption.getLabel() : null,
                smellOption,
                smellOption != null ? smellOption.getLabel() : null,
                normalizeOptional(request.notes()),
                request.active() == null || request.active()
        );

        return toResponse(batchRepository.save(batch));
    }

    private BatchResponse toResponse(Batch batch) {
        return new BatchResponse(
                batch.getId(),
                batch.getLabel(),
                batch.getStartDate(),
                batch.getGrams(),
                batch.getBatchValue(),
                batch.getValuePerGram(),
                batch.getTextureOption() != null ? batch.getTextureOption().getId() : null,
                batch.getTextureLabelSnapshot(),
                batch.getSmellOption() != null ? batch.getSmellOption().getId() : null,
                batch.getSmellLabelSnapshot(),
                batch.getNotes(),
                batch.isActive(),
                batch.getCreatedAt()
        );
    }

    private BatchTextureOption resolveTexture(java.util.UUID id) {
        if (id == null) {
            return null;
        }
        return batchTextureOptionRepository.findById(id)
                .filter(BatchTextureOption::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Textura invalida"));
    }

    private BatchSmellOption resolveSmell(java.util.UUID id) {
        if (id == null) {
            return null;
        }
        return batchSmellOptionRepository.findById(id)
                .filter(BatchSmellOption::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Cheiro invalido"));
    }

    private String normalizeLabel(String label) {
        String normalized = label == null ? "" : label.trim();
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("label e obrigatorio");
        }
        return normalized;
    }

    private String normalizeOptional(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        return normalized.isBlank() ? null : normalized;
    }

    private BigDecimal validatePositive(BigDecimal value, String field) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(field + " deve ser maior que zero");
        }
        return value;
    }

    private BigDecimal calculateValuePerGram(BigDecimal batchValue, BigDecimal grams) {
        return batchValue.divide(grams, 6, RoundingMode.HALF_UP);
    }
}
