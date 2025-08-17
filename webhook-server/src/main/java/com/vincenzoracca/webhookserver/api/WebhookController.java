package com.vincenzoracca.webhookserver.api;

import com.vincenzoracca.webhookserver.model.Webhook;
import com.vincenzoracca.webhookserver.model.WebhookRegistrationRequest;
import com.vincenzoracca.webhookserver.service.impl.WebhookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("webhooks")
public class WebhookController {

    private final WebhookService webhookService;

    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping
    public ResponseEntity<Webhook> registerWebhook(@RequestBody WebhookRegistrationRequest request) {
        var client = webhookService.registerWebhook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }



    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handleException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }
}
