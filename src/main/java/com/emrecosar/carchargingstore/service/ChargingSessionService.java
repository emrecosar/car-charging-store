package com.emrecosar.carchargingstore.service;

import com.emrecosar.carchargingstore.api.models.ChargingSession;
import com.emrecosar.carchargingstore.api.models.ChargingSessionsSummary;

import java.util.List;

/**
 * Charging Session service to process operations
 */
public interface ChargingSessionService {

    /**
     * Create a charging session for the station
     *
     * @param stationId the station id
     * @return {@linkplain ChargingSession} model
     */
    ChargingSession createChargingSession(String stationId);

    /**
     * Stop a charging session for the station
     *
     * @param stationId the station id
     * @return {@linkplain ChargingSession} model
     */
    ChargingSession stopChargingSession(String stationId);

    /**
     * Retrieve all charging sessions
     *
     * @return list of {@linkplain ChargingSession} model
     */
    List<ChargingSession> getAllChargingSessions();

    /**
     * Retrieve a summary of submitted charging sessions
     *
     * @return {@linkplain ChargingSessionsSummary} model
     */
    ChargingSessionsSummary getChargingSessionsSummary();
}
