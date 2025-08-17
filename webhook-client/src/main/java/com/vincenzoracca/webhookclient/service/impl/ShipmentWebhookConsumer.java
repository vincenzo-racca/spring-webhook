package com.vincenzoracca.webhookclient.service.impl;

import com.vincenzoracca.webhookclient.dao.ShipmentEventDao;
import com.vincenzoracca.webhookclient.model.ShipmentEvent;
import com.vincenzoracca.webhookclient.service.ShipmentConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ShipmentWebhookConsumer implements ShipmentConsumer {

    private static final Logger log = LoggerFactory.getLogger(ShipmentWebhookConsumer.class);

    private final ShipmentEventDao shipmentEventDao;

    public ShipmentWebhookConsumer(ShipmentEventDao shipmentEventDao) {
        this.shipmentEventDao = shipmentEventDao;
    }

    @Override
    public void consumeEvent(ShipmentEvent event) {
        ShipmentEvent oldValue = shipmentEventDao.putIfAbsent(event);
        if(oldValue == null) {
            log.info("Shipment event {} has been saved", event);
        }
        else {
            log.warn("Shipment event {} duplicated", event);
        }
    }
}
