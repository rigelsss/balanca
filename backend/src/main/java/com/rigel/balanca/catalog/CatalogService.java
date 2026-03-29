package com.rigel.balanca.catalog;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CatalogService {

    private final MoodOptionRepository moodOptionRepository;
    private final ReasonOptionRepository reasonOptionRepository;
    private final BatchTextureOptionRepository batchTextureOptionRepository;
    private final BatchSmellOptionRepository batchSmellOptionRepository;

    public CatalogService(MoodOptionRepository moodOptionRepository,
                          ReasonOptionRepository reasonOptionRepository,
                          BatchTextureOptionRepository batchTextureOptionRepository,
                          BatchSmellOptionRepository batchSmellOptionRepository) {
        this.moodOptionRepository = moodOptionRepository;
        this.reasonOptionRepository = reasonOptionRepository;
        this.batchTextureOptionRepository = batchTextureOptionRepository;
        this.batchSmellOptionRepository = batchSmellOptionRepository;
    }

    public List<CatalogOptionView> listMoodOptions() {
        return moodOptionRepository.findByActiveTrueOrderBySortOrderAscLabelAsc().stream()
                .map(option -> new CatalogOptionView(option.getId(), option.getLabel(), option.getSortOrder(), option.isActive()))
                .toList();
    }

    public List<CatalogOptionView> listAllMoodOptions() {
        return moodOptionRepository.findAllByOrderBySortOrderAscLabelAsc().stream()
                .map(option -> new CatalogOptionView(option.getId(), option.getLabel(), option.getSortOrder(), option.isActive()))
                .toList();
    }

    public List<ReasonOptionView> listReasonOptions() {
        return reasonOptionRepository.findByActiveTrueOrderBySortOrderAscLabelAsc().stream()
                .map(option -> new ReasonOptionView(
                        option.getId(),
                        option.getLabel(),
                        option.getSortOrder(),
                        option.isActive(),
                        option.isRequiresText()
                ))
                .toList();
    }

    public List<ReasonOptionView> listAllReasonOptions() {
        return reasonOptionRepository.findAllByOrderBySortOrderAscLabelAsc().stream()
                .map(option -> new ReasonOptionView(
                        option.getId(),
                        option.getLabel(),
                        option.getSortOrder(),
                        option.isActive(),
                        option.isRequiresText()
                ))
                .toList();
    }

    public List<CatalogOptionView> listBatchTextureOptions() {
        return batchTextureOptionRepository.findByActiveTrueOrderBySortOrderAscLabelAsc().stream()
                .map(option -> new CatalogOptionView(option.getId(), option.getLabel(), option.getSortOrder(), option.isActive()))
                .toList();
    }

    public List<CatalogOptionView> listAllBatchTextureOptions() {
        return batchTextureOptionRepository.findAllByOrderBySortOrderAscLabelAsc().stream()
                .map(option -> new CatalogOptionView(option.getId(), option.getLabel(), option.getSortOrder(), option.isActive()))
                .toList();
    }

    public List<CatalogOptionView> listBatchSmellOptions() {
        return batchSmellOptionRepository.findByActiveTrueOrderBySortOrderAscLabelAsc().stream()
                .map(option -> new CatalogOptionView(option.getId(), option.getLabel(), option.getSortOrder(), option.isActive()))
                .toList();
    }

    public List<CatalogOptionView> listAllBatchSmellOptions() {
        return batchSmellOptionRepository.findAllByOrderBySortOrderAscLabelAsc().stream()
                .map(option -> new CatalogOptionView(option.getId(), option.getLabel(), option.getSortOrder(), option.isActive()))
                .toList();
    }

    @Transactional
    public CatalogOptionView createMood(CatalogUpsertRequest request) {
        MoodOption saved = moodOptionRepository.save(MoodOption.create(
                normalizeLabel(request.label()),
                defaultActive(request.active()),
                defaultSortOrder(request.sortOrder())
        ));
        return new CatalogOptionView(saved.getId(), saved.getLabel(), saved.getSortOrder(), saved.isActive());
    }

    @Transactional
    public CatalogOptionView updateMood(UUID id, CatalogUpsertRequest request) {
        MoodOption option = moodOptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Humor nao encontrado"));
        option.update(
                normalizeLabel(request.label()),
                defaultActive(request.active()),
                defaultSortOrder(request.sortOrder())
        );
        MoodOption saved = moodOptionRepository.save(option);
        return new CatalogOptionView(saved.getId(), saved.getLabel(), saved.getSortOrder(), saved.isActive());
    }

    @Transactional
    public ReasonOptionView createReason(CatalogUpsertRequest request) {
        ReasonOption saved = reasonOptionRepository.save(ReasonOption.create(
                normalizeLabel(request.label()),
                Boolean.TRUE.equals(request.requiresText()),
                defaultActive(request.active()),
                defaultSortOrder(request.sortOrder())
        ));
        return new ReasonOptionView(saved.getId(), saved.getLabel(), saved.getSortOrder(), saved.isActive(), saved.isRequiresText());
    }

    @Transactional
    public ReasonOptionView updateReason(UUID id, CatalogUpsertRequest request) {
        ReasonOption option = reasonOptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Motivo nao encontrado"));
        option.update(
                normalizeLabel(request.label()),
                Boolean.TRUE.equals(request.requiresText()),
                defaultActive(request.active()),
                defaultSortOrder(request.sortOrder())
        );
        ReasonOption saved = reasonOptionRepository.save(option);
        return new ReasonOptionView(saved.getId(), saved.getLabel(), saved.getSortOrder(), saved.isActive(), saved.isRequiresText());
    }

    @Transactional
    public CatalogOptionView createBatchTexture(CatalogUpsertRequest request) {
        BatchTextureOption saved = batchTextureOptionRepository.save(BatchTextureOption.create(
                normalizeLabel(request.label()),
                defaultActive(request.active()),
                defaultSortOrder(request.sortOrder())
        ));
        return new CatalogOptionView(saved.getId(), saved.getLabel(), saved.getSortOrder(), saved.isActive());
    }

    @Transactional
    public CatalogOptionView updateBatchTexture(UUID id, CatalogUpsertRequest request) {
        BatchTextureOption option = batchTextureOptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Textura nao encontrada"));
        option.update(
                normalizeLabel(request.label()),
                defaultActive(request.active()),
                defaultSortOrder(request.sortOrder())
        );
        BatchTextureOption saved = batchTextureOptionRepository.save(option);
        return new CatalogOptionView(saved.getId(), saved.getLabel(), saved.getSortOrder(), saved.isActive());
    }

    @Transactional
    public CatalogOptionView createBatchSmell(CatalogUpsertRequest request) {
        BatchSmellOption saved = batchSmellOptionRepository.save(BatchSmellOption.create(
                normalizeLabel(request.label()),
                defaultActive(request.active()),
                defaultSortOrder(request.sortOrder())
        ));
        return new CatalogOptionView(saved.getId(), saved.getLabel(), saved.getSortOrder(), saved.isActive());
    }

    @Transactional
    public CatalogOptionView updateBatchSmell(UUID id, CatalogUpsertRequest request) {
        BatchSmellOption option = batchSmellOptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cheiro nao encontrado"));
        option.update(
                normalizeLabel(request.label()),
                defaultActive(request.active()),
                defaultSortOrder(request.sortOrder())
        );
        BatchSmellOption saved = batchSmellOptionRepository.save(option);
        return new CatalogOptionView(saved.getId(), saved.getLabel(), saved.getSortOrder(), saved.isActive());
    }

    private String normalizeLabel(String label) {
        String normalized = label == null ? "" : label.trim();
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("label e obrigatorio");
        }
        return normalized;
    }

    private boolean defaultActive(Boolean active) {
        return active == null || active;
    }

    private int defaultSortOrder(Integer sortOrder) {
        return sortOrder == null ? 0 : sortOrder;
    }
}
