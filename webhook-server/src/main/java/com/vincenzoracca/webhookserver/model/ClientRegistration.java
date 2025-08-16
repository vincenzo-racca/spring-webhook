package com.vincenzoracca.webhookserver.model;


public record ClientRegistration(
        String clientId,
        String callbackUrl,
        EventFilter eventFilter
) {

    public enum EventFilter {
        ALL,
        COMPLETED,
        CANCELED
    }
}
