package com.rigel.balanca.batch;

import com.rigel.balanca.catalog.BatchSmellOptionRepository;
import com.rigel.balanca.catalog.BatchTextureOptionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BatchServiceTest {

    @Test
    void shouldCalculateValuePerGramWhenCreatingBatch() {
        BatchRepository batchRepository = mock(BatchRepository.class);
        BatchTextureOptionRepository textureRepository = mock(BatchTextureOptionRepository.class);
        BatchSmellOptionRepository smellRepository = mock(BatchSmellOptionRepository.class);

        when(batchRepository.save(any(Batch.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BatchService service = new BatchService(batchRepository, textureRepository, smellRepository);
        BatchResponse response = service.createBatch(new BatchUpsertRequest(
                "Lote teste",
                LocalDate.of(2026, 3, 29),
                new BigDecimal("10"),
                new BigDecimal("25"),
                null,
                null,
                "ok",
                true
        ));

        assertEquals(new BigDecimal("2.500000"), response.valuePerGram());
    }
}
