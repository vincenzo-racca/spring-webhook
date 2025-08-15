package com.vincenzoracca.webhookproducer.api;

import com.vincenzoracca.webhookproducer.model.ConsumerRegistration;
import com.vincenzoracca.webhookproducer.model.ConsumerRegistration.EventFilter;
import com.vincenzoracca.webhookproducer.model.ShipmentEvent;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;


@RestController
public class ShipmentController {

    private final JdbcAggregateTemplate jdbcAggregateTemplate;
    private final RestClient restClient;

    public ShipmentController(JdbcAggregateTemplate jdbcAggregateTemplate, RestClient.Builder builder) {
        this.jdbcAggregateTemplate = jdbcAggregateTemplate;
        this.restClient = builder.build();
    }

    @PutMapping("consumers")
    public ResponseEntity<ConsumerRegistration> registerConsumer(@RequestBody ConsumerRegistration consumerRegistration) {
        jdbcAggregateTemplate.save(consumerRegistration);
        return ResponseEntity.ok(consumerRegistration);
    }

    @PostMapping("simulate")
    public ResponseEntity<Void> simulateEvent(@RequestBody ShipmentEvent event) {
        jdbcAggregateTemplate.findAll(ConsumerRegistration.class)
                .stream()
                .filter(consumer -> EventFilter.COMPLETED.equals(consumer.eventFilter()) ||
                        event.status().name().equals(consumer.eventFilter().name()))
                .forEach(client ->
                        restClient.post()
                                .uri(client.callbackUrl())
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(event)
                                .retrieve()
                                .toBodilessEntity()
                );

        return ResponseEntity.noContent().build();
    }
}
