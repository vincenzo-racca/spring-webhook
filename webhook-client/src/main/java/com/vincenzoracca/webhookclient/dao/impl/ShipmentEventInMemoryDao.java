package com.vincenzoracca.webhookclient.dao.impl;

import com.vincenzoracca.webhookclient.dao.ShipmentEventDao;
import com.vincenzoracca.webhookclient.model.ShipmentEvent;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Repository
public class ShipmentEventInMemoryDao implements ShipmentEventDao {

    private static final Map<String, ShipmentEvent> EVENTS_DB = new ConcurrentHashMap<>();


    public ShipmentEvent putIfAbsent(ShipmentEvent event) {
        return EVENTS_DB.putIfAbsent(event.eventId(), event);
    }
}
