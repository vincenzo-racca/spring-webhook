package com.vincenzoracca.webhookserver.service;

import com.vincenzoracca.webhookserver.model.ShipmentEvent;

public interface ShipmentProducer {

    void sendEvent(ShipmentEvent event);
}
