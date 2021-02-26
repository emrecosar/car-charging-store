package com.emrecosar.carchargingstore.service;

import com.emrecosar.carchargingstore.api.models.ChargingSession;
import com.emrecosar.carchargingstore.api.models.StatusEnum;
import com.emrecosar.carchargingstore.repository.ChargingSessionRepository;
import com.emrecosar.carchargingstore.repository.ChargingSessionSummaryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ChargingSessionServiceImplTest {

    @Mock
    private ChargingSessionRepository chargingSessionRepository;

    @Mock
    private ChargingSessionSummaryRepository chargingSessionSummaryRepository;

    private ChargingSessionServiceImpl chargingSessionService;

    @BeforeEach
    public void setup() {
        chargingSessionService = new ChargingSessionServiceImpl(chargingSessionRepository,
                chargingSessionSummaryRepository);
    }

    @Test
    public void givenStationId_whenCreateChargingSession_thenReturnNewChargingSession() {

        // given
        ChargingSession expectedChargingSession = createChargingSession();
        Mockito.doReturn(true)
                .when(chargingSessionRepository).createChargingSession(ArgumentMatchers.any(ChargingSession.class));

        // when
        ChargingSession actualChargingSession =
                chargingSessionService.createChargingSession(expectedChargingSession.getStationId());

        Assertions.assertEquals(expectedChargingSession.getStationId(), actualChargingSession.getStationId());
        Assertions.assertEquals(StatusEnum.IN_PROGRESS, actualChargingSession.getStatus());

        Mockito.verify(chargingSessionRepository).createChargingSession(ArgumentMatchers.any(ChargingSession.class));
        Mockito.verify(chargingSessionSummaryRepository).started(ArgumentMatchers.eq(actualChargingSession.getStartedAt()));
    }

    @Test
    public void givenStationId_whenUpdateChargingSession_thenReturnStoppedChargingSession() {

        // given
        ChargingSession expectedChargingSession = createChargingSession();
        Mockito.doReturn(Optional.ofNullable(expectedChargingSession))
                .when(chargingSessionRepository).getChargingSession(ArgumentMatchers.eq(expectedChargingSession.getStationId()));
        Mockito.doNothing()
                .when(chargingSessionRepository).updateChargingSession(ArgumentMatchers.any(ChargingSession.class));

        // when
        ChargingSession actualChargingSession =
                chargingSessionService.stopChargingSession(expectedChargingSession.getStationId());

        Assertions.assertEquals(expectedChargingSession.getStationId(), actualChargingSession.getStationId());
        Assertions.assertEquals(StatusEnum.FINISHED, actualChargingSession.getStatus());
        Mockito.verify(chargingSessionRepository).getChargingSession(ArgumentMatchers.eq(expectedChargingSession.getStationId()));
        Mockito.verify(chargingSessionRepository).updateChargingSession(ArgumentMatchers.any(ChargingSession.class));
        Mockito.verify(chargingSessionSummaryRepository).stopped(ArgumentMatchers.eq(actualChargingSession.getStoppedAt()));
    }

    private ChargingSession createChargingSession() {
        return new ChargingSession(UUID.randomUUID(), UUID.randomUUID().toString(),
                LocalDateTime.now(), null, StatusEnum.IN_PROGRESS);
    }


}