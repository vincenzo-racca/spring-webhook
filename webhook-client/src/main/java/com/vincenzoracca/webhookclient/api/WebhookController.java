package com.vincenzoracca.webhookclient.api;

import com.vincenzoracca.webhookclient.model.ShipmentEvent;
import com.vincenzoracca.webhookclient.service.WebhookClientService;
import com.vincenzoracca.webhookclient.util.SecurityClientUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("webhook")
public class WebhookController {

    private static final String SECRET = "secret";

    private final WebhookClientService webhookClientService;
    private final SecurityClientUtil securityClientUtil;

    public WebhookController(WebhookClientService webhookClientService,
                             SecurityClientUtil securityClientUtil) {
        this.webhookClientService = webhookClientService;
        this.securityClientUtil = securityClientUtil;
    }

    @PostMapping
    public ResponseEntity<Void> consumeEvent(
            @RequestHeader("X-Timestamp") long requestTimestamp,
            @RequestHeader("X-Signature") String signature,
            @RequestBody ShipmentEvent event) {

        if(! securityClientUtil.isInToleranceTime(requestTimestamp)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if(! securityClientUtil.verifySignature(SECRET, requestTimestamp, event, signature)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        webhookClientService.consumeEvent(event);
        return ResponseEntity.accepted().build();
    }
}
