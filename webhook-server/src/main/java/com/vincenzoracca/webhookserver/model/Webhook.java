package com.vincenzoracca.webhookserver.model;


public record Webhook(
        String webhookId,
        String callbackUrl,
        String secret,
        EventFilter eventFilter
) {

    @Override
    public String toString() {
        return "Webhook{" +
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
