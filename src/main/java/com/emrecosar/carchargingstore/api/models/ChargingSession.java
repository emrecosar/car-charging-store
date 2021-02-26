package com.emrecosar.carchargingstore.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Charging Session model
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChargingSession {

    private UUID id;
    private String stationId;
    private LocalDateTime startedAt;
    private LocalDateTime stoppedAt;
    private StatusEnum status;

    public ChargingSession(UUID id, String stationId, LocalDateTime startedAt,
                           LocalDateTime stoppedAt, StatusEnum status) {
        this.id = id;
        this.stationId = stationId;
        this.startedAt = startedAt;
        this.stoppedAt = stoppedAt;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getStoppedAt() {
        return stoppedAt;
    }

    public void setStoppedAt(LocalDateTime stoppedAt) {
        this.stoppedAt = stoppedAt;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }
}
