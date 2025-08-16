package com.vincenzoracca.webhookserver.model;

public record ClientRegistrationRequest(
        String callbackUrl,
        ClientRegistration.EventFilter eventFilter
) {
}
