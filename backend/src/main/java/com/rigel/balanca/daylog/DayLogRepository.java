package com.rigel.balanca.daylog;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayLogRepository extends JpaRepository<DayLog, UUID> {
    Optional<DayLog> findByDay(LocalDate day);
}
