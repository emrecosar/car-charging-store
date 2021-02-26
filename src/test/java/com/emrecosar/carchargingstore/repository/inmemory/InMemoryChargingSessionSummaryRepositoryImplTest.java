package com.emrecosar.carchargingstore.repository.inmemory;

import com.emrecosar.carchargingstore.api.models.ChargingSessionsSummary;
import com.emrecosar.carchargingstore.repository.ChargingSessionSummaryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

public class InMemoryChargingSessionSummaryRepositoryImplTest {

    private ChargingSessionSummaryRepository chargingSessionSummaryRepository;

    @BeforeEach
    public void setup() {
        chargingSessionSummaryRepository = new InMemoryChargingSessionSummaryRepositoryImpl();
        ReflectionTestUtils.setField(chargingSessionSummaryRepository, "summaryPeriodMinutes", 1L);
    }

    @Test
    public void givenRepository_whenProcessTransactions_thenReturnSummary() throws InterruptedException {

        // given
        chargingSessionSummaryRepository.started(LocalDateTime.now());
        chargingSessionSummaryRepository.started(LocalDateTime.now());
        chargingSessionSummaryRepository.stopped(LocalDateTime.now());
        // wait 1 minute
        Thread.sleep(1000 * 60);
        chargingSessionSummaryRepository.stopped(LocalDateTime.now());

        // when
        ChargingSessionsSummary chargingSessionsSummary = chargingSessionSummaryRepository.getSummary();

        // then
        Assertions.assertEquals(0, chargingSessionsSummary.getStartedCount());
        Assertions.assertEquals(1, chargingSessionsSummary.getStoppedCount());
        Assertions.assertEquals(1, chargingSessionsSummary.getTotalCount());

    }


}