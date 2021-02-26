package com.emrecosar.carchargingstore.api;

import com.emrecosar.carchargingstore.api.models.ChargingSession;
import com.emrecosar.carchargingstore.api.requests.ChargingSessionRequest;
import com.emrecosar.carchargingstore.api.models.ChargingSessionsSummary;
import com.emrecosar.carchargingstore.service.ChargingSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController()
@RequestMapping("/chargingSessions")
@Validated
public class ChargingSessionController {

    private final ChargingSessionService chargingSessionService;

    @Autowired
    public ChargingSessionController(ChargingSessionService chargingSessionService) {
        this.chargingSessionService = chargingSessionService;
    }

    /**
     * Start a charging station session
     *
     * @return {@linkplain ChargingSession}
     */
    @PostMapping
    public ResponseEntity<ChargingSession> createChargingSession(@RequestBody @Valid ChargingSessionRequest request) {
        ChargingSession chargingSession = chargingSessionService.createChargingSession(request.getStationId());
        return new ResponseEntity(chargingSession, HttpStatus.CREATED);
    }

    /**
     * Stop a charging station session
     *
     * @return {@linkplain ChargingSession}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ChargingSession> stopChargingSession(@PathVariable() @NotEmpty(message = "id must not be empty") String id) {
        ChargingSession chargingSession = chargingSessionService.stopChargingSession(id);
        return ResponseEntity.ok(chargingSession);
    }

    /**
     * Fetch and return all charging sessions
     *
     * @return list of {@linkplain ChargingSession}
     */
    @GetMapping()
    public ResponseEntity<List<ChargingSession>> getAllChargingSessions() {
        List<ChargingSession> chargingSessions = chargingSessionService.getAllChargingSessions();
        return ResponseEntity.ok(chargingSessions);
    }

    /**
     * Fetch and return session summary
     *
     * @return {@linkplain ChargingSessionsSummary}
     */
    @GetMapping("/summary")
    public ResponseEntity<ChargingSessionsSummary> getChargingSessionsSummary() {
        ChargingSessionsSummary chargingSessionsSummary = chargingSessionService.getChargingSessionsSummary();
        return ResponseEntity.ok(chargingSessionsSummary);
    }


}
