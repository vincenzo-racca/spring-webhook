package com.vincenzoracca.webhookserver.model;

public record ShipmentEvent(
        String eventId,
        String orderId,
        ShipmentStatus status
) {

    public enum ShipmentStatus {
        COMPLETED,
        CANCELED,
    }
}
