package com.emrecosar.carchargingstore;

import com.emrecosar.carchargingstore.api.models.StatusEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.emrecosar.carchargingstore.api.models.ChargingSession;
import com.emrecosar.carchargingstore.api.models.ChargingSessionsSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ChargingSessionIntegrationTest {

    private static final Logger logger = Logger.getLogger(ChargingSessionIntegrationTest.class.getName());
    private final String API_BASE = "/chargingSessions";
    private final String FIELD_STATION_ID = "stationId";
    @Autowired
    protected TestRestTemplate testRestTemplate;
    protected ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = objectMapper();
    }

    // Bad Requests
    @Test
    public void givenStationId_whenCreateSameSessionTwice_thenReturnBadRequest() throws JsonProcessingException {

        ObjectNode request = objectMapper.createObjectNode();
        request.put(FIELD_STATION_ID, UUID.randomUUID().toString());

        ResponseEntity<ChargingSession> responseEntity = testRestTemplate.exchange(
                API_BASE,
                HttpMethod.POST,
                new HttpEntity<>(objectMapper.writeValueAsString(request), getDefaultHeader()),
                ChargingSession.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        ResponseEntity<String> badRequestResponse = testRestTemplate.exchange(
                API_BASE,
                HttpMethod.POST,
                new HttpEntity<>(objectMapper.writeValueAsString(request), getDefaultHeader()),
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, badRequestResponse.getStatusCode());
    }

    @Test
    public void givenStationId_whenSessionIsNullOrEmpty_thenReturnBadRequest() throws JsonProcessingException {

        // null body
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                API_BASE,
                HttpMethod.POST,
                new HttpEntity<>(null, getDefaultHeader()),
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        // body with empty stationId
        ObjectNode body = objectMapper.createObjectNode();
        body.put(FIELD_STATION_ID, "");
        responseEntity = testRestTemplate.exchange(
                API_BASE,
                HttpMethod.POST,
                new HttpEntity<>(objectMapper.writeValueAsString(body), getDefaultHeader()),
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("'stationId' is empty", objectMapper.readTree(responseEntity.getBody()).get("message").asText());
    }

    // Not Found request
    @Test
    public void givenNonExistStationId_whenStopSession_thenResourceNotFound() {

        String stationId = UUID.randomUUID().toString();
        ObjectNode request = objectMapper.createObjectNode();
        request.put(FIELD_STATION_ID, stationId);

        ResponseEntity<ChargingSession> responseEntity = testRestTemplate.exchange(
                API_BASE + "/" + stationId,
                HttpMethod.PUT,
                new HttpEntity<>(null, getDefaultHeader()),
                ChargingSession.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    // A Happy Path and Bad Request
    @Test
    public void givenStationId_whenStartAndStopSession_thenComplete() throws JsonProcessingException {

        String stationId = UUID.randomUUID().toString();
        ObjectNode request = objectMapper.createObjectNode();
        request.put(FIELD_STATION_ID, stationId);

        ResponseEntity<ChargingSession> responseEntity = testRestTemplate.exchange(
                API_BASE,
                HttpMethod.POST,
                new HttpEntity<>(objectMapper.writeValueAsString(request), getDefaultHeader()),
                ChargingSession.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(stationId, responseEntity.getBody().getStationId());
        assertEquals(StatusEnum.IN_PROGRESS, responseEntity.getBody().getStatus());
        assertNull(responseEntity.getBody().getStoppedAt());

        responseEntity = testRestTemplate.exchange(
                API_BASE + "/" + stationId,
                HttpMethod.PUT,
                new HttpEntity<>(null, getDefaultHeader()),
                ChargingSession.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(stationId, responseEntity.getBody().getStationId());
        assertEquals(StatusEnum.FINISHED, responseEntity.getBody().getStatus());
        assertNotNull(responseEntity.getBody().getStoppedAt());

        // try to stop again
        ResponseEntity<String> badRequestResponse = testRestTemplate.exchange(
                API_BASE + "/" + stationId,
                HttpMethod.PUT,
                new HttpEntity<>(null, getDefaultHeader()),
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, badRequestResponse.getStatusCode());
        assertEquals("The station is already stopped",
                objectMapper.readTree(badRequestResponse.getBody()).get("message").asText());
    }


    @Test
    public void givenStationIds_whenRequestAllChargingSessions_thenReturnAllSessions() throws JsonProcessingException {

        ResponseEntity<ChargingSession[]> sessionsBefore = testRestTemplate.exchange(
                API_BASE,
                HttpMethod.GET,
                new HttpEntity<>(null, getDefaultHeader()),
                ChargingSession[].class);

        assertEquals(HttpStatus.NOT_FOUND, sessionsBefore.getStatusCode());

        int count = 100;
        for (int i = 0; i < count; i++) {
            String stationId = UUID.randomUUID().toString();
            ObjectNode request = objectMapper.createObjectNode();
            request.put(FIELD_STATION_ID, stationId);

            // create
            testRestTemplate.exchange(
                    API_BASE,
                    HttpMethod.POST,
                    new HttpEntity<>(objectMapper.writeValueAsString(request), getDefaultHeader()),
                    Object.class);
        }

        ResponseEntity<ChargingSession[]> sessionsAfter = testRestTemplate.exchange(
                API_BASE,
                HttpMethod.GET,
                new HttpEntity<>(null, getDefaultHeader()),
                ChargingSession[].class);

        assertEquals(HttpStatus.OK, sessionsAfter.getStatusCode());
        assertEquals(count, sessionsAfter.getBody().length);
    }

    // "Start" and "Stop" some charging stations and monitor the summary
    // This integration test includes Thread.sleep to be able to see changes in the summary
    @Test
    public void givenMultipleStationId_whenRequestSummary_thenReturnSummary() throws JsonProcessingException, InterruptedException {

        final String API_SUMMARY = API_BASE + "/summary";
        List<String> allCreatedSessions = new ArrayList<>();

        // Start 1000 charging stations
        int startedSessionCount = 1000;
        for (int i = 0; i < startedSessionCount; i++) {

            String stationId = UUID.randomUUID().toString();
            allCreatedSessions.add(stationId);
            ObjectNode request = objectMapper.createObjectNode();
            request.put(FIELD_STATION_ID, stationId);
            ResponseEntity<Object> responseEntity = testRestTemplate.exchange(
                    API_BASE,
                    HttpMethod.POST,
                    new HttpEntity<>(objectMapper.writeValueAsString(request), getDefaultHeader()),
                    Object.class);
            responseEntity.getStatusCode();
        }

        // stop 100 charging session
        int stoppedSessionCount = 100;
        for (int i = 0; i < stoppedSessionCount; i++) {
            testRestTemplate.exchange(
                    API_BASE + "/" + allCreatedSessions.get(i),
                    HttpMethod.PUT,
                    new HttpEntity<>(null, getDefaultHeader()),
                    Object.class);
        }

        ResponseEntity<ChargingSessionsSummary> summaryBefore = testRestTemplate.exchange(
                API_SUMMARY,
                HttpMethod.GET,
                new HttpEntity<>(null, getDefaultHeader()),
                ChargingSessionsSummary.class);

        assertEquals(HttpStatus.OK, summaryBefore.getStatusCode());
        assertEquals(startedSessionCount + stoppedSessionCount, summaryBefore.getBody().getTotalCount());
        assertEquals(startedSessionCount, summaryBefore.getBody().getStartedCount());
        assertEquals(stoppedSessionCount, summaryBefore.getBody().getStoppedCount());

        // wait more than a minute to see changes in summary
        logger.info("Waiting 1 minute for summary");
        Thread.sleep(1000 * 61);

        // stop another 100 charging session starting from latest stopped index
        for (int i = stoppedSessionCount; i < stoppedSessionCount * 2; i++) {
            testRestTemplate.exchange(
                    API_BASE + "/" + allCreatedSessions.get(i),
                    HttpMethod.PUT,
                    new HttpEntity<>(null, getDefaultHeader()),
                    Object.class);
        }

        // expect the last 100 stopped sessions in the response after a minute wait.
        ResponseEntity<ChargingSessionsSummary> summaryAfter = testRestTemplate.exchange(
                API_SUMMARY,
                HttpMethod.GET,
                new HttpEntity<>(null, getDefaultHeader()),
                ChargingSessionsSummary.class);

        assertEquals(HttpStatus.OK, summaryAfter.getStatusCode());
        assertEquals(stoppedSessionCount, summaryAfter.getBody().getTotalCount());
        assertEquals(0, summaryAfter.getBody().getStartedCount());
        assertEquals(stoppedSessionCount, summaryAfter.getBody().getStoppedCount());
    }

    public HttpHeaders getDefaultHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

}