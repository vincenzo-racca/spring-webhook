package com.vincenzoracca.webhookserver.model;


public record ClientRegistration(
        String clientId,
        String callbackUrl,
        String secret,
        EventFilter eventFilter
) {

    @Override
    public String toString() {
        return "ClientRegistration{" +
                "clientId='" + clientId + '\'' +
                ", callbackUrl='" + callbackUrl + '\'' +
                ", eventFilter=" + eventFilter +
                '}';
    }

    public enum EventFilter {
        ALL,
        COMPLETED,
        CANCELED
    }
}
