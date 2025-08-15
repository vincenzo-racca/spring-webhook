package com.vincenzoracca.webhookproducer.model;

import org.springframework.data.annotation.Id;

public record ShipmentEvent(
        @Id String orderId,
        ShipmentStatus status
) {

    public enum ShipmentStatus {
        COMPLETED,
        CANCELED,
    }
}
