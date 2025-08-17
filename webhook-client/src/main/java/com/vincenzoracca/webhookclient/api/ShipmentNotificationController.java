package com.vincenzoracca.webhookclient.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincenzoracca.webhookclient.model.ShipmentEvent;
import com.vincenzoracca.webhookclient.service.ShipmentConsumer;
import com.vincenzoracca.webhookclient.util.SecurityClientUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("shipment-notifications")
public class ShipmentNotificationController {

    private static final String SECRET = "secret"; // secret mocked

    private final ShipmentConsumer shipmentConsumer;
    private final SecurityClientUtil securityClientUtil;
    private final ObjectMapper objectMapper;

    public ShipmentNotificationController(ShipmentConsumer shipmentConsumer,
                                          SecurityClientUtil securityClientUtil, ObjectMapper objectMapper) {
        this.shipmentConsumer = shipmentConsumer;
        this.securityClientUtil = securityClientUtil;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<Void> consumeEvent(
            @RequestHeader("X-Timestamp") long requestTimestamp,
            @RequestHeader("X-Signature") String signature,
            @RequestBody String rawBody) throws JsonProcessingException {

        if(! securityClientUtil.isInToleranceTime(requestTimestamp)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if(! securityClientUtil.verifySignature(SECRET, requestTimestamp, rawBody, signature)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var event = objectMapper.readValue(rawBody, ShipmentEvent.class);
        shipmentConsumer.consumeEvent(event);
        return ResponseEntity.accepted().build();
    }
}
