package com.vincenzoracca.webhookserver.api;

import com.vincenzoracca.webhookserver.model.ShipmentEvent;
import com.vincenzoracca.webhookserver.service.ShipmentProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("simulate")
public class SimulatorController {

    private final ShipmentProducer shipmentProducer;

    public SimulatorController(ShipmentProducer shipmentProducer) {
        this.shipmentProducer = shipmentProducer;
    }

    @PostMapping("simulate")
    public ResponseEntity<Void> simulateEvent(@RequestBody ShipmentEvent event) {
        shipmentProducer.sendEvent(event);
        return ResponseEntity.noContent().build();
    }
}
