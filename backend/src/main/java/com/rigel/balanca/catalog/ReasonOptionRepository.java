package com.rigel.balanca.catalog;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReasonOptionRepository extends JpaRepository<ReasonOption, UUID> {
    List<ReasonOption> findByActiveTrueOrderBySortOrderAscLabelAsc();
    List<ReasonOption> findAllByOrderBySortOrderAscLabelAsc();
}
