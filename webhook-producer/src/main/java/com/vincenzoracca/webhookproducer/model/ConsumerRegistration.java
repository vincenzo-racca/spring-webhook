package com.vincenzoracca.webhookproducer.model;

import org.springframework.data.annotation.Id;

import java.net.URI;


public record ConsumerRegistration(
        @Id String id,
        URI callbackUrl,
        EventFilter eventFilter
) {

    public enum EventFilter {
        ALL,
        COMPLETED,
        CANCELED
    }
}
