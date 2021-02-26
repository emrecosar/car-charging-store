package com.emrecosar.carchargingstore.service;

import com.emrecosar.carchargingstore.api.exceptions.NotFoundException;
import com.emrecosar.carchargingstore.api.models.ChargingSession;
import com.emrecosar.carchargingstore.api.models.StatusEnum;
import com.emrecosar.carchargingstore.repository.ChargingSessionRepository;
import com.emrecosar.carchargingstore.repository.ChargingSessionSummaryRepository;
import com.emrecosar.carchargingstore.api.exceptions.BadRequestException;
import com.emrecosar.carchargingstore.api.models.ChargingSessionsSummary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ChargingSessionServiceImpl implements ChargingSessionService {

    private final ChargingSessionRepository chargingSessionRepository;

    private final ChargingSessionSummaryRepository chargingSessionSummaryRepository;

    public ChargingSessionServiceImpl(ChargingSessionRepository chargingSessionRepository,
                                      ChargingSessionSummaryRepository chargingSessionSummaryRepository) {
        this.chargingSessionRepository = chargingSessionRepository;
        this.chargingSessionSummaryRepository = chargingSessionSummaryRepository;
    }

    @Override
    public ChargingSession createChargingSession(String stationId) {
        ChargingSession chargingSession = new ChargingSession(UUID.randomUUID(), stationId, getLocalDateTimeNow(),
                null, StatusEnum.IN_PROGRESS);

        if (!chargingSessionRepository.createChargingSession(chargingSession)) {
            throw new BadRequestException(String.format("The station: %s is already exist!", stationId));
        }

        chargingSessionSummaryRepository.started(chargingSession.getStartedAt());

        return chargingSession;
    }

    @Override
    public ChargingSession stopChargingSession(String stationId) {
        ChargingSession chargingSession = chargingSessionRepository.getChargingSession(stationId)
                .orElseThrow(NotFoundException::new);

        if (chargingSession.getStatus() == StatusEnum.FINISHED) {
            throw new BadRequestException("The station is already stopped");
        }

        chargingSession.setStoppedAt(getLocalDateTimeNow());
        chargingSession.setStatus(StatusEnum.FINISHED);
        chargingSessionRepository.updateChargingSession(chargingSession);

        chargingSessionSummaryRepository.stopped(chargingSession.getStoppedAt());

        return chargingSession;
    }

    @Override
    public List<ChargingSession> getAllChargingSessions() {
        List<ChargingSession> chargingSessions = chargingSessionRepository.getAllChargingSessions();
        if (chargingSessions.isEmpty()) {
            throw new NotFoundException();
        }
        return chargingSessions;
    }

    @Override
    public ChargingSessionsSummary getChargingSessionsSummary() {
        return chargingSessionSummaryRepository.getSummary();
    }

    private LocalDateTime getLocalDateTimeNow() {
        return LocalDateTime.now();
    }
}
