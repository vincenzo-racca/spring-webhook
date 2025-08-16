package com.vincenzoracca.webhookserver.model;


public record ClientRegistration(
        String webhookId,
        String callbackUrl,
        String secret,
        EventFilter eventFilter
) {

    @Override
    public String toString() {
        return "ClientRegistration{" +
                "webhookId='" + webhookId + '\'' +
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
