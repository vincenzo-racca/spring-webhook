package com.vincenzoracca.webhookclient.service;

import com.vincenzoracca.webhookclient.model.ShipmentEvent;

public interface ShipmentConsumer {

    void consumeEvent(ShipmentEvent event);
}
