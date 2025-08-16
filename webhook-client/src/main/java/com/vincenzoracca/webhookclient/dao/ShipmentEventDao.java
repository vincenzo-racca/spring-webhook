package com.vincenzoracca.webhookclient.dao;

import com.vincenzoracca.webhookclient.model.ShipmentEvent;


public interface ShipmentEventDao {

    ShipmentEvent putIfAbsent(ShipmentEvent event);
}
