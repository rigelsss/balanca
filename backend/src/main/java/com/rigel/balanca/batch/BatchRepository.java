package com.rigel.balanca.batch;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchRepository extends JpaRepository<Batch, UUID> {
    List<Batch> findByActiveTrueOrderByCreatedAtDescLabelAsc();
    List<Batch> findAllByOrderByCreatedAtDescLabelAsc();
}
