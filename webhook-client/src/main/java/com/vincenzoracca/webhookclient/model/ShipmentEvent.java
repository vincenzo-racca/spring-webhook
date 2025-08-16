package com.vincenzoracca.webhookclient.model;

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
