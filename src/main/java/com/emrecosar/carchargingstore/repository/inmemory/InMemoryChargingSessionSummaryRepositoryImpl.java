package com.emrecosar.carchargingstore.repository.inmemory;

import com.emrecosar.carchargingstore.api.models.ChargingSessionsSummary;
import com.emrecosar.carchargingstore.repository.ChargingSessionSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * In-Memory with data structure implementation of Charging Session Summary repository
 * <p>
 * Used PriorityBlockingQueue as an in-memory data structure for thread-safe operations
 * Complexity;
 * "offer" and "size" -> O(log(n))
 * "remove" -> O(1)
 */
@Service
public class InMemoryChargingSessionSummaryRepositoryImpl implements ChargingSessionSummaryRepository {

    private final Queue<LocalDateTime> startedSessions;
    private final Queue<LocalDateTime> stoppedSessions;

    @Value("${summary.period.minutes:1}")
    private Long summaryPeriodMinutes;

    @Autowired
    public InMemoryChargingSessionSummaryRepositoryImpl() {
        startedSessions = new PriorityBlockingQueue<>();
        stoppedSessions = new PriorityBlockingQueue<>();
    }

    @Override
    public void started(LocalDateTime started) {
        startedSessions.offer(started);
    }

    @Override
    public void stopped(LocalDateTime stopped) {
        stoppedSessions.offer(stopped);
    }

    @Override
    public ChargingSessionsSummary getSummary() {
        removeOutOfPeriodRecords();
        return new ChargingSessionsSummary(startedSessions.size() + stoppedSessions.size(), startedSessions.size(), stoppedSessions.size());
    }

    /**
     * Remove out of period records from both Queue.
     */
    private void removeOutOfPeriodRecords() {

        LocalDateTime minimumTimeToCountFrom = LocalDateTime.now().minusMinutes(summaryPeriodMinutes);

        while (!stoppedSessions.isEmpty() && stoppedSessions.peek().isBefore(minimumTimeToCountFrom)) {
            stoppedSessions.remove();
        }

        while (!startedSessions.isEmpty() && startedSessions.peek().isBefore(minimumTimeToCountFrom)) {
            startedSessions.remove();
        }

    }

}
