package com.vincenzoracca.webhookclient.service;

import com.vincenzoracca.webhookclient.dao.ShipmentEventDao;
import com.vincenzoracca.webhookclient.model.ShipmentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WebhookClientService {

    private static final Logger log = LoggerFactory.getLogger(WebhookClientService.class);

    private final ShipmentEventDao shipmentEventDao;

    public WebhookClientService(ShipmentEventDao shipmentEventDao) {
        this.shipmentEventDao = shipmentEventDao;
    }

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
