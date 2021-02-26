package com.emrecosar.carchargingstore.api.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

/**
 * Charging Session Request body object for creating a Charging Session
 */
public class ChargingSessionRequest {

    @JsonIgnore
    public static final String STATION_ID = "stationId";

    @NotBlank(message = "'" + STATION_ID + "' is empty")
    @JsonProperty(STATION_ID)
    private String stationId;

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }
}
