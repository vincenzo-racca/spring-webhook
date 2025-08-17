package com.vincenzoracca.webhookserver.model;

public record WebhookRegistrationRequest(
        String callbackUrl,
        Webhook.EventFilter eventFilter
) {
}
