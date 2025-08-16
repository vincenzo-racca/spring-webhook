package com.vincenzoracca.webhookclient.api;

import com.vincenzoracca.webhookclient.model.ShipmentEvent;
import com.vincenzoracca.webhookclient.service.WebhookClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("webhook")
public class WebhookController {

    private final WebhookClientService webhookClientService;

    public WebhookController(WebhookClientService webhookClientService) {
        this.webhookClientService = webhookClientService;
    }

    @PostMapping
    public ResponseEntity<Void> consumeEvent(@RequestBody ShipmentEvent event) {
        webhookClientService.consumerEvent(event);
        return ResponseEntity.accepted().build();
    }
}
