package com.rigel.balanca.entry;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UseEntryRepository extends JpaRepository<UseEntry, UUID>, JpaSpecificationExecutor<UseEntry> {
    boolean existsByDayLog_DayAndFirstOfDayTrue(java.time.LocalDate day);
}
