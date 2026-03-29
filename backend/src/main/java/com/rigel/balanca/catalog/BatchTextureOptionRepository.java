package com.rigel.balanca.catalog;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchTextureOptionRepository extends JpaRepository<BatchTextureOption, UUID> {
    List<BatchTextureOption> findByActiveTrueOrderBySortOrderAscLabelAsc();
    List<BatchTextureOption> findAllByOrderBySortOrderAscLabelAsc();
}
