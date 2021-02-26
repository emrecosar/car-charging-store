package com.emrecosar.carchargingstore.api.models;

/**
 * Charging Sessions Summary model
 */
public class ChargingSessionsSummary {

    private int totalCount;

    private int startedCount;

    private int stoppedCount;

    public ChargingSessionsSummary(int totalCount, int startedCount, int stoppedCount) {
        this.totalCount = totalCount;
        this.startedCount = startedCount;
        this.stoppedCount = stoppedCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getStartedCount() {
        return startedCount;
    }

    public void setStartedCount(int startedCount) {
        this.startedCount = startedCount;
    }

    public int getStoppedCount() {
        return stoppedCount;
    }

    public void setStoppedCount(int stoppedCount) {
        this.stoppedCount = stoppedCount;
    }
}
