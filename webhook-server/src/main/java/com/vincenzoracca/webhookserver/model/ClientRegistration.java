package com.vincenzoracca.webhookserver.model;


public record ClientRegistration(
        String clientId,
        String callbackUrl,
        String secret,
        EventFilter eventFilter
) {

    public enum EventFilter {
        ALL,
        COMPLETED,
        CANCELED
    }
}
