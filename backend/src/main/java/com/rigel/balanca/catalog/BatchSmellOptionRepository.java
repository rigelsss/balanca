package com.rigel.balanca.catalog;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchSmellOptionRepository extends JpaRepository<BatchSmellOption, UUID> {
    List<BatchSmellOption> findByActiveTrueOrderBySortOrderAscLabelAsc();
    List<BatchSmellOption> findAllByOrderBySortOrderAscLabelAsc();
}
