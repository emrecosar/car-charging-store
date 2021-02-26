package com.emrecosar.carchargingstore.repository;

import com.emrecosar.carchargingstore.api.models.ChargingSessionsSummary;

import java.time.LocalDateTime;

/**
 * Charging Session Summary Repository
 * <p>
 * This repository hold the statistics of started and stopped charging stations
 */
public interface ChargingSessionSummaryRepository {

    /**
     * Count started station sessions
     *
     * @param started started local date time
     */
    void started(LocalDateTime started);

    /**
     * Count stopped station sessions
     *
     * @param stopped stopped local date time
     */
    void stopped(LocalDateTime stopped);

    /**
     * Returns a summary on started and stopped station sessions
     */
    ChargingSessionsSummary getSummary();
}
