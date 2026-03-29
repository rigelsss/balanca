package com.rigel.balanca.entry;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EntryQueryService {

    private final UseEntryRepository useEntryRepository;

    public EntryQueryService(UseEntryRepository useEntryRepository) {
        this.useEntryRepository = useEntryRepository;
    }

    @Transactional(readOnly = true)
    public EntryHistoryResponse listEntries(Integer page,
                                            Integer pageSize,
                                            LocalDate from,
                                            LocalDate to,
                                            UUID moodOptionId,
                                            UUID reasonOptionId,
                                            UUID batchId) {
        int resolvedPage = page == null || page < 1 ? 1 : page;
        int resolvedPageSize = pageSize == null || pageSize < 1 ? 20 : Math.min(pageSize, 100);

        Page<UseEntry> result = useEntryRepository.findAll(
                buildSpec(from, to, moodOptionId, reasonOptionId, batchId),
                PageRequest.of(resolvedPage - 1, resolvedPageSize, Sort.by(Sort.Direction.DESC, "createdAt"))
        );

        List<EntryHistoryItemResponse> items = result.getContent().stream()
                .map(entry -> new EntryHistoryItemResponse(
                        entry.getId(),
                        entry.getCreatedAt(),
                        entry.getMoodLabelSnapshot(),
                        entry.getReasonLabelSnapshot(),
                        entry.getReasonText(),
                        entry.isFirstOfDay(),
                        entry.isAwayLong(),
                        entry.isRanTodaySnapshot(),
                        entry.getSleepQualitySnapshot(),
                        entry.getSleepHoursSnapshot(),
                        entry.getBatchLabelSnapshot(),
                        entry.getMeasureKind(),
                        entry.getStableOk(),
                        entry.getNotes()
                ))
                .toList();

        return new EntryHistoryResponse(items, resolvedPage, resolvedPageSize, result.getTotalElements());
    }

    private Specification<UseEntry> buildSpec(LocalDate from,
                                              LocalDate to,
                                              UUID moodOptionId,
                                              UUID reasonOptionId,
                                              UUID batchId) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (from != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("dayLog").get("day"), from));
            }
            if (to != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("dayLog").get("day"), to));
            }
            if (moodOptionId != null) {
                predicates.add(builder.equal(root.get("moodOption").get("id"), moodOptionId));
            }
            if (reasonOptionId != null) {
                predicates.add(builder.equal(root.get("reasonOption").get("id"), reasonOptionId));
            }
            if (batchId != null) {
                predicates.add(builder.equal(root.get("batch").get("id"), batchId));
            }

            return builder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
