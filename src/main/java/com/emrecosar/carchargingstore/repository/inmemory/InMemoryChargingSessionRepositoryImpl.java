package com.emrecosar.carchargingstore.repository.inmemory;

import com.emrecosar.carchargingstore.api.models.ChargingSession;
import com.emrecosar.carchargingstore.repository.ChargingSessionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-Memory data structure implementation of Charging Session repository
 */
@Service
public class InMemoryChargingSessionRepositoryImpl implements ChargingSessionRepository {

    private final Map<String, ChargingSession> chargingSessions;

    /**
     * Used ConcurrentHashMap for thread-safe operations and
     * Complexity;
     * "get" and "put" -> O(log(n))
     * "size" -> O(1)
     */
    public InMemoryChargingSessionRepositoryImpl() {
        this.chargingSessions = new ConcurrentHashMap<>();
    }

    public Optional<ChargingSession> getChargingSession(String stationId) {
        return Optional.ofNullable(chargingSessions.getOrDefault(stationId, null));
    }

    public List<ChargingSession> getAllChargingSessions() {
        return new ArrayList<>(chargingSessions.values());
    }

    public boolean createChargingSession(ChargingSession chargingSession) {
        boolean created = false;
        if (chargingSessions.putIfAbsent(chargingSession.getStationId(), chargingSession) == null) {
            created = true;
        }
        return created;
    }

    public void updateChargingSession(ChargingSession chargingSession) {
        chargingSessions.put(chargingSession.getStationId(), chargingSession);
    }
}
