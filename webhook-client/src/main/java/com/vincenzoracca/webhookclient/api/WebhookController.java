package com.vincenzoracca.webhookclient.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincenzoracca.webhookclient.model.ShipmentEvent;
import com.vincenzoracca.webhookclient.service.WebhookClientService;
import com.vincenzoracca.webhookclient.util.SecurityClientUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("webhook")
public class WebhookController {

    private static final String SECRET = "secret"; // secret mocked

    private final WebhookClientService webhookClientService;
    private final SecurityClientUtil securityClientUtil;
    private final ObjectMapper objectMapper;

    public WebhookController(WebhookClientService webhookClientService,
                             SecurityClientUtil securityClientUtil, ObjectMapper objectMapper) {
        this.webhookClientService = webhookClientService;
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
        webhookClientService.consumeEvent(event);
        return ResponseEntity.accepted().build();
    }
}
