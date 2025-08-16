package com.vincenzoracca.webhookserver.api;

import com.vincenzoracca.webhookserver.model.ClientRegistration;
import com.vincenzoracca.webhookserver.model.ClientRegistrationRequest;
import com.vincenzoracca.webhookserver.model.ShipmentEvent;
import com.vincenzoracca.webhookserver.service.WebhookServerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("clients")
public class ClientController {

    private final WebhookServerService webhookServerService;

    public ClientController(WebhookServerService webhookServerService) {
        this.webhookServerService = webhookServerService;
    }

    @PostMapping
    public ResponseEntity<ClientRegistration> registerClient(@RequestBody ClientRegistrationRequest request) {
        var client = webhookServerService.registerClient(request);
        return ResponseEntity.ok(client);
    }

    @PostMapping("simulate")
    public ResponseEntity<Void> simulateEvent(@RequestBody ShipmentEvent event) {
        webhookServerService.sendEvent(event);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handleException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }
}
