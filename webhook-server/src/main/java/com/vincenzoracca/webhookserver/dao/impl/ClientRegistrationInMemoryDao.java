package com.vincenzoracca.webhookserver.dao.impl;

import com.vincenzoracca.webhookserver.dao.ClientRegistrationDao;
import com.vincenzoracca.webhookserver.model.ClientRegistration;
import com.vincenzoracca.webhookserver.model.ClientRegistration.EventFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ClientRegistrationInMemoryDao implements ClientRegistrationDao {

    private static final Logger log = LoggerFactory.getLogger(ClientRegistrationInMemoryDao.class);

    private static final Map<String, ClientRegistration> CLIENTS_DB = new ConcurrentHashMap<>();

    @Override
    public void insert(ClientRegistration registration) {
        var client = CLIENTS_DB.putIfAbsent(registration.webhookId(), registration);
        if (client != null) {
            throw new IllegalArgumentException("Client already exists");
        }
        log.debug("Client inserted: {}", registration);
    }

    public List<ClientRegistration> findAllByEventFilters(EventFilter eventFilter) {
        return CLIENTS_DB.values().stream()
                .filter(client ->
                        client.eventFilter().equals(EventFilter.ALL) ||
                        eventFilter.equals(client.eventFilter()))
                .toList();
    }
}
