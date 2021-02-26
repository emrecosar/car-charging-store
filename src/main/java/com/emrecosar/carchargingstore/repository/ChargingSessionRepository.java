package com.emrecosar.carchargingstore.repository;

import com.emrecosar.carchargingstore.api.models.ChargingSession;

import java.util.List;
import java.util.Optional;

/**
 * Charging Session Repository to manage charging session data
 */
public interface ChargingSessionRepository {

    /**
     * Fetch a charging station
     *
     * @param stationId the station id
     * @return {@linkplain ChargingSession}
     */
    Optional<ChargingSession> getChargingSession(String stationId);

    /**
     * Fetch all charging stations
     *
     * @return list of {@linkplain ChargingSession}
     */
    List<ChargingSession> getAllChargingSessions();

    /**
     * Persist a charging station
     *
     * @param chargingSession the charging session
     * @return {@linkplain Boolean} result of the operation
     */
    boolean createChargingSession(ChargingSession chargingSession);

    /**
     * Update a charging station
     *
     * @param chargingSession the charging session
     */
    void updateChargingSession(ChargingSession chargingSession);
}
